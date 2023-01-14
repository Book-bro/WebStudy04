package kr.or.ddit.login.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogoutController{
	@PostMapping("/login/logout.do")
	public String process(HttpSession session) {
//		session.removeAttribute("authMember");
		session.invalidate(); //세션 삭제
		
		return "redirect:/";
	}

}
