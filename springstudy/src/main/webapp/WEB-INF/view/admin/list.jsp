<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/admin/list.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>    
<!DOCTYPE html><html><head>
<meta charset="UTF-8">
<title>회원목록</title><!-- .prop => property-->
<script type="text/javascript">
   function allchkbox(allchk) {
	   $(".idchks").prop("checked",allchk.checked)
   }
</script></head><body>
<form action="mailForm" method="post">
  <table><tr><td colspan="7">회원목록</td></tr>
  <tr><th>아이디</th><th>이름</th><th>전화</th><th>생일</th>
  <th>이메일</th><th>&nbsp;</th><th><input type="checkbox" name="allchk" 
       onchange="allchkbox(this)"></th></tr><!-- onchange = 체크박스 바꼈다 안바꼈다 -->
<c:forEach items="${list}" var="user">
<tr><td>${user.userid}</td><td>${user.username}</td><td>${user.phoneno}</td>
   <td><fmt:formatDate value="${user.birthday}"  pattern="yyyy-MM-dd"/></td>
   <td>${user.email}</td>
   <td><a href="../user/update?id=${user.userid}">수정</a>
       <a href="../user/delete?id=${user.userid}">강제탈퇴</a>
       <a href="../user/mypage?id=${user.userid}">회원정보</a>
   </td><td><input type="checkbox" name="idchks" class="idchks"
      value="${user.userid}"></td></tr>
</c:forEach>
  <tr><td colspan="7"><input type="submit" value="메일보내기"></td></tr>
  </table></form></body></html>
