package kr.or.ddit.member.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

/**
 * Backend controller(command handler) --> Plain Old Java Object
 */
@Slf4j
@Controller
@RequestMapping("/member/memberInsert.do") //이 컨트롤러는 이 요청만 받겠다
public class MemberInsertController {
	@Inject
	private MemberService service;
	
	@ModelAttribute("member") // 핸들러메소드 호출전에 먼저 만들어짐 , 아무 메서드 호출해도 무조건 갖게됨
	public MemberVO member() {
		log.info("@ModelAttribute 메소드 실행 -> member 속성 생성");
		return new MemberVO(); 
	}
	
	//get 방식으로 들어옴
//	@RequestMapping("/member/memberInsert.do") //default는 post를 제외한 모든메소드를 다 받을 수 있음
	@GetMapping //get메소드만 받겠다
	public String memberForm(@ModelAttribute("member") MemberVO member)  { 
		return "member/memberForm";
	}
	
	//post 방식
	@PostMapping
	public String memberForm(
			HttpServletRequest req
			, @Validated(InsertGroup.class)  @ModelAttribute("member") MemberVO member
			, Errors errors
//			, @RequestPart(value="memImage", required=false) MultipartFile memImage //MemberVO에 있어서 이미 만들어져있음
	) throws ServletException, IOException {
		
		boolean valid = !errors.hasErrors();
		
		String viewName = null;
		
		if(valid) {
			ServiceResult result = service.createMember(member);
			switch (result) {
			case PKDUPLICATED:  //이미 있거나
				req.setAttribute("message", "아이디 중복");
				viewName = "member/memberForm";
				break;
			case FAIL:	//문제발생시
				req.setAttribute("message", "서버에 문제 있음. 쫌따 다시 하셈.");
				viewName = "member/memberForm";
				break;

			default:
				viewName = "redirect:/";
				break;
			}
		}else {
			viewName = "member/memberForm";
		}
		return viewName;
		
		
	}
}




















