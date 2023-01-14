package kr.or.ddit.login.service;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.UserNotFoundException;
import kr.or.ddit.member.dao.MemberDAO;
import kr.or.ddit.vo.MemberVO;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {
	
	private MemberDAO memberDAO;
	
	@Inject
	public AuthenticateServiceImpl(MemberDAO memberDAO) { //기본생성자를 없앰
		super();
		this.memberDAO = memberDAO;
	}

	@Resource(name="passwordEncoder") //여러개 등록되어 있어도 특정한 녀석만 가져옴
	private PasswordEncoder encoder; //비밀번호 암호화, 우리가 만들지 않은건 수동으로 등록해야함
	
	@Override
	public ServiceResult authenticate(MemberVO member) {  //입력한 아이디 비밀번호가 저장된 객체 받아옴
		MemberVO savedMember = memberDAO.selectMember(member.getMemId()); //아이디에 맞는 회원 검색후 저장
		if(savedMember==null || savedMember.isMemDelete())  //삭제되어 존재하지 않을때
			throw new UserNotFoundException(String.format("%s 사용자 없음.", member.getMemId()));
		String inputPass = member.getMemPass();  //입력받은 비밀번호 
		String savedPass = savedMember.getMemPass(); //입력받은 아이디의 비밀번호
		ServiceResult result = null;
		if(encoder.matches(inputPass, savedPass)) { //입력받는 비번과 암호화된 비번이 같으면 true
			
//			member.setMemName(savedMember.getMemName());
			try {
				//copyProperties : 원본 객체를 복사
				BeanUtils.copyProperties(member, savedMember);//(원본객체, 복사할 객체)
				result = ServiceResult.OK;  //검증 완료
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			
			
		}else {
			result = ServiceResult.INVALIDPASSWORD; //비밀번호가 다름
		}
		return result;
	}

}
