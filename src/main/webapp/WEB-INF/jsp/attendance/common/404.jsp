<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8"%>
<%
request.setAttribute("pageTitle", "접근 거부");
request.setAttribute("content", "/WEB-INF/jsp/attendance/common/_404_body.jsp");
%>
<jsp:include page="/WEB-INF/jsp/attendance/layout/layout.jsp" />
