<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/user/login.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %> 
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>로그인화면</title>
<script type="text/javascript">
function win_open(page) {
	   var op = "width=500, height=350, left=50,top=150";
	   open(page,"",op);
}
</script>
</head><body>
<h2>사용자 로그인</h2>
<form:form modelAttribute="user" method="post" action="login" name="loginform">
<input type="hidden" name="username" value="유효성검증을위한 파라미터" >
<input type="hidden" name="email" value="valid@aaa.bbb" >
  <spring:hasBindErrors name="user">
    <font color="red"><c:forEach items="${errors.globalErrors}" var="error">
         <spring:message code="${error.code}" />
      </c:forEach></font></spring:hasBindErrors>
  <table border="1" style="border-collapse: collapse;">
   <tr height="40px"><td>아이디</td><td><form:input path="userid" />
     <font color="red"><form:errors path="userid" /></font></td></tr>
<tr height="40px"><td>비밀번호</td><td><form:password path="password" />
<font color="red"><form:errors path="password" /></font></td></tr>
<tr height="40px"><td colspan="2" align="center">
<input type="submit" value="로그인">
<input type="button" value="회원가입"  onclick="location.href='userEntry'">
<input type="button" value="아이디찾기" onclick="win_open('idsearch')">
<input type="button" value="비밀번호찾기" onclick="win_open('pwsearch')">
   </td></tr></table>
</form:form></body></html>