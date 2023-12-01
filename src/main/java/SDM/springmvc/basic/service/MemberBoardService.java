package SDM.springmvc.basic.service;

import SDM.springmvc.basic.domain.MemberBoardInfo;
import SDM.springmvc.basic.repository.MemberBoardRepository;
import SDM.springmvc.basic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBoardService {
    @Autowired
    private MemberBoardRepository memberBoardRepository;
    @Autowired
    private UserRepository userRepository;

    public void saveMemberPost(MemberBoardInfo memberBoardInfo, Long loggedInUserId) { // 이력서 써서 멤버카드 생성
        Long memberBoardId = memberBoardRepository.saveMember(memberBoardInfo, loggedInUserId);
        userRepository.updateMemberBoardId(loggedInUserId, memberBoardId);
    }

    public List<MemberBoardInfo> findAllMemberPost(){ // 모든 멤버카드 조회
        return memberBoardRepository.findMemberAll();
    }
    public MemberBoardInfo findMemberPostById(Long userId) { // 이력서 조회
        return memberBoardRepository.findMemberById(userId);
    }

    public List<MemberBoardInfo> findMemberPostByStack(Long stackId) { // 스택으로 멤버 찾기
        return memberBoardRepository.findMemberPostsByStack(stackId);
    }

    public void updateMemberPost(MemberBoardInfo memberBoardInfo) {
        memberBoardRepository.updateMemberBoard(memberBoardInfo);
    }

}