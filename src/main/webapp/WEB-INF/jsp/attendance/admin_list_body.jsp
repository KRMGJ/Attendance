<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<form class="grid md:grid-cols-5 gap-3 mb-4" method="get">
		<input name="q" value="${param.q}"
			class="rounded border px-3 py-2 md:col-span-2" placeholder="이름/이메일" />
		<input type="date" name="from" value="${param.from}"
			class="rounded border px-3 py-2" /> <input type="date" name="to"
			value="${param.to}" class="rounded border px-3 py-2" />
		<button class="rounded-md bg-gray-900 text-white px-4">검색</button>
	</form>
	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">직원</th>
					<th class="px-4 py-2 text-left">근무일</th>
					<th class="px-4 py-2 text-left">출근</th>
					<th class="px-4 py-2 text-left">퇴근</th>
					<th class="px-4 py-2 text-left">상태</th>
					<th class="px-4 py-2 text-left">연장(분)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="row" items="${list}">
					<tr class="border-t">
						<td class="px-4 py-2"><c:out value="${row.employee.name}" /></td>
						<td class="px-4 py-2"><c:out value="${row.workDate}" /></td>
						<td class="px-4 py-2"><c:out value="${row.checkIn}" /></td>
						<td class="px-4 py-2"><c:out value="${row.checkOut}" /></td>
						<td class="px-4 py-2"><c:out value="${row.status}" /></td>
						<td class="px-4 py-2"><c:out value="${row.overtimeMinutes}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
