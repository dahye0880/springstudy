<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 수정 화면</title>
<script type="text/javascript">
function file_delete(){
	document.updateform.file2.value="";
	document.getElementById("file_desc").style.display="none";
}
</script>
<style type="text/css"> .errortext {color : red; }</style>
</head>
<body>
<%--modelAttribute 이거 공부해보자 --%>
<form:form modelAttribute="board" action="update" enctype="multipart/form-data"
           name="updateform"> <!-- 매서드방식 무조건 post방식 -->
<input type="hidden" name="file2" value="${board.fileurl}" >
<form:hidden path="num" /> <!-- hidden으로 감춰놓은것 -->
<form:hidden path="name" /> <!-- hidden으로 감춰놓은것 -->
<table>
<tr><td colspan="2">Spring 게시판</td></tr>
<tr><td>제목</td><td><form:input path="subject"/><form:errors path="subject" class="errortext"/>
	</td></tr>
<tr><td>내용</td><td><form:textarea path="content" cols="67" rows="15"/>
		<form:errors path="content" class="errortext"/></td></tr>
<tr><td>첨부파일</td><td>&nbsp;
	<c:if test="${!empty board.fileurl}">
		<div id="file_desc"><a href="file/${board.fileurl}">${board.fileurl}</a>&nbsp;
			<a href="javascript:file_delete()">[첨부파일삭제]</a>
		</div></c:if>
		<input type="file" name="file1" id="file1">
	</td></tr><tr><td>비밀번호</td>
		<td><form:password path="pass" /><form:errors path="pass" class="errortext"/></td></tr>
		<tr><td colspan="2" align="center">
		<a href="javascript:document.updateform.submit()">[수정]</a>&nbsp;
		<a href="list?pageNum=${param.pageNum}">[목록]</a>
		</td></tr></table></form:form>
</body>
</html>