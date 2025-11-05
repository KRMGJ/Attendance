<%@ page contentType="text/html; charset=UTF-8" %>
<%
  request.setAttribute("pageTitle", "직원 등록");
  request.setAttribute("content", "/WEB-INF/jsp/attendance/employee/join_form.jsp");
%>
<jsp:include page="/WEB-INF/jsp/layout/layout.jsp"/>
