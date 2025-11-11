<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "권한 별 URL 관리");
request.setAttribute("content", "/WEB-INF/jsp/security/_role_url_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
