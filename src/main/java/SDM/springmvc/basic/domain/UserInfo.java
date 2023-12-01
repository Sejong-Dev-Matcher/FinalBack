package SDM.springmvc.basic.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class UserInfo {
    private String id;
    private String pw;
}