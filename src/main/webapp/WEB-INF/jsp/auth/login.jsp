<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "로그인");
request.setAttribute("content", "/WEB-INF/jsp/auth/_login_form.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
