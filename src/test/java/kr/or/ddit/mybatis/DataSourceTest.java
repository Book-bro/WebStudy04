package kr.or.ddit.mybatis;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

//테스트목적의 가상 컨테이너가 필요
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/datasource-context.xml")
@WebAppConfiguration
public class DataSourceTest {
	
	@Inject
	private DataSource dataSource;
	
	@Test
	public void test() {
		log.info("주입된 DataSource : {}", dataSource);
	}

}
