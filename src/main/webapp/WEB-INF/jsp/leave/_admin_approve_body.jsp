<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">휴가 승인</h2>
	<table class="min-w-full text-sm">
		<thead class="bg-gray-50">
			<tr>
				<th class="px-4 py-2 text-left">직원</th>
				<th class="px-4 py-2 text-left">기간</th>
				<th class="px-4 py-2 text-left">구분</th>
				<th class="px-4 py-2 text-left">상태</th>
				<th class="px-4 py-2"></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty pending}">
					<c:forEach var="r" items="${pending}">
						<tr class="border-t">
							<td class="px-4 py-2"><c:out value="${r.employee.name}" /></td>
							<td class="px-4 py-2"><c:out value="${r.startDate}" /> ~ <c:out value="${r.endDate}" /></td>
							<td class="px-4 py-2"><c:out value="${r.type}" /></td>
							<td class="px-4 py-2"><c:out value="${r.status}" /></td>
							<td class="px-4 py-2 text-right">
								<form method="post"
									action="<c:url value='/admin/leave/approve.do'/>" class="inline">
									<input type="hidden" name="id" value="${r.id}" />
									<button class="px-3 py-1.5 rounded-md bg-emerald-600 text-white">승인</button>
								</form>
								<form method="post"
									action="<c:url value='/admin/leave/reject.do'/>" class="inline">
									<input type="hidden" name="id" value="${r.id}" />
									<button class="px-3 py-1.5 rounded-md bg-rose-600 text-white">반려</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="px-4 py-2 text-center" colspan="5">승인 대기 중인 휴가 신청이
							없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>
