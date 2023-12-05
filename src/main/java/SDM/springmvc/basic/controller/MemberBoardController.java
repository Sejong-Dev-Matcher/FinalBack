package SDM.springmvc.basic.controller;

import SDM.springmvc.basic.domain.MemberBoardInfo;
import SDM.springmvc.basic.domain.MemberBoardRequest;
import SDM.springmvc.basic.repository.MemberBoardRepository;
import SDM.springmvc.basic.service.MemberBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin(originPatterns = "http://3.39.21.137:8080")
public class MemberBoardController {
    @Autowired
    private MemberBoardService memberBoardService;
    @Autowired
    private MemberBoardRepository memberBoardRepository;

    @PostMapping("/memberboard/post") // 게시글 작성
    public void post(@RequestBody MemberBoardRequest memberBoardRequest) {
        memberBoardService.saveMemberPost(
                memberBoardRequest.getMemberBoardInfo(),
                memberBoardRequest.getStudentId()
        );
    }

    @GetMapping("/memberboard/viewAll") // 게시글 전체 조회
    public List<MemberBoardInfo> viewAll() {
        return memberBoardService.findAllMemberPost();
    }

    @GetMapping("/memberboard/post/{student_Id}") // 게시글 하나 조회(studentId로)
    public MemberBoardInfo viewOne(@PathVariable Long student_Id) {
        return memberBoardService.findMemberPostById(student_Id);
    }

    @GetMapping("/memberboard/view-stack") // 스택으로 게시글 찾기
    public List<MemberBoardInfo> viewMemberByStack(Long stack_Id) {
        return memberBoardService.findMemberPostByStack(stack_Id);
    }

    @PutMapping("/memberboard/update/{student_Id}")
    public String updateMemberPost(@PathVariable Long student_Id, @ModelAttribute MemberBoardInfo memberBoardInfo) {
        MemberBoardInfo existingBoardInfo = memberBoardRepository.getMemberPost(student_Id);
        if (existingBoardInfo != null) {
            existingBoardInfo.setMemberBoardId(memberBoardInfo.getMemberBoardId());
            existingBoardInfo.setContent(memberBoardInfo.getContent());
            existingBoardInfo.setUsername(memberBoardInfo.getUsername());
            existingBoardInfo.setStackInfoList(memberBoardInfo.getStackInfoList());
            existingBoardInfo.setTitle(memberBoardInfo.getTitle());
            memberBoardRepository.updateMemberBoard(existingBoardInfo);
        } else{
            return "redirect:/error";
        }
        return "redirect:/memberboard/post/" + student_Id;
    }
}