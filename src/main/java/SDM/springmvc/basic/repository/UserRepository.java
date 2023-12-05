package SDM.springmvc.basic.repository;

import SDM.springmvc.basic.domain.UserDTO;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

@Slf4j
@Repository
public class UserRepository { //여기서 자소서,학번(loginId),pw,major,이름 다 넣어줘야됨. 학번이랑 pw도 입력한거, 이름,major는 파싱

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //자소서는 사용자가 회원가입할 때 입력한거 넣을거고 나머지는 세종api 파싱한거에서 가져올거임
    //userDTO 객체를 하나 만들어서 jsonobject파싱하고 아이디 패스워드, 자소서 다 넣어둔 다음에
    //ps객체에 넣는건가
    public UserDTO save(String loginId, String pw, JsonObject jsonObject) {
        String sql = "insert into student(loginId,pw,student_name,major) values (?,?,?,?)";
        UserDTO userDTO = new UserDTO();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        userDTO.setLoginId(loginId);
        userDTO.setPassword(pw);
        JsonObject jsonObject1 = (JsonObject) jsonObject.get("result");
        JsonObject jsonObject2 = (JsonObject) jsonObject1.get("body"); //마트료시카 두번까기
        userDTO.setMajor(jsonObject2.get("major").getAsString());
        userDTO.setStudent_name(jsonObject2.get("name").getAsString());
        log.info("userDTO={}",userDTO);
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"loginId"}); //근데 이 두번째 파라미터값은 머야?
            ps.setString(1, userDTO.getLoginId());
            ps.setString(2, userDTO.getPassword());
            ps.setString(3, userDTO.getStudent_name());
            ps.setString(4, userDTO.getMajor());
            return ps;
        }, keyHolder);
        String key = keyHolder.getKey().toString();
        userDTO.setLoginId(key); //key를 primary key로 설정
        return userDTO;
    }

    public void updateMemberBoardId(Long studentId, Long memberBoardId) {
        String sql = "UPDATE student SET member_board_id = ? WHERE student_id = ? and member_board_id is null";
        jdbcTemplate.update(sql, memberBoardId, studentId);
    }
}