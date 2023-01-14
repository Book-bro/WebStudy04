package kr.or.ddit.commons;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController{
	
	@RequestMapping("/index.do")
	public String index(HttpServletRequest req){
		req.setAttribute("contentPage", "/WEB-INF/views/index.jsp");
		return "layout";
	}
}
