package kr.or.ddit.member.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.UserNotFoundException;
import kr.or.ddit.login.service.AuthenticateService;
import kr.or.ddit.login.service.AuthenticateServiceImpl;
import kr.or.ddit.member.dao.MemberDAO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService{
	@Inject
	private MemberDAO memberDAO;
	@Inject
	private AuthenticateService authService;
	@Inject
	private PasswordEncoder encoder;
	
	@PostConstruct
	public void init() {
		log.info("주입된 객체 : {}, {}, {}", memberDAO, authService, encoder);
	}
	
	@Override
	public ServiceResult createMember(MemberVO member) {
		ServiceResult result = null;
		try {
			retrieveMember(member.getMemId());
			result = ServiceResult.PKDUPLICATED;
		}catch(UserNotFoundException e) {
			String encoded = encoder.encode(member.getMemPass());
			member.setMemPass(encoded);
			int rowcnt = memberDAO.insertMember(member);
			result = rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
		}
		return result;
	}

	@Override
	public List<MemberVO> retrieveMemberList(PagingVO<MemberVO> pagingVO) { //simplecondition이 들어있음
		pagingVO.setTotalRecord(memberDAO.selectTotalRecord(pagingVO));
		
		List<MemberVO> memberList = memberDAO.selectMemberList(pagingVO);
		
		pagingVO.setDataList(memberList);
		
		return memberList;
	}

	@Override
	public MemberVO retrieveMember(String memId) {
		MemberVO member = memberDAO.selectMember(memId);
		if(member==null)
			throw new UserNotFoundException(String.format(memId+"에 해당하는 사용자 없음."));
		return member;
	}

	@Override
	public ServiceResult modifyMember(MemberVO member) {
		MemberVO inputData = new MemberVO();
		inputData.setMemId(member.getMemId());
		inputData.setMemPass(member.getMemPass());
		
		ServiceResult result = authService.authenticate(inputData); //인증의 책임을 다 갖고 있음
		if(ServiceResult.OK.equals(result)) {
			int rowcnt = memberDAO.updateMember(member);
			result = rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
		}
		return result;
	}

	@Override
	public ServiceResult removeMember(MemberVO member) {
		ServiceResult result = authService.authenticate(member); //인증의 책임을 다 갖고 있음
		if(ServiceResult.OK.equals(result)) {
			int rowcnt = memberDAO.deleteMember(member.getMemId());
			result = rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
		}
		return result;
	}

}








