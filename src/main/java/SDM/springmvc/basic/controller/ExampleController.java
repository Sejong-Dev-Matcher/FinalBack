package SDM.springmvc.basic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(originPatterns = "http://3.39.21.137:8080")
public class ExampleController {
    @RequestMapping("/example")
    public String test(){
        log.info("ok");
        return "ok";
    }
}