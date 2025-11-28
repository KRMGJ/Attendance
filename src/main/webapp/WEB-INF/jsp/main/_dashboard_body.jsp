<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ui2" uri="http://egovframework.let/attendance/ui" %>

<div class="space-y-6">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-3">
			안녕하세요, <c:out value="${empName}" /> 님
		</h2>
		<p class="text-gray-600">
			오늘은 <strong><%=java.time.LocalDate.now()%></strong> 입니다.
		</p>
	</div>

	<div class="grid md:grid-cols-3 gap-6">
		<div class="bg-white border rounded-xl p-6 shadow-sm">
			<h3 class="font-semibold mb-2">오늘 근태</h3>
			<p class="text-sm mb-3 text-gray-600">
				출근:
				<c:choose>
					<c:when test="${not empty today.checkIn}">
						<c:out value="${today.checkIn}" />
					</c:when>
					<c:otherwise>-</c:otherwise>
				</c:choose>
				/ 퇴근:
				<c:choose>
					<c:when test="${not empty today.checkOut}">
						<c:out value="${today.checkOut}" />
					</c:when>
					<c:otherwise>-</c:otherwise>
				</c:choose>
			</p>
			<div class="flex gap-2">
				<form method="post" action="<c:url value='/attendance/checkin.do'/>">
					<button class="px-4 py-2 rounded-md bg-emerald-600 text-white">출근</button>
				</form>
				<form method="post"
					action="<c:url value='/attendance/checkout.do'/>">
					<button class="px-4 py-2 rounded-md bg-rose-600 text-white">퇴근</button>
				</form>
			</div>
		</div>

		<div class="bg-white border rounded-xl p-6 shadow-sm">
			<h3 class="font-semibold mb-2">휴가 잔여</h3>
			<p class="text-gray-600 text-sm mb-3">
				연차
				<c:out value="${leave.remaining}" />
				일 남음
			</p>
			<a href="<c:url value='/leave/requests.do'/>"
				class="px-4 py-2 rounded-md bg-gray-900 text-white">휴가 내역 보기</a>
		</div>
		
		<div class="bg-white border rounded-xl p-6 shadow-sm">
        	<h3 class="font-semibold mb-2">근무시간 요약</h3>
        	<p class="text-gray-700 text-sm mb-1">이번 달:
            	<strong>
            		<fmt:formatNumber value="${monthlyMinutes div 60}" maxFractionDigits="0" />시간
           	    	<fmt:formatNumber value="${monthlyMinutes mod 60}" pattern="00" />분
                </strong>
        	</p>

        	<p class="text-gray-700 text-sm mb-1">이번 주: 
        		<strong> 
        			<fmt:formatNumber value="${weeklyMinutes div 60}" maxFractionDigits="0" />시간 
        			<fmt:formatNumber value="${weeklyMinutes mod 60}" pattern="00" />분
				</strong>
			</p>

	        <p class="text-gray-700 text-sm">오늘:
	            <strong>
	                <c:choose>
	                    <c:when test="${not empty today.workedMinutes}">
							<fmt:formatNumber value="${today.workedMinutes div 60}" maxFractionDigits="0" />시간
                            <fmt:formatNumber value="${today.workedMinutes mod 60}" pattern="00" />분
	                    </c:when>
	                    <c:otherwise>0시간 0분</c:otherwise>
	                </c:choose>
	            </strong>
	        </p>
	    </div>
	</div>

	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h3 class="font-semibold mb-3">최근 근태 기록</h3>
		<div class="overflow-x-auto">
			<table class="min-w-full text-sm">
				<thead class="bg-gray-50">
					<tr>
						<th class="px-4 py-2 text-left">날짜</th>
						<th class="px-4 py-2 text-left">출근</th>
						<th class="px-4 py-2 text-left">퇴근</th>
						<th class="px-4 py-2 text-left">상태</th>
					</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${not empty recentAttendance}">
						<c:forEach var="a" items="${recentAttendance}">
							<tr class="border-t">
								<td class="px-4 py-2"><c:out value="${empty a.workDate ? '-' : a.workDate}" /></td>
								<td class="px-4 py-2"><c:out value="${empty a.checkIn ? '-' : a.checkIn}" /></td>
								<td class="px-4 py-2"><c:out value="${empty a.checkOut ? '-' : a.checkOut}" /></td>
								<td class="px-4 py-2">
									<span class="${ui2:workStatusColor(a.status)} font-medium">
										${ui2:workStatusIcon(a.status)}
										${ui2:label(a.status)}
									</span>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="border-t">
							<td class="px-4 py-2 text-center" colspan="4">최근 근태 기록이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</div>
<c:if test="${not empty attendanceResult}">
	<script>
		const messages = {
			checkin_success: "출근이 정상적으로 처리되었습니다.",
			checkin_fail: "출근 처리에 실패했습니다. 다시 시도해주세요.",
			already_check_in: "이미 출근 처리가 되어 있습니다.",
			checkout_success: "퇴근이 정상적으로 처리되었습니다.",
			checkout_fail: "퇴근 처리에 실패했습니다. 다시 시도해주세요.",
			already_check_out: "이미 퇴근 처리가 되어 있습니다."
		};
		const code = "${attendanceResult}";
		if (code) alert(messages[code] || "처리 결과를 알 수 없습니다.");
	</script>
</c:if>
