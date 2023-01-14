package kr.or.ddit.auth;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberVOWrapper;

public class GeneratePrincipalFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(false);
		MemberVO authMember = null;
		if(session!=null) {
			authMember = (MemberVO) session.getAttribute("authMember");
		}
		if(authMember!=null) { //관리자
			HttpServletRequest modifiedReq = new HttpServletRequestWrapper(req) {//한번 만든 코드는 바꾸지 않는 원칙으로 wrapper를 하나 만들어줌
				@Override
				public Principal getUserPrincipal() {
					HttpServletRequest adaptee = (HttpServletRequest) getRequest();//어뎁티 꺼냄
					HttpSession session = adaptee.getSession(false); //세션이 없으면 만들지 마라
					if(session!=null) {
						MemberVO realMember = (MemberVO) session.getAttribute("authMember");//같은 세션이므로 authMember을 가져올수 있음
						return new MemberVOWrapper(realMember); //principal안에 넣어반환
					}else {
						return super.getUserPrincipal();
					}
				}
			};
			chain.doFilter(modifiedReq, response); //request를 쓰면 필터링이 전달이안됨, 그래서 modifiedReq씀
		}else {  //일반 유저
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void destroy() {
		
		
	}

}
