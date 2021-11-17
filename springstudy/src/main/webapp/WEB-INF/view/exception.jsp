<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%--page 디렉티브의 isErrorPage 옵션의 값에 true 를 주어 현재 페이지가 예외 처리 페이지라는 것을 명시합니다. 
이때부터는 exception 내장 객체를 이용할 수 있으며, 예외가 발생한 페이지로부터 제어가 넘어오면 해당 페이지에서 발생한 예외 내용을 exception 객체에 담아 사용할 수 있습니다.
exception객체는 오류페이지에서만 사용가능 : exception,BoardException이 할당됨. 
 --%>
<script>
	alert("${exception.message}")
	location.href="${exception.url}"
</script>