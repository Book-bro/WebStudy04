<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<h4>WELCOME</h4>
<%-- 로그인 사용자 정보 불러오기 --%>
<c:set var="principal" value="${pageContext.request.userPrincipal }"/>
<c:choose>
	<%-- 로그인 정보가 있으면 --%>
	<c:when test="${not empty principal }">
		<%-- --%>
		<c:set var="authMember" value="${principal.realMember }"/>
		<h4>로그인된 사용자??? : ${principal }</h4>
		프로필이미지:
		<img src="data:image/*;base64,${authMember.base64MemImg }" />
		<a href='<c:url value='/mypage.do' ></c:url>'>${authMember.memName }[${authMember.memRole }]님</a>
		
		<form name="logoutForm" action="<c:url value='/login/logout.do'/>" method="post"></form>
		<%--href="#" : 의미없는 링크를 주어 페이징 안되게함 --%>
		<a href="#" class="logoutBtn"> 로그아웃</a>
		<script>
			$(".logoutBtn").on("click", function(event){
				event.preventDefault();
				document.logoutForm.submit();
				return false;
			});
		</script>
	</c:when>
	<%-- 첫 화면(로그인 정보 없을때) --%>
	<c:otherwise>
		<a href="<c:url value='/login/loginForm.jsp'/>">로그인</a>
		<a href="<c:url value='/member/memberInsert.do'/>">회원가입</a>
	</c:otherwise>
	<%-- c:url은 자동으로 contextPath를 붙여줌 --%>
</c:choose>