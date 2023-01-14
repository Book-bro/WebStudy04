package kr.or.ddit.login.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.UserNotFoundException;
import kr.or.ddit.login.service.AuthenticateService;
import kr.or.ddit.login.service.AuthenticateServiceImpl;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 1. 검증에 통과하지 못했을 경우, 다시 로그인 폼으로 이동함.
 * 2. 인증에 통과하지 못했을 경우, 다시 로그인 폼으로 이동함.
 * 	 - 비밀번호 오류 상태를 가정하고, 메시지 전달. -> alert 함수로 메시지 출력.
 * 	 - 이전에 입력받은 아이디의 상태를 유지함.
 * 3. 인증 완료시에 웰컴 페이지로 이동함.
 */
@Slf4j
@RequiredArgsConstructor //갖고있는 프로퍼티중에서 파이널이 있으면 걔네만 모아서 생성해줌
@Controller
public class LoginProcessController {
	
	private final AuthenticateService service;
	
	@PostConstruct
	public void init() {
		log.info("주입된 객체 : {}", service);
	}
	
	@RequestMapping(value="/login/loginProcess.do", method=RequestMethod.POST)
	public String loginProcess(HttpSession session, HttpServletRequest req,  HttpServletResponse resp) throws ServletException, IOException {
		//로그인 화면에서 아무것도 안하고 일정시간이 지나면
		if(session.isNew()) {
			resp.sendError(400, "로그인 폼이 없는데 어떻게 로그인을 하지??");
			return null; //이미 상태코드가 유지됨
		}
		String memId = req.getParameter("memId");  //아이디 
		String memPass = req.getParameter("memPass"); //비밀번호 
		String saveId = req.getParameter("saveId"); //저장된 아이디
		MemberVO member = new MemberVO(); //객체 생성
		member.setMemId(memId); //객체에 아이디 저장
		member.setMemPass(memPass); //객체에 비밀번호 저장
		
		boolean valid = validate(member); //유효성 검사
		String viewName = null;
		
		if(valid) {
			try {
				ServiceResult result = service.authenticate(member); //아이디와 비밀번호가 일치하는지 검증
	
				if(ServiceResult.OK.equals(result)) {//검증이 완료됬으면
					Cookie saveIdCookie = new Cookie("saveId", member.getMemId()); //쿠키생성(쿠키이름,쿠키값)
	//				ex) www[blog].naver.com
					saveIdCookie.setDomain("localhost"); //도메인 설정
					saveIdCookie.setPath(req.getContextPath()); //경로 설정
					int maxAge = 0;
					if(StringUtils.isNotBlank(saveId)){ //저장된 아이디가 있으면
						maxAge = 60*60*24*5;	//지속시간 5일
					}
					saveIdCookie.setMaxAge(maxAge); //지속시간 설정
					resp.addCookie(saveIdCookie);	//쿠키저장
					session.setAttribute("authMember", member);	//검증된 값 저장
					viewName="redirect:/";	//메인화면
				}else {	//비밀번호가 다를때
					session.setAttribute("validId", memId);	//아이디 저장
					session.setAttribute("message", "비밀번호 오류");
					viewName="redirect:/login/loginForm.jsp";
				}
			}catch(UserNotFoundException e) {	//없은 아이디 일때
				session.setAttribute("message", "존재하지 않는 회원입니다.");
				viewName="redirect:/login/loginForm.jsp";
			}
		}else { //아이디나 비밀번호를 하나라도 입력 안했을때
			session.setAttribute("message", "아이디나 비밀번호 누락");
			viewName="redirect:/login/loginForm.jsp";
		}
		
		return viewName;
	}

	private boolean validate(MemberVO member) {
		boolean valid = true;
		
		if(StringUtils.isBlank(member.getMemId())) {  //아이디가 없으면
			valid = false;
		}
		if(StringUtils.isBlank(member.getMemPass())) { //비밀번호가 없으면
			valid = false;
		}
		
		return valid;
	}
}
