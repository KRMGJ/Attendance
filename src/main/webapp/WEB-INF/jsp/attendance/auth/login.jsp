<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "로그인");
request.setAttribute("content", "/WEB-INF/jsp/attendance/auth/_login_form.jsp");
%>
<jsp:include page="/WEB-INF/jsp/attendance/layout/layout.jsp" />
