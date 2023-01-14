<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/includee/preScript.jsp" />
<c:if test="${not empty message }">
	<script type="text/javascript">
		alert("${message}");
	</script>
</c:if>
</head>
<body>
<form:form modelAttribute="prod" enctype="multipart/form-data">
	<table class="table table-bordered">
		<tr>
			<th>상품아이디</th>
			<td>
				<form:input path="prodId" Class="form-control" readonly="true"/>
				<form:errors path="prodId" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>상품명</th>
			<td>
				<form:input path="prodName" Class="form-control"/>
				<form:errors path="prodName" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>상품분류</th>
			<td>
				<form:select path="prodLgu">
					<option value>분류</option>
					<c:forEach items="${lprodList }" var="lprod">
						<form:option value="${lprod.lprodGu }" label="${lprod.lprodNm }"/>
					</c:forEach>
				</form:select>
				<form:errors path="prodLgu" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>거래처</th>
			<td>
<%-- 			<form:select path="prodBuyer" items="${buyerList }" item itemValue="buyerId" itemLabel="buyerName"></form:select> --%>
				<form:select path="prodBuyer">
					<option value>거래처</option>
					<c:forEach items="${buyerList }" var="buyer">
						<form:option value="${buyer.buyerId }" cssClass="${buyer.buyerLgu }" label="${buyer.buyerName }"/>
					</c:forEach>
				</form:select>
				<form:errors path="prodBuyer" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>구매가</th>
			<td>
				<form:input path="prodCost" type="number" Class="form-control" />
				<form:errors path="prodCost" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>판매가</th>
			<td>
				<form:input path="prodPrice" type="number" Class="form-control" />
				<form:errors path="prodPrice" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>세일가</th>
			<td>
				<form:input path="prodSale" type="number" Class="form-control" />
				<form:errors path="prodSale" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>상품요약</th>
			<td>
				<form:input path="prodOutline" Class="form-control" />
				<form:errors path="prodOutline" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>상품상세</th>
			<td>
				<form:input path="prodDetail" Class="form-control" />
				<form:errors path="prodDetail" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>상품이미지</th>
			<td>
				<input type="file" name="prodImage" accept="image/*"/>
				<form:errors path="prodImage" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>재고</th>
			<td>
				<form:input path="prodTotalstock" type="number" Class="form-control" />
				<form:errors path="prodTotalstock" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>입고일</th>
			<td>
				<form:input path="prodInsdate" type="date" Class="form-control" />
				<form:errors path="prodInsdate" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>적정재고</th>
			<td>
				<form:input path="prodProperstock" type="number" Class="form-control" />
				<form:errors path="prodProperstock" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>크기</th>
			<td>
				<form:input path="prodSize" Class="form-control" />
				<form:errors path="prodSize" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>색상</th>
			<td>
				<form:input path="prodColor" Class="form-control" />
				<form:errors path="prodColor" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>배송방법</th>
			<td>
				<form:input path="prodDelivery" Class="form-control" />
				<form:errors path="prodDelivery" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>단위</th>
			<td>
				<form:input path="prodUnit" Class="form-control" />
				<form:errors path="prodUnit" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>입고량</th>
			<td>
				<form:input path="prodQtyin" type="number" Class="form-control" />
				<form:errors path="prodQtyin" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>출고량</th>
			<td>
				<form:input path="prodQtysale" type="number" Class="form-control" />
				<form:errors path="prodQtysale" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<th>마일리지</th>
			<td>
				<form:input path="prodMileage" type="number" Class="form-control" />
				<form:errors path="prodMileage" element="span" cssClass="text-danger"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="저장" />
				<input type="reset" value="취소" />
			</td>
		</tr>
	</table>
</form:form>	
<script>
let prodBuyerTag = $("[name=prodBuyer]");
$("[name=prodLgu]").on("change", function(){
	let prodLgu = $(this).val();
	if(prodLgu){
		prodBuyerTag.find("option:gt(0)").hide();
		prodBuyerTag.find("option."+prodLgu).show();
	}
}).trigger("change");
</script>
<jsp:include page="/includee/postScript.jsp" />

</body>
</html>














