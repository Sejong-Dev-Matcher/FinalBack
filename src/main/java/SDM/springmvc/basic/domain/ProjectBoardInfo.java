package SDM.springmvc.basic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBoardInfo {
    private long projectBoardId;
    private long studentId;
    private String title;
    private String content;
    private int maxpeople;
    private int nowpeople;
    private List<ProjectStackInfo> stackInfoList;
    //{"post_Id":1, "user_id":19011512, "title":"캡스톤 팀원 구합니다", "contents":"2023-2학기 캡스톤 팀원 구합니다"}
}