package SDM.springmvc.basic.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sql.DataSource;

@Slf4j
@Repository
@CrossOrigin(originPatterns = "http://3.39.21.137:8080")
public class JdbcTemplateLogin {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateLogin(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String findById(String id) {
        try {
            String result = jdbcTemplate.queryForObject("select pw from student where loginId = ?", String.class, id);
            log.info("result = {}", result);
            return result;
        } catch (Exception e) {
            log.error("데이터베이스에 사용자 존재하지 않음");
            return "exception";
        }
    }
}
