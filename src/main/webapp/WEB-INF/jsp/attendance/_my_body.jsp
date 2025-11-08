<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">내 근태</h2>
		<a class="px-3 py-2 rounded-md border"
			href="<c:url value='/attendance/checkin.do'/>">출·퇴근</a>
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
