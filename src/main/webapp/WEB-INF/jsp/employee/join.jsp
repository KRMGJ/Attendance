<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
  request.setAttribute("pageTitle", "직원 등록");
  request.setAttribute("content", "/WEB-INF/jsp/employee/_join_form.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>
