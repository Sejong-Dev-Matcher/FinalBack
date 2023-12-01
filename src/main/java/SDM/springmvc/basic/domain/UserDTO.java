package SDM.springmvc.basic.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id; //데이터 베이스에 저장 될 식별자
    private String student_name;
    private String loginId; //학번
    private String password;
    private String major;
}