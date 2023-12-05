package SDM.springmvc.basic.controller;

import SDM.springmvc.basic.domain.JdbcTemplateLogin;
import SDM.springmvc.basic.domain.UserInfo;
import SDM.springmvc.basic.service.DataSourceConfig;
import SDM.springmvc.basic.service.UserParsing;
import SDM.springmvc.basic.repository.UserRepository;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Slf4j
@Controller
@RestController
@CrossOrigin(origins = "http://3.39.21.137:8080")
public class UserController {//여기서 로그인 회원가입 다

    @PostMapping("/user/join")
    //JSON형식으로 반환받아 "is_auth" 파라미터 확인 -> true인 경우 우리 DB에 추가
    public String userController(@RequestBody UserInfo userInfo) throws URISyntaxException {//아 여기서 자소서도 쓰기로했지
        UserParsing userParsing = new UserParsing();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        String id = userInfo.getId();
        String pw = userInfo.getPw();
        log.info("id = {}, pw = {}", id, pw);
        JsonObject jsonObject = userParsing.Parsing(id, pw);
        JsonObject jsonObject1 = (JsonObject) jsonObject.get("result");
        String auth = jsonObject1.get("is_auth").getAsString();
        log.info("auth = {}", auth);
        //우리 데이터베이스에 이미 존재할 때를 처리 아직 안해줌
        try {
            if (auth.equals("true")) {  //만약 데이터베이스에 존재안하고, 입력한 비밀번호가 학사정보와 같다면,
                UserRepository userRepository = new UserRepository(dataSourceConfig.dataSource());
                userRepository.save(id, pw, jsonObject); //우리 데이터베이스에 저장
                return "ok";
            } else {
                throw new Exception("fail to join");
            }
        } catch (Exception e) {
            return "exception"; //학사정보와 다르다면 exception 반환 -> "이미 가입되었거나, 비밀번호가 틀립니다?" "따로따로 줘야하나?"
        }

    }


    //로그인 데이터베이스에 저장안돼있으면 exception 출력 -> 회원가입하고 다시 와야됨
    @RequestMapping(value = "/user/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String doLogin(@RequestBody UserInfo userInfo) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        String id = userInfo.getId();
        String pw = userInfo.getPw();
        log.info("id = {}", id);
        log.info("pw = {}", pw);
        JdbcTemplateLogin jdbcTemplateLogin = new JdbcTemplateLogin(dataSourceConfig.dataSource());
        try {
            String result = jdbcTemplateLogin.findById(id).toString();
            log.info("result = {}", result);
            if (pw.equals(result)) {
                log.info("pw = {}", pw);
                log.info("ok");
                return "ok";
            } else {
                throw new Exception("fail to join");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage(); //회원가입 하고와라
        }
    }
    @RequestMapping(value = "/user/login", method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}