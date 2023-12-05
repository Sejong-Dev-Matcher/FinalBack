package SDM.springmvc.basic.repository;

import SDM.springmvc.basic.domain.ProjectBoardInfo;
import SDM.springmvc.basic.domain.ProjectStackInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProjectBoardRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ProjectBoardInfo> rowMapper = (rs, rowNum) -> {
        ProjectBoardInfo projectBoardInfo = new ProjectBoardInfo();
        projectBoardInfo.setProjectBoardId(rs.getLong("project_board_id"));
        projectBoardInfo.setStudentId(rs.getLong("student_id"));
        projectBoardInfo.setTitle(rs.getString("title"));
        projectBoardInfo.setContent(rs.getString("content"));
        projectBoardInfo.setMaxpeople(rs.getInt("maxpeople"));
        projectBoardInfo.setNowpeople(rs.getInt("nowpeople"));
        projectBoardInfo.setStackInfoList(findStacksByProjectPostId(projectBoardInfo.getProjectBoardId()));
        return projectBoardInfo;
    };

    public List<ProjectBoardInfo> findProjectAll() { //게시글 전체 조회
        String sql = "SELECT * FROM project_board_info";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public ProjectBoardInfo findProjectById(Long postId) { //게시글 하나 조회(id로찾음)
        String sql = "SELECT * FROM project_board_info WHERE project_board_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, postId);
    }

    @Transactional
    public Long saveProject(ProjectBoardInfo projectBoardInfo) { //게시글 생성
        try {
            projectBoardInfo.setNowpeople(1);
            String sql = "INSERT INTO project_board_info(student_id, title, content, maxpeople, nowpeople) VALUES (?,?,?,?,?)";
            jdbcTemplate.update(sql, projectBoardInfo.getStudentId(), projectBoardInfo.getTitle(), projectBoardInfo.getContent(), projectBoardInfo.getMaxpeople(), projectBoardInfo.getNowpeople());
            Long postId = null;
            try {
                postId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            } catch (EmptyResultDataAccessException e) {
                log.error("ID를 찾을 수 없습니다: " + e.getMessage());
                throw new IllegalStateException("projectBoardInfo 테이블에서 삽입된 ID를 찾을 수 없습니다.", e);
            } catch (IncorrectResultSizeDataAccessException e) {
                log.error("삽입 후 결과 사이즈가 잘못 됨:" + e.getMessage());
                throw new IllegalStateException("projectBoardInfo 테이블에 삽입되었지만 결과 크기가 잘못됐습니다.", e);
            }

            if (projectBoardInfo.getStackInfoList() != null) {
                for (ProjectStackInfo stackInfo : projectBoardInfo.getStackInfoList()) {
                    String sqlStack = "INSERT INTO pj_stack(project_board_id, stack_id) VALUES (?,?)";
                    jdbcTemplate.update(sqlStack, postId, stackInfo.getStackId());
                }
            }
            return postId;
        } catch (Exception e) {
            log.error("Error occured while saving projecgt : {}", e.getMessage());
            return null;
        }
    }

    public void deleteProjectById(Long postId) { //게시글 삭제
        String sql = "DELETE FROM project_board_info WHERE project_board_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    public List<ProjectStackInfo> findStacksByProjectPostId(Long postId) { //프로젝트 게시글에 필요한 스택찾기
        String sql = "SELECT si.* FROM stack_info si INNER JOIN pj_stack psi ON si.stack_id = psi.stack_id WHERE psi.project_board_id = ?";
        return jdbcTemplate.query(sql, new Object[]{postId}, (rs, rowNum) -> {
            ProjectStackInfo stackInfo = new ProjectStackInfo();
            stackInfo.setStackId(rs.getLong("stack_id"));
            stackInfo.setName(rs.getString("stack_name"));
            stackInfo.setImg(rs.getString("img"));
            return stackInfo;
        });
    }

    public List<ProjectBoardInfo> findProjectPostsByStack(Long stackId) { //스택으로 게시글 검색
        String sql = "SELECT * FROM project_board_info pbi INNER JOIN pj_stack psi ON pbi.project_board_id = psi.project_board_id WHERE psi.stack_id = ?";
        return jdbcTemplate.query(sql, new Object[]{stackId}, (rs, rowNum) -> {
            ProjectBoardInfo projectBoardInfo = new ProjectBoardInfo();
            projectBoardInfo.setProjectBoardId(rs.getLong("project_board_id"));
            projectBoardInfo.setStudentId(rs.getLong("student_id"));
            projectBoardInfo.setTitle(rs.getString("title"));
            projectBoardInfo.setContent(rs.getString("content"));
            projectBoardInfo.setMaxpeople(rs.getInt("maxpeople"));
            projectBoardInfo.setNowpeople(rs.getInt("nowpeople"));
            projectBoardInfo.setStackInfoList(findStacksByProjectPostId(projectBoardInfo.getProjectBoardId()));
            return projectBoardInfo;
        });
    }

    public List<ProjectBoardInfo> findProjectPostsByTitle(String title) { //제목으로 게시글 검색
        String sql = "SELECT * FROM project_board_info WHERE title LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + title + "%"}, rowMapper);
    }

    public void updateProjectBoard(ProjectBoardInfo projectBoardInfo) {
        if (projectBoardInfo == null) {
            log.error("ProjectBoardInfo는 null이 될 수 없습니다.");
            return;
        }
        if (projectBoardInfo.getTitle() == null || projectBoardInfo.getContent() == null) {
            log.error("제목과 내용은 null이 될 수 없습니다.");
            return;
        }
        String sql = "UPDATE member_board_info SET title = ?, content =? WHERE member_board_id = ?";
        try {
            jdbcTemplate.update(sql, projectBoardInfo.getTitle(), projectBoardInfo.getContent(), projectBoardInfo.getProjectBoardId());
        } catch (DataAccessException e) {
            log.error("업데이트 하는 도중 에러 발생 member_board_info 테이블");
            return;
        }
        if (projectBoardInfo.getStackInfoList() != null) {
            for (ProjectStackInfo stackInfo : projectBoardInfo.getStackInfoList()) {
                if (stackInfo == null) {
                    continue;
                }
                String stackSql;
                try {
                    if (stackInfo.isDeleted()) {
                        stackSql = "DELETE FROM mem_stack WHERE member_board_id = ? AND stack_id = ?";
                        jdbcTemplate.update(stackSql, projectBoardInfo.getProjectBoardId(), stackInfo.getStackId());
                    } else if (stackInfo.isNew()) {
                        stackSql = "INSERT INTO mem_stack(member_board_id, stack_id) VALUES (?,?)";
                        jdbcTemplate.update(stackSql, projectBoardInfo.getProjectBoardId(), stackInfo.getStackId());
                    }
                } catch (DataAccessException e) {
                    log.error("업데이트 하는 도중 에러 발생 mem_stack 테이블");
                }
            }
        }
    }
}