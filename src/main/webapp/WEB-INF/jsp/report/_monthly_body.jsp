<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="bg-white border rounded-xl p-6 shadow-sm">

	<form class="flex items-end gap-3 mb-4" method="get" action="">
		<div>
			<label class="block text-sm mb-1">연도-월</label> 
			<input type="month" name="ym" value="${empty param.ym ? ym : param.ym}" class="rounded border px-3 py-2" />
		</div>
		<button class="rounded-md bg-gray-900 text-white px-4 py-2">조회</button>
	</form>

	<c:if test="${not empty report}">
		<c:set var="sumHead" value="${0}" />
		<c:set var="sumWorkDays" value="${0}" />
		<c:set var="sumMinutes" value="${0}" />
		<c:set var="sumOver" value="${0}" />
		<c:set var="sumNight" value="${0}" />
		<c:set var="sumWeekend" value="${0}" />
		<c:set var="sumLate" value="${0}" />
		<c:set var="sumEarly" value="${0}" />
		<c:set var="sumAbsent" value="${0}" />
		<c:set var="sumLeave" value="${0}" />
		<c:set var="sumSick" value="${0}" />

		<c:forEach var="r" items="${report}">
			<c:set var="sumHead" value="${sumHead + r.headcount}" />
			<c:set var="sumWorkDays" value="${sumWorkDays + r.workDays}" />
			<c:set var="sumMinutes" value="${sumMinutes + r.totalMinutes}" />
			<c:set var="sumOver" value="${sumOver + r.totalOvertime}" />
			<c:set var="sumNight" value="${sumNight + r.nightOvertime}" />
			<c:set var="sumWeekend" value="${sumWeekend + r.weekendOvertime}" />
			<c:set var="sumLate" value="${sumLate + r.lateCount}" />
			<c:set var="sumEarly" value="${sumEarly + r.earlyLeaveCount}" />
			<c:set var="sumAbsent" value="${sumAbsent + r.absentCount}" />
			<c:set var="sumLeave" value="${sumLeave + r.leaveDays}" />
			<c:set var="sumSick" value="${sumSick + r.sickLeaveDays}" />
		</c:forEach>

		<c:set var="sumH" value="${(sumMinutes - (sumMinutes mod 60)) / 60}" />
		<c:set var="sumM" value="${sumMinutes mod 60}" />
		<c:set var="sumOH" value="${(sumOver - (sumOver mod 60)) / 60}" />
		<c:set var="sumOM" value="${sumOver mod 60}" />

		<div class="grid grid-cols-2 md:grid-cols-4 gap-3 mb-4">
			<div class="p-3 rounded-lg border">
				<div class="text-xs text-gray-500">총 인원</div>
				<div class="text-lg font-semibold">
					<fmt:formatNumber value="${sumHead}" />
				</div>
			</div>
			<div class="p-3 rounded-lg border">
				<div class="text-xs text-gray-500">총 근무시간</div>
				<div class="text-lg font-semibold">
					<fmt:formatNumber value="${sumMinutes div 60}" maxFractionDigits="0" />:
					<fmt:formatNumber value="${sumMinutes mod 60}" pattern="00" />
				</div>
			</div>
			<div class="p-3 rounded-lg border">
				<div class="text-xs text-gray-500">총 연장시간</div>
				<div class="text-lg font-semibold">
					<fmt:formatNumber value="${sumOver div 60}" maxFractionDigits="0" />:
					<fmt:formatNumber value="${sumOver mod 60}" pattern="00" />
				</div>
			</div>
			<div class="p-3 rounded-lg border">
				<div class="text-xs text-gray-500">결근/휴가(승인)</div>
				<div class="text-lg font-semibold">
					<c:out value="${sumAbsent}" /> 건 /<c:out value="${sumLeave}" /> 일
				</div>
			</div>
		</div>
	</c:if>

	<!-- 표 -->
	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">부서</th>
					<th class="px-4 py-2 text-right">인원</th>
					<th class="px-4 py-2 text-right">근무일수</th>
					<th class="px-4 py-2 text-right">총 근무시간</th>
					<th class="px-4 py-2 text-right">총 연장</th>
					<th class="px-4 py-2 text-right">야간 연장</th>
					<th class="px-4 py-2 text-right">주말 연장</th>
					<th class="px-4 py-2 text-right">지각</th>
					<th class="px-4 py-2 text-right">조퇴</th>
					<th class="px-4 py-2 text-right">결근</th>
					<th class="px-4 py-2 text-right">휴가</th>
					<th class="px-4 py-2 text-right">병가</th>
					<th class="px-4 py-2 text-right">1인 평균 근무</th>
					<th class="px-4 py-2 text-right">1인 평균 연장</th>
				</tr>
			</thead>

			<tbody>
				<c:choose>
					<c:when test="${not empty report}">
						<c:forEach var="r" items="${report}">
							<tr class="border-t">
								<td class="px-4 py-2">
									<c:out value="${r.department}" default="-" />
								</td>
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.headcount}" />
								</td>
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.workDays}" />
								</td>

								<!-- 총 근무시간 HH:MM -->
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.totalMinutes div 60}" maxFractionDigits="0" />: 
									<fmt:formatNumber value="${r.totalMinutes mod 60}" pattern="00" />
								</td>

								<!-- 총 연장 -->
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.totalOvertime div 60}" maxFractionDigits="0" />:
									<fmt:formatNumber value="${r.totalOvertime mod 60}" pattern="00" />
								</td>

								<!-- 야간 연장 -->
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.nightOvertime div 60}" maxFractionDigits="0" />:
									<fmt:formatNumber value="${r.nightOvertime mod 60}" pattern="00" />
								</td>

								<!-- 주말 연장 -->
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.weekendOvertime div 60}" maxFractionDigits="0" />:
									<fmt:formatNumber value="${r.weekendOvertime mod 60}" pattern="00" />
								</td>

								<td class="px-4 py-2 text-right">
								<fmt:formatNumber
										value="${r.lateCount}" />
										</td>
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.earlyLeaveCount}" />
								</td>
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.absentCount}" />
								</td>
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.leaveDays}" />
								</td>
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.sickLeaveDays}" />
								</td>

								<!-- 1인 평균 근무 -->
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.avgMinutes div 60}" maxFractionDigits="0" />:
							 		<fmt:formatNumber value="${r.avgMinutes mod 60}" pattern="00" />
								</td>

								<!-- 1인 평균 연장 -->
								<td class="px-4 py-2 text-right">
									<fmt:formatNumber value="${r.avgOvertimePerCapita div 60}" maxFractionDigits="0" />:
									<fmt:formatNumber value="${r.avgOvertimePerCapita mod 60}" pattern="00" />
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="px-4 py-8 text-center text-gray-500" colspan="14">보고서 데이터가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>

			<!-- 합계 행 -->
			<c:if test="${not empty report}">
				<tfoot class="bg-gray-50 font-semibold">
					<tr class="border-t">
						<td class="px-4 py-2">합계</td>
						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumHead}" />
						</td>
						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumWorkDays}" />
						</td>

						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumMinutes div 60}" maxFractionDigits="0" />: 
							<fmt:formatNumber value="${sumMinutes mod 60}" pattern="00" />
						</td>

						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumOver div 60}" maxFractionDigits="0" />: 
							<fmt:formatNumber value="${sumOver mod 60}" pattern="00" />
						</td>

						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumNight div 60}" maxFractionDigits="0" />:
							 <fmt:formatNumber value="${sumNight mod 60}" pattern="00" />
						</td>

						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumWeekend div 60}" maxFractionDigits="0" />:
							<fmt:formatNumber value="${sumWeekend mod 60}" pattern="00" />
						</td>

						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumLate}" />
						</td>
						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumEarly}" />
						</td>
						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumAbsent}" />
						</td>
						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumLeave}" />
						</td>
						<td class="px-4 py-2 text-right">
							<fmt:formatNumber value="${sumSick}" />
						</td>

						<td class="px-4 py-2 text-right">-</td>
						<td class="px-4 py-2 text-right">-</td>
					</tr>
				</tfoot>
			</c:if>
		</table>
	</div>

	<!-- CSV 다운로드 -->
	<div class="mt-4 flex gap-2">
		<a href="<c:url value='/report/monthly.csv'><c:param name='ym' value='${empty param.ym ? ym : param.ym}'/></c:url>"
			class="px-3 py-2 rounded-md border">CSV 다운로드</a>
	</div>
</div>
