package kr.or.ddit.member.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberVOWrapper;

@Controller
public class MypageControllerServlet extends HttpServlet{

	//서비스와 의존관계
	@Inject
	private MemberService service;
	
	@RequestMapping("/mypage.do")
	public String myPage(
			Model model
			, MemberVOWrapper principal
	) {
		
		MemberVO authMember = principal.getRealMember();
		
		MemberVO member = service.retrieveMember(authMember.getMemId());
		
		model.addAttribute("member", member);
		
		return "member/memberView"; 
	}
}
