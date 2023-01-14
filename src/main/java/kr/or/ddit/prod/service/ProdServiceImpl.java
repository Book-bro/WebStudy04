package kr.or.ddit.prod.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.UserNotFoundException;
import kr.or.ddit.prod.dao.ProdDAO;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ProdVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProdServiceImpl implements ProdService{
	//의존관계 형성, 결합력 증가
	private final ProdDAO prodDAO;
	
	private final WebApplicationContext context; //웹컨테이너
	private File saveFolder;
	
	@PostConstruct //라이프사이클 콜백선언
	public void init() throws IOException {
		String saveFolderURL = "/resources/prodImages";
//		ServletContext application = req.getServletContext(); //어플리케이션 기본객체
//		String saveFolderPath = application.getRealPath(saveFolderURL);
		Resource saveFolderRes = context.getResource(saveFolderURL); //따로 물리경로를 설정 안해도됌
		saveFolder = saveFolderRes.getFile();
		if(!saveFolder.exists()) {
			saveFolder.mkdirs(); 
		}
	}
	
	private void processProdImage(ProdVO prod) {
		try {
			if(1==1) throw new RuntimeException("트랜잭션 관리 여부 확인을 위한 강제 예외발생");
			prod.saveTo(saveFolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ProdVO retrieveProd(String prodId) {
		ProdVO prod = prodDAO.selectProd(prodId);
		
		if(prod==null)
			throw new RuntimeException(String.format("%s는 없는 상품", prodId));
		return prod;
	}


	@Override
	public void retrieveProdList(PagingVO<ProdVO> pagingVO) {
		int totalRecord = prodDAO.selectTotalRecord(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
		List<ProdVO> dataList = prodDAO.selectProdList(pagingVO);
		pagingVO.setDataList(dataList);
	}

	@Inject
	private SqlSessionFactory SqlSessionFactory;

	@Override
	public ServiceResult createProd(ProdVO prod) {
		//세션 오픈
		try(
			SqlSession sqlSession = SqlSessionFactory.openSession(); // 트랜젝션 시작
		){	//예외 발생시 커밋안됌, -> 롤백됨
			int rowcnt = prodDAO.insertProd(prod, sqlSession);
			
			processProdImage(prod);
			
			sqlSession.commit();
			
			return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
		}
	}


	@Override
	public ServiceResult modifyProd(ProdVO prod) {
		retrieveProd(prod.getProdId());
		
		int rowcnt = prodDAO.updateProd(prod);
		processProdImage(prod);
		
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
	}

}
