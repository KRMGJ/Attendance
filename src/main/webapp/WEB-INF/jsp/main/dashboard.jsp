<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "대시보드");
request.setAttribute("content", "/WEB-INF/jsp/main/_dashboard_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
