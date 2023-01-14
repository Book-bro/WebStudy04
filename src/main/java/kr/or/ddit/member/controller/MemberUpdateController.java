package kr.or.ddit.member.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.validate.UpdateGroup;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberVOWrapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member/memberUpdate.do")
public class MemberUpdateController{
	private final MemberService service; 
	
	@GetMapping
	public String updateForm(
			@SessionAttribute("authMember") MemberVO authMember
			,Model model
	) {
		
		MemberVO member = service.retrieveMember(authMember.getMemId());
		//모델로 공유
		model.addAttribute("member", member);
		//뷰 선택
		String viewName = "member/memberForm";
		
		return viewName;
	}
	
	@PostMapping
	public String updateProcess(
			Model model
			,@Validated(UpdateGroup.class) @ModelAttribute("member") MemberVO member 
			,BindingResult errors
			,HttpSession session
	) throws IOException {
		
		String viewName = null;
		
		boolean valid = !errors.hasErrors();
		
		if(valid) {
			ServiceResult result = service.modifyMember(member);
			switch(result){
			case INVALIDPASSWORD:
				model.addAttribute("message", "비밀번호 오류");
				viewName = "member/memberForm";
				break;
			case FAIL:
				model.addAttribute("message", "서버 오류, 좀따 다시");
				viewName = "member/memberForm";
				break;
				
			default:
				
				MemberVO modifiedMember =  service.retrieveMember(member.getMemId());
				session.setAttribute("authMember", modifiedMember);
				
				viewName = "redirect:/mypage.do";
				break;
			}
		}else {
			viewName = "member/memberForm";
		}
		return viewName;
	}
}





