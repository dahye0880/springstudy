<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/board/list.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록 보기</title>
</head>
<!-- 속성으로 들어오기 때문에 el 로 사용한것이다. -->
<body>
<table>
	<c:if test="${listcount > 0}"> <!-- 등록된 게시물이 존재하는 경우 -->
		<tr><td colspan="4">Spring 게시판</td><td>글개수:${listcount}</td></tr>
		<tr><th>번호</th><th>제목</th><th>글쓴이</th><th>날짜</th><th>조회수</th></tr>
	<c:forEach var="board" items="${boardlist}">
		<tr><td>${boardno}</td>
			<c:set var="boardno" value="${boardno - 1}" />
			<td style="text-aligh: left;">
			<c:if test="${! empty board.fileurl}"><a href="file/${board.fileurl}">@</a></c:if>
			<c:if test="${empty board.grplevel}">&nbsp;&nbsp;&nbsp;</c:if>
		<c:forEach begin="1" end="${board.grplevel}">&nbsp;&nbsp;</c:forEach>
			<c:if test="${board.grplevel > 0}">└</c:if>
		<a href="detail?num=${board.num}">${board.subject}</a></td>
			<td>${board.name}</td>
			<td><fmt:formatDate value="${board.regdate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td>${board.readcnt}</td></tr>
	</c:forEach>
	<tr><td colspan="5">
		<c:if test="${pageNum > 1}">
			<a href="list?pageNum=${pageNum - 1}">[이전]</a></c:if>
		<c:forEach var="a" begin="${startpage}" end="${endpage}">
			<c:if test="${a == pageNum}">[${a}]</c:if>
			<c:if test="${a != pageNum}">
			<a href="list?pageNum=${a}">[${a}]</a></c:if>
		</c:forEach>
		<c:if test="${pageNum < maxpage}">
			<a href="list?pageNum=${pageNum + 1}">[다음]</a></c:if>
		<c:if test="${pageNum >= maxpage}">[다음]</c:if></td></tr>
	</c:if>
		<c:if test="${listcount == 0}"><!-- 등록된 게시물이 없는 경우 -->
			<tr><td colspan="5">등록된 게시물이 없습니다.</td></tr></c:if>
<%--등록된 게시물이 있든 없든 화면에 표시되는 내용 --%>
		<tr><td colspan="5" align="right">
		<a href="write">[글쓰기]</a></td></tr>
</table>
</body>
</html>