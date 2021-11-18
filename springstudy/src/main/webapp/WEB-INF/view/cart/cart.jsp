<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/cart/cart.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>    
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>장바구니</title></head>
<body><h2>장바구니</h2>
<table><tr><td colspan="4">장바구니 상품 목록</td></tr>
  <tr><th>상품명</th><th>가격</th><th>수량</th><th>합계</th></tr>
  <%-- tot 변수를 0으로 초기화하기 
  		${tot +(itemSet.quantity * itemSet.item.price)}
  		
  		varStatus="stat" => forEach가 반복문이고 이건 반복문의 반복상태정보(index,count)를 가진 객체
  		${stat.index} : 0,1,2,3
  		${stat.count} : 1,2,3,4
  --%>
  <c:set var="tot" value="${0}"/>
<c:forEach items="${cart.itemSetList}" var="itemSet" varStatus="stat"><!--cart=>itemSet객체 // itemSetList => ArrayLst 객체이다. -->
<%-- ${itemSet.item.name}
		=>	itemSet.getItem().getName()
		${itemSet.quantity * itemSet.item.price} => 가격부분
 --%>
   <tr><td>${itemSet.item.name}</td><td>${itemSet.item.price}</td>
       <td>${itemSet.quantity}</td>
       <td>${itemSet.quantity * itemSet.item.price}
  <c:set var="tot" 
        value="${tot +(itemSet.quantity * itemSet.item.price)}"/>
      <a href="cartDelete?index=${stat.index}">ⓧ</a></td></tr></c:forEach>
 <tr><td colspan="4" align="right">
   총 구입 금액 : ${tot}원</td></tr>
</table><br>${message }<br><a href="../item/list">상품목록</a>
<a href="checkout">주문하기</a></body></html>