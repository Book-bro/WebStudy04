package kr.or.ddit.member.service;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.or.ddit.AbstractTestCase;
import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.UserNotFoundException;
import kr.or.ddit.login.service.AuthenticateService;
import kr.or.ddit.member.dao.MemberDAO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

//Mock request : 모조(서버를 돌리지 않고 테스트를 하므로 모조를 만듬)

@Slf4j
public class MemberServiceImplTest extends AbstractTestCase{
	
	@Inject
	private MemberService service;
	
	@Inject
	private MemberDAO memberDAO;
	@Inject
	private AuthenticateService authService;
	@Inject
	private PasswordEncoder encoder;
	
	private MemberVO member;
	
	@Before //테스트하기전 세팅
	public void setUp() {
		member = new MemberVO();
		member.setMemId("a001");
	}
	
//	@Test
	public void testCreateMember() {
		ServiceResult result = null;
		try {
			testRetrieveMember(member.getMemId());
			result = ServiceResult.PKDUPLICATED;
		}catch(UserNotFoundException e) {
			String encoded = encoder.encode(member.getMemPass());
			member.setMemPass(encoded);
			int rowcnt = memberDAO.insertMember(member);
			result = rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
		}
	}
	
	@Test
	public void testRetrieveMember() {
		MemberVO member1 = memberDAO.selectMember(member.getMemId());
		if(member1==null)
			throw new UserNotFoundException(String.format(member.getMemId()+"에 해당하는 사용자 없음."));
	}

}
