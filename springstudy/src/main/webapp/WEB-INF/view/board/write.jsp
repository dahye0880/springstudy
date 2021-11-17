<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/WEB-INF/view/board/write.jsp 에 만들어진거임
	http://localhost:8090/springstudy/board/write
 --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>
<%--enctype="multipart/form-data" 파라미터값과 내용을 전송, 무조건 post방식
request객체를 쓸수없다. 
multipart/form-data=>모든 문자를 인코딩하지 않음을 명시함. 이 방식은 <form> 요소가 파일이나 이미지를 서버로 전송할 때 주로 사용함.
 --%>
<form:form modelAttribute="board" action="write" enctype="multipart/form-data" name="f">
	<table>
	<tr><td>글쓴이</td><td><form:input path="name" />
	<font color="red"><form:errors path="name" /></font></td></tr>
	<tr><td>비밀번호</td><td><form:password path="pass" />
	<font color="red"><form:errors path="pass" /></font></td></tr>
	<tr><td>제목</td><td><form:input path="subject" />
	<font color="red"><form:errors path="subject" /></font></td></tr>
	<tr><td>내용</td><td><form:textarea path="content" rows="15" cols="80" />
	<font color="red"><form:errors path="content" /></font></td></tr>
	<tr><td>첨부파일</td><td><input type="file" name="file1"></td></tr>
	<tr><td colspan="2"><a href="javascript:document.f.submit()">[게시글등록]</a>
	<a href="list">[게시글목록]</a></td></tr>
	</table>
</form:form>
</body>
</html>