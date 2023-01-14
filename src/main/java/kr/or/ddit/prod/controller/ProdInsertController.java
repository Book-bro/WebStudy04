package kr.or.ddit.prod.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.prod.dao.OthersDAO;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.vo.BuyerVO;
import kr.or.ddit.vo.ProdVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/prod/prodInsert.do")
public class ProdInsertController{
	
	
	
	
	private final ProdService service;
	private final OthersDAO othersDAO;

	
	
	//핸들러 어뎁터가 알아서 실행
	@ModelAttribute("lprodList")
	public List<Map<String, Object>> lprodList() {
		return othersDAO.selectLprodList();
	}
	
	@ModelAttribute("buyerList")
	public List<BuyerVO> buyerList() {
		return othersDAO.selectBuyerList(null);
	}
	
	@ModelAttribute("prod")
	public ProdVO prod() {
		return new ProdVO();
	}
	
	
	@GetMapping
	public String prodForm(@ModelAttribute("prod") ProdVO prod){
		return "prod/prodForm";
	}

	@PostMapping
	public String insertProcess(
		@Validated(InsertGroup.class) @ModelAttribute("prod") ProdVO prod	//@ModelAttribute : command object 클라이언트가 보내는 모든 데이터를 받음(VO에 설정만 잘해놓으면)
		, Errors errors
		, Model model
	) throws IOException, ServletException {
		
			
		boolean valid = !errors.hasErrors();
		
		String viewName = null;
		if(valid) {
			ServiceResult result = service.createProd(prod);
			if(ServiceResult.OK == result) {
				viewName = "redirect:/prod/"+prod.getProdId();
			}else {
				model.addAttribute("message", "서버 오류, 쫌따 다시");
				viewName = "prod/prodForm";
			}
		}else {
			viewName = "prod/prodForm";
		}
		return viewName;
	}
}

















