<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "내 근태");
request.setAttribute("content", "/WEB-INF/jsp/attendance/_my_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>