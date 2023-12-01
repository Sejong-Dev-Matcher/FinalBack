package SDM.springmvc.basic.domain;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberBoardRequest {
    private MemberBoardInfo memberBoardInfo;
    private long studentId;
}