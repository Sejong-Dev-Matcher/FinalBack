package SDM.springmvc.basic.service;

import SDM.springmvc.basic.domain.ProjectBoardInfo;
import SDM.springmvc.basic.repository.ProjectBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectBoardService {
    @Autowired
    private ProjectBoardRepository projectBoardRepository;

    public void saveProjectPost(ProjectBoardInfo projectBoardInfo) {
        projectBoardRepository.saveProject(projectBoardInfo);
    }

    public List<ProjectBoardInfo> findAllProjectPost(){ // 게시글 모두 조회
        return projectBoardRepository.findProjectAll();
    }

    public ProjectBoardInfo findProjectPostById(Long postId) { // 게시글 하나 조회
        return projectBoardRepository.findProjectById(postId);
    }

    public List<ProjectBoardInfo> findProjectPostByStack(Long stackId){ // 스택으로 게시글 검색
        return projectBoardRepository.findProjectPostsByStack(stackId);
    }

    public List<ProjectBoardInfo> findProjectPostByTitle(String title) { // 제목으로 게시글 검색
        return projectBoardRepository.findProjectPostsByTitle(title);
    }

    public void updateProjectPost(ProjectBoardInfo projectBoardInfo) {
        projectBoardRepository.updateProjectBoard(projectBoardInfo);
    }


}