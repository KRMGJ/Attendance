<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "월간 보고서");
request.setAttribute("content", "/WEB-INF/jsp/report/_monthly_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
