package SDM.springmvc.basic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberBoardInfo {
    private long memberBoardId;
    private String username;
    private String title; // 썸네일에 나올 개발자 ***
    private String content;
    private List<MemberStackInfo> stackInfoList;
}