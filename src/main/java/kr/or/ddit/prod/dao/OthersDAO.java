package kr.or.ddit.prod.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BuyerVO;

/**
 * 상품 분류 선택 UI와 거래처 선택 UI 완성을 위한 데이터 조회.
 *
 */
@Mapper
public interface OthersDAO {
	public List<Map<String, Object>> selectLprodList();
	
	public List<BuyerVO> selectBuyerList(@Param("buyerLgu") String buyerLgu);
}
