<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "직원 상세");
request.setAttribute("content", "/WEB-INF/jsp/employee/_detail_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
