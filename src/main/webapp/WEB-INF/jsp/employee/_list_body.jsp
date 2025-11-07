<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
request.setAttribute("pageTitle", "직원 목록");
%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">직원 목록</h2>
		<a href="<c:url value='/employee/join.do'/>"
			class="px-3 py-2 rounded-md bg-gray-900 text-white">직원 등록</a>
	</div>
	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">이름</th>
					<th class="px-4 py-2 text-left">이메일</th>
					<th class="px-4 py-2 text-left">직급</th>
					<th class="px-4 py-2 text-left">상태</th>
					<th class="px-4 py-2"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${employees}">
					<tr class="border-t">
						<td class="px-4 py-2"><c:out value="${e.name}" /></td>
						<td class="px-4 py-2"><c:out value="${e.email}" /></td>
						<td class="px-4 py-2"><c:out value="${e.position}" /></td>
						<td class="px-4 py-2"><c:out value="${e.status}" /></td>
						<td class="px-4 py-2 text-right"><a class="underline"
							href="<c:url value='/employee/detail.do?id=${e.id}'/>">보기</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
