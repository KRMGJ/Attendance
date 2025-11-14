<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">내 근태</h2>
	</div>
	<div class="flex items-end gap-3 mb-4">
	    <form method="get" action="<c:url value='/attendance/my.do'/>" class="flex gap-3 items-end">
	        <div>
	            <label class="block text-sm mb-1">시작일</label>
	            <input type="date" name="from" value="<fmt:formatDate value='${from}' pattern='yyyy-MM-dd'/>" class="rounded border px-3 py-2" />
	        </div>
	        <div>
	            <label class="block text-sm mb-1">종료일</label>
	            <input type="date" name="to" value="<fmt:formatDate value='${to}' pattern='yyyy-MM-dd'/>" class="rounded border px-3 py-2" />
	        </div>
	        <button class="rounded-md bg-gray-900 text-white px-4 py-2">조회</button>
	    </form>
	</div>
	<table class="min-w-full text-sm">
		<thead class="bg-gray-50">
			<tr>
				<th class="px-4 py-2 text-left">근무일</th>
				<th class="px-4 py-2 text-left">출근</th>
				<th class="px-4 py-2 text-left">퇴근</th>
				<th class="px-4 py-2 text-left">상태</th>
				<th class="px-4 py-2 text-left">연장(분)</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty myAttendance}">
					<c:forEach var="row" items="${myAttendance}">
						<tr class="border-t">
							<td class="px-4 py-2"><c:out value="${row.workDate}" /></td>
							<td class="px-4 py-2"><c:out value="${empty row.checkIn ? '-' : row.checkIn}" /></td>
							<td class="px-4 py-2"><c:out value="${empty row.checkOut ? '-' : row.checkOut}" /></td>
							<td class="px-4 py-2"><c:out value="${row.status}" /></td>
							<td class="px-4 py-2"><c:out value="${row.overtimeMinutes}" /></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="px-4 py-2 text-center" colspan="5">근태 기록이 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>
