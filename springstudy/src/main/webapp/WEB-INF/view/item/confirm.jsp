<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/item/confirm.jsp 
    상품삭제 버튼 클릭시 해당 상품을 삭제하기
--%>    
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>  
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>상품 삭제 전 확인</title></head>
<body><h2>상품 삭제 전 확인</h2>
<table><tr><td><img src="../img/${item.pictureUrl}"></td>
   <td><table><tr><td>상품명</td><td>${item.name}</td></tr>
         <tr><td>가격</td><td>${item.price}원</td></tr>
         <tr><td>상품설명</td><td>${item.description}</td></tr>
         <tr><td colspan="2">
          <input type="button" value="상품삭제"
           onclick="location.href='delete?id=${item.id}'">
       <input type="button" value="상품목록"
           onclick="location.href='list'"></td></tr>
   </table></table></body></html>