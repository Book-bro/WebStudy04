package kr.or.ddit.prod.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.prod.dao.OthersDAO;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.vo.BuyerVO;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ProdVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/prod")
public class ProdListController{
	@Inject
	private ProdService service;
	@Inject
	private OthersDAO othersDAO;
	
	@ModelAttribute("lprodList")
	public List<Map<String, Object>> lprodList() {
		return othersDAO.selectLprodList();
	}
	
	@ModelAttribute("buyerList")
	public List<BuyerVO> buyerList() {
		return othersDAO.selectBuyerList(null);
	}

	@GetMapping
	public String listUI() {
		return "prod/prodList";
	}
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)// 내가 json을 생산하겠다는 뜻(클라이언트가 나한테 json을 요구했을 때-accept라는 헤더를 통해 알 수 있음 여기에 만들면 굳이 요청헤더를 찾을 필요가 없음)

	public String listData(
		@RequestParam(value="page", required=false, defaultValue="1") int currentPage	//값이없을때 1로 설정해 1페이지가 뜨게 만듬
		, @ModelAttribute("detailCondition") ProdVO detailCondition
		, Model model
	) throws ServletException {
		
		PagingVO<ProdVO> pagingVO = new PagingVO<>(5, 2);
		pagingVO.setCurrentPage(currentPage);
		pagingVO.setDetailCondition(detailCondition);
		
		service.retrieveProdList(pagingVO);
		model.addAttribute("pagingVO", pagingVO);
		
		return "jsonView";
		
	}

	//accept를 통해서 UI,Data중 뭐를 원하는지
}















