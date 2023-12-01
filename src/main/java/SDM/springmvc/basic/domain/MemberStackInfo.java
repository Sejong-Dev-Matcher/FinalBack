package SDM.springmvc.basic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberStackInfo {
    private long stackId;
    private String name;
    private String img;
    private boolean isDeleted;
    private boolean isNew;
}