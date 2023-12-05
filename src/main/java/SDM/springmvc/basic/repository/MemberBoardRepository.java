package SDM.springmvc.basic.repository;

import SDM.springmvc.basic.domain.MemberBoardInfo;
import SDM.springmvc.basic.domain.MemberStackInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberBoardRepository {

    private final JdbcTemplate jdbctemplate;
    private final UserRepository userRepository;

    public String getStudentName(Long studentId) {
        String sql = "SELECT student_name FROM student WHERE student_id = ?";
        try {
            return jdbctemplate.queryForObject(sql, new Object[]{studentId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("학생을 찾을 수 없습니다: " + studentId);
            return null;
        } catch (Exception e){
            System.out.println("검색하는동안 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    private final RowMapper<MemberBoardInfo> rowMapper = (rs, rowNum) -> {
        MemberBoardInfo memberBoardInfo = new MemberBoardInfo();
        memberBoardInfo.setMemberBoardId(rs.getLong("member_board_id"));
        memberBoardInfo.setUsername(rs.getString("username"));
        memberBoardInfo.setTitle(rs.getString("title"));
        memberBoardInfo.setStackInfoList(findStacksByMemberUserId(memberBoardInfo.getMemberBoardId()));
        return memberBoardInfo;
    };

    public List<MemberBoardInfo> findMemberAll() { // 멤버카드 전체조회
        String sql = "SELECT * FROM member_board_info";
        return jdbctemplate.query(sql, rowMapper);
    }

    public MemberBoardInfo findMemberById(Long userId){ // 멤버카드 하나조회
        String sql = "SELECT * FROM member_board_info WHERE member_board_id = ?";
        return jdbctemplate.queryForObject(sql, rowMapper, userId);
    }

    @Transactional
    public Long saveMember(MemberBoardInfo memberBoardInfo, Long studentId) { //멤버카드 생성
        if (memberBoardInfo == null || studentId == null) {
            throw new IllegalArgumentException("MemberBoardInfo와 studentId null이 될 수 없습니다.");
        }
        String username = null;
        try{
            username = getStudentName(studentId);
        } catch (EmptyResultDataAccessException e){
            log.error("학생 ID에 해당하는 학생을 찾을 수 없습니다: " + studentId);
            return null;
        }

        if (username == null) {
            throw new IllegalArgumentException("Username은 null이 될 수 없습니다.");
        }
        memberBoardInfo.setTitle("개발자 " + username);
        String sql = "INSERT INTO member_board_info(username, title, content, student_id) VALUES (?,?,?,?)";
        Long postId = null;

        try {
            jdbctemplate.update(sql, username, memberBoardInfo.getTitle(), memberBoardInfo.getContent(), studentId);
            postId = jdbctemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        } catch (DataAccessException e) {
            log.error("MemberBoardInfo 테이블에 데이터를 삽입하는 도중 오류 발생 : " + e.getMessage());
            return null;
        }

        if (memberBoardInfo.getStackInfoList() != null) {
            for (MemberStackInfo stackInfo : memberBoardInfo.getStackInfoList()) {
                String sqlStack = "INSERT INTO mem_stack(member_board_id,stack_id) VALUES(?,?)";
                try {
                    jdbctemplate.update(sqlStack, postId, stackInfo.getStackId());
                } catch (DataAccessException e) {
                    log.error("Error occurred while inserting data into mem_stack table : " + e.getMessage());
                }
            }
        }
        return postId;
    }

    public List<MemberStackInfo> findStacksByMemberUserId(Long userId) { //멤버카드에 있는 스택찾기
        String sql = "SELECT si.* FROM stack_info si INNER JOIN mem_stack msi ON si.stack_id = msi.stack_id WHERE msi.member_board_id = ?";
        return jdbctemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            MemberStackInfo stackInfo = new MemberStackInfo();
            stackInfo.setStackId(rs.getLong("stack_id"));
            stackInfo.setName(rs.getString("stack_name"));
            stackInfo.setImg(rs.getString("img"));
            return stackInfo;
        } );
    }

    public List<MemberBoardInfo> findMemberPostsByStack(Long stackId) { //스택으로 멤버 찾기
        String sql = "SELECT * FROM member_board_info mbi INNER JOIN mem_stack msi ON mbi.user_id = msi.user_id WHERE msi.stack_id = ?";
        return jdbctemplate.query(sql, new Object[]{stackId}, (rs, rowNum) -> {
            MemberBoardInfo memberBoardInfo = new MemberBoardInfo();
            memberBoardInfo.setMemberBoardId(rs.getLong("member_board_id"));
            memberBoardInfo.setTitle(rs.getString("title"));
            memberBoardInfo.setUsername(rs.getString("username"));
            memberBoardInfo.setStackInfoList(findStacksByMemberUserId(memberBoardInfo.getMemberBoardId()));
            return memberBoardInfo;
        });
    }

    public void updateMemberBoard(MemberBoardInfo memberBoardInfo) {
        String sql = "UPDATE member_board_info SET title = ?, content =? WHERE member_board_id = ?";
        jdbctemplate.update(sql, memberBoardInfo.getTitle(), memberBoardInfo.getContent(), memberBoardInfo.getMemberBoardId());

        for (MemberStackInfo stackInfo : memberBoardInfo.getStackInfoList()) {
            String stackSql;
            if (stackInfo.isDeleted()) {
                stackSql = "DELETE FROM mem_stack WHERE member_board_id = ? AND stack_id = ?";
                jdbctemplate.update(stackSql, memberBoardInfo.getMemberBoardId(), stackInfo.getStackId());
            } else if (stackInfo.isNew()) {
                stackSql = "INSERT INTO mem_stack(member_board_id, stack_id) VALUES (?,?)";
                jdbctemplate.update(stackSql, memberBoardInfo.getMemberBoardId(), stackInfo.getStackId());
            }
        }
    }
}