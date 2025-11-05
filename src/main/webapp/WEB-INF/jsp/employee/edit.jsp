<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "직원 정보 수정");
request.setAttribute("content", "/WEB-INF/jsp/employee/_edit_form.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
