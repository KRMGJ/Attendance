<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<form class="flex items-end gap-3 mb-4" method="get">
		<div>
			<label class="block text-sm mb-1">연도-월</label> <input type="month"
				name="ym" value="${param.ym}" class="rounded border px-3 py-2" />
		</div>
		<button class="rounded-md bg-gray-900 text-white px-4 py-2">조회</button>
	</form>
	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">부서</th>
					<th class="px-4 py-2 text-left">직원수</th>
					<th class="px-4 py-2 text-left">총 근무시간</th>
					<th class="px-4 py-2 text-left">총 연장시간</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty report}">
						<c:forEach var="r" items="${report}">
							<tr class="border-t">
								<td class="px-4 py-2"><c:out value="${r.department}" /></td>
								<td class="px-4 py-2"><c:out value="${r.headcount}" /></td>
								<td class="px-4 py-2"><c:out value="${r.totalHours}" /></td>
								<td class="px-4 py-2"><c:out value="${r.totalOvertime}" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="px-4 py-2 text-center" colspan="4">보고서 데이터가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>
