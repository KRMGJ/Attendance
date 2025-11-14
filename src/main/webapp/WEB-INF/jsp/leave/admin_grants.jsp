<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "휴가 관리 - 휴가 부여 관리");
request.setAttribute("content", "/WEB-INF/jsp/leave/_admin_grants_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>