<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "권한 계층 관리");
request.setAttribute("content", "/WEB-INF/jsp/security/_role_hierarchy_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
