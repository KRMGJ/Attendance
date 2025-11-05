<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">휴가 신청 목록</h2>
		<a href="<c:url value='/leave/new.do'/>" class="px-3 py-2 rounded-md bg-gray-900 text-white">신청</a>
	</div>
	<table class="min-w-full text-sm">
		<thead class="bg-gray-50">
			<tr>
				<th class="px-4 py-2 text-left">기간</th>
				<th class="px-4 py-2 text-left">구분</th>
				<th class="px-4 py-2 text-left">상태</th>
				<th class="px-4 py-2 text-left">승인자</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="r" items="${requests}">
				<tr class="border-t">
					<td class="px-4 py-2"><c:out value="${r.startDate}" /> ~ <c:out value="${r.endDate}" /></td>
					<td class="px-4 py-2"><c:out value="${r.type}" /></td>
					<td class="px-4 py-2"><c:out value="${r.status}" /></td>
					<td class="px-4 py-2"><c:out value="${r.approver}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
