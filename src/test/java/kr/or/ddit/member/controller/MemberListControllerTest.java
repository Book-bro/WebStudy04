package kr.or.ddit.member.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@ContextHierarchy({
	@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*-context.xml") //다오와 서비스
	,@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml") //하위컨테이너
})
@WebAppConfiguration //web컨테이너
public class MemberListControllerTest {

	@Inject
	private WebApplicationContext context;
	
	private MockMvc mockMvc; //웹용 테스트 객체
	
	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testMemberList() throws Exception {
		mockMvc.perform(get("/member/memberList.do").param("page", "2"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pagingVO"))
				.andExpect(view().name("member/memberList"))
				.andDo(log());
	}

}
