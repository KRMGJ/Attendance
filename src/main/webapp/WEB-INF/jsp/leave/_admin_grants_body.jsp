<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="ui2" uri="http://egovframework.let/attendance/ui" %>

<c:url value="/admin/leave/grants.do" var="GRANTS_URL" />

<div class="bg-white border rounded-xl p-6 shadow-sm">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">연차 지급/차감 이력 (관리자)</h2>

		<form id="sizeForm" method="get" action="${GRANTS_URL}" class="flex items-center gap-2">
			<input type="hidden" name="page" value="${empty param.page ? 1 : param.page}" />
			<input type="hidden" name="q" value="${paramQ}" /> 
			<input type="hidden" name="year" value="${paramYear}" />
		    <input type="hidden" name="reason" value="${paramReason}" /> 
		    <input type="hidden" name="kind" value="${paramKind}" />
		    <input type="hidden" name="from" value="<fmt:formatDate value='${paramFrom}' pattern='yyyy-MM-dd'/>" />
			<input type="hidden" name="to" value="<fmt:formatDate value='${paramTo}' pattern='yyyy-MM-dd'/>" />

			<label class="text-sm text-gray-600">페이지 크기</label>
			<c:set var="sz" value="${empty size ? (empty param.size ? 20 : param.size) : size}" />
			<select name="size" class="rounded border px-2 py-1" onchange="document.getElementById('sizeForm').submit()">
				<option value="10" ${sz==10  ? 'selected' : ''}>10</option>
				<option value="20" ${sz==20  ? 'selected' : ''}>20</option>
				<option value="50" ${sz==50  ? 'selected' : ''}>50</option>
				<option value="100" ${sz==100 ? 'selected' : ''}>100</option>
			</select>
		</form>
	</div>

	<!-- 검색/필터 폼 -->
	<form method="get" action="${GRANTS_URL}" class="grid md:grid-cols-6 gap-3 mb-4">
		<div class="md:col-span-2">
			<label class="block text-sm mb-1">직원 검색 (이름/이메일)</label>
			<input type="text" name="q" value="${paramQ}" class="w-full rounded border px-3 py-2" placeholder="이름 또는 이메일" />
		</div>
		<div>
			<label class="block text-sm mb-1">연도</label> 
			<input type="number" name="year" value="${paramYear}" class="w-full rounded border px-3 py-2" placeholder="예) 2025" />
		</div>
		<div>
			<label class="block text-sm mb-1">지급일시 From</label> 
			<input type="date" name="from" value="<fmt:formatDate value='${paramFrom}' pattern='yyyy-MM-dd'/>" class="w-full rounded border px-3 py-2" />
		</div>
		<div>
			<label class="block text-sm mb-1">지급일시 To</label> 
			<input type="date" name="to" value="<fmt:formatDate value='${paramTo}' pattern='yyyy-MM-dd'/>" class="w-full rounded border px-3 py-2" />
		</div>
		<div>
		    <label class="block text-sm mb-1">구분</label>
		    <select name="kind" class="w-full rounded border px-3 py-2">
		        <c:set var="k" value="${empty paramKind ? (empty param.kind ? '' : param.kind) : paramKind}" />
		        <option value="" ${k == '' ? 'selected' : ''}>전체</option>
		        <option value="GRANT"  ${k == 'GRANT' ? 'selected' : ''}>지급만</option>
		        <option value="DEDUCT" ${k == 'DEDUCT' ? 'selected' : ''}>차감만</option>
		    </select>
		</div>
		<div class="md:col-span-2">
			<label class="block text-sm mb-1">지급 사유</label> 
			<input type="text" name="reason" value="${paramReason}" class="w-full rounded border px-3 py-2" placeholder="예) 입사1년미만 월차, 연차조정 등" />
		</div>
		<div class="flex items-end">
			<button class="rounded-md bg-gray-900 text-white px-4 py-2">조회</button>
		</div>
	</form>

	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">직원</th>
					<th class="px-4 py-2 text-left">이메일</th>
					<th class="px-4 py-2 text-left">부서</th>
					<th class="px-4 py-2 text-left">연도</th>
					<th class="px-4 py-2 text-left">지급 사유</th>
					<th class="px-4 py-2 text-left">변경 일수</th>
					<th class="px-4 py-2 text-left">지급일시</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach var="r" items="${list}">
							<tr class="border-t">
								<td class="px-4 py-2"><c:out value="${r.empName}" /></td>
								<td class="px-4 py-2"><c:out value="${r.empEmail}" /></td>
								<td class="px-4 py-2"><c:out value="${empty r.department ? '-' : r.department}" /></td>
								<td class="px-4 py-2"><c:out value="${r.year}" /></td>
								<td class="px-4 py-2">
									<span class="${ui2:grantReasonColor(r.reason)} font-medium">
										${ui2:grantReasonIcon(r.reason)}
										${ui2:label(r.reason)}
									</span>
								</td>
								<td class="px-4 py-2 text-left">
									<c:choose>
										<c:when test="${r.changeDays > 0}">
											<span class="text-green-600">+<c:out value="${r.changeDays}" /></span>
										</c:when>
										<c:when test="${r.changeDays < 0}">
											<span class="text-red-600"><c:out value="${r.changeDays}" /></span>
										</c:when>
										<c:otherwise>
											<span class="text-gray-700"><c:out value="${r.changeDays}" /></span>
										</c:otherwise>
									</c:choose>
								</td>
								<td class="px-4 py-2"><fmt:formatDate value="${r.grantedAt}" pattern="yyyy-MM-dd HH:mm" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="px-4 py-6 text-center text-gray-500" colspan="7">조회된 이력이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>

	<div class="mt-4 flex justify-center">
		<ui:pagination paginationInfo="${paginationInfo}" type="text" jsFunction="goPage" />
	</div>
</div>

<script>
	function goPage(pageNo) {
		const url = new URL(location.href);
		url.searchParams.set('page', pageNo);
		const sizeSel = document.querySelector('#sizeForm select[name="size"]');
		if (sizeSel) {
			url.searchParams.set('size', sizeSel.value);
		}
		location.href = url.toString();
	}
</script>
