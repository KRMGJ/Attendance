<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%
request.setAttribute("pageTitle", "휴가 신청 목록");
request.setAttribute("content", "/WEB-INF/jsp/leave/_request_list_body.jsp");
%>
<%@ include file="/WEB-INF/jsp/layout/layout.jsp" %>