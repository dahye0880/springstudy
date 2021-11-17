<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/layout/layout.jsp --%>
<%-- sitemesh 의 태그이다 --%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html><head><meta charset="UTF-8">
<%-- <decorator:title /> : 원래 작성된 HTML 중 <title>태그의 내용을 집어넣어라. --%>
<title><decorator:title /></title>
<%-- 아래는 jquery를 쓰기 위해 작성한것 --%>
<%-- CDM 방식으로 Jquery의 자바스크립트 파일 로드 .min은 Jquery파일을 좀 최소화해서 다운받는것--%>
<script type="text/javascript" src=
"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<%-- smartedit라고 하는 것(아래) --%>
<script type="text/javascript" 
   src="http://cdn.ckeditor.com/4.5.7/standard/ckeditor.js"></script>
   <%-- <decorator:head /> : 원래 작성된 HTML 중 <head>태그의 내용을 가지고 와.
                                            <title>태그제외하고 다른 태그들의 내용만 가지고옴. --%>
<decorator:head />
<link rel="stylesheet" href="${path}/css/main.css"><%-- ${path} => springstudy/ --%>
</head><body>
<table><tr><td colspan="3" style="text-align:right">
 <c:if test="${empty sessionScope.loginUser}">
   <a href="${path}/user/login">로그인</a>
   <a href="${path}/user/userEntry">회원가입</a></c:if>
 <c:if test="${!empty sessionScope.loginUser}">
   ${sessionScope.loginUser.username }님
   <a href="${path}/user/logout">로그아웃</a></c:if>
</td></tr>
<tr><td width="15%" valign="top">
  <a href="${path}/board/list">게시판</a><br>
  <a href="${path}/user/main">회원관리</a><br>
  <a href="${path}/item/list">상품관리</a><br>
</td><td colspan="2" style="text-align: left; vertical-align: top">
<%-- <decorator:body /> : 원본페이지의 body 태그 부분을 가져오는것 --%>
<decorator:body /></td></tr>
<tr><td colspan="3">인공지능을 활용한 웹플랫폼구축 </td></tr>
</table></body></html>