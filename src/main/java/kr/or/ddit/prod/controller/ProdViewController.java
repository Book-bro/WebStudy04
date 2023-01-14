package kr.or.ddit.prod.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.vo.ProdVO;

/**
 * 상품 상세 조회시, 해당 거래처의 모든 정보 함께 조회함.
 * 상품 상세 조회시, 구매자 리스트(회원아이디, 회원명, 휴대폰, 이메일, 마일리지) 함께 조회.
 * 분류명도 함께 조회함.
 *
 */
@Controller
public class ProdViewController{
	@Inject
	private ProdService service;
	
	@RequestMapping("/prod/{prodId}") //경로변수 형태, 파라미터의 개념이 없어짐
	public ModelAndView prodView(
		@PathVariable String prodId
	){
		ModelAndView mav = new ModelAndView();
		
		ProdVO prod = service.retrieveProd(prodId);
		
		mav.addObject("prod", prod);
		
		mav.setViewName("prod/prodView");
		
		return mav; // logical view name
	}
}






















