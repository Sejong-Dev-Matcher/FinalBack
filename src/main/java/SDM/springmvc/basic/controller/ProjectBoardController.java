package SDM.springmvc.basic.controller;

import SDM.springmvc.basic.domain.ProjectBoardInfo;
import SDM.springmvc.basic.service.ProjectBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(originPatterns = "http://3.39.21.137:8080")
public class ProjectBoardController {
    @Autowired
    private ProjectBoardService projectBoardService;

    @PostMapping("/projectboard/post") // 게시글 작성
    public void post(@RequestBody ProjectBoardInfo projectBoardInfo) {
        projectBoardService.saveProjectPost(projectBoardInfo);
    }

    @GetMapping("/projectboard/viewAll") // 게시글 전체 조회
    public List<ProjectBoardInfo> viewAll() {
        return projectBoardService.findAllProjectPost();
    }

    @GetMapping("/projectboard/post/{post_Id}") // 게시글 하나 조회(postid로)
    public ProjectBoardInfo viewOne(@PathVariable Long post_Id) {
        return projectBoardService.findProjectPostById(post_Id);
    }

    @GetMapping("/projectboard/view-stack") // 스택으로 게시글 찾기
    public List<ProjectBoardInfo> viewProjectByStack(Long stack_Id) {
        return projectBoardService.findProjectPostByStack(stack_Id);
    }

    @GetMapping("/projectboard/view-title") // 제목으로 게시글 찾기
    public List<ProjectBoardInfo> viewProjectByTitle(String title) {
        return projectBoardService.findProjectPostByTitle(title);
    }

    @PutMapping("/projectboard/update/{projectBoard_id}") // 게시글 수정
    public String updateProjectPost(@PathVariable Long projectBoard_id, @ModelAttribute ProjectBoardInfo projectBoardInfo) {
        projectBoardInfo.setProjectBoardId(projectBoard_id);
        projectBoardService.updateProjectPost(projectBoardInfo);
        return "redirect:/projectboard/post?" + projectBoard_id;
    }
}