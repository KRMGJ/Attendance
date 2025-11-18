<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<div class="bg-white border rounded-xl p-6 shadow-sm">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">휴가 신청 목록</h2>
		<div class="flex items-center gap-2">
			<form id="sizeForm" method="get" class="flex items-center gap-2">
				<input type="hidden" name="page" value="${empty param.page ? 1 : param.page}" /> 
					<label class="text-sm text-gray-600">페이지 크기</label> 
					<select name="size" class="rounded border px-2 py-1" onchange="document.getElementById('sizeForm').submit()">
					<c:set var="sz" value="${empty size ? (empty param.size ? 20 : param.size) : size}" />
					<option value="10" ${sz==10 ? 'selected' : ''}>10</option>
					<option value="20" ${sz==20 ? 'selected' : ''}>20</option>
					<option value="50" ${sz==50 ? 'selected' : ''}>50</option>
				</select>
			</form>
			<a href="<c:url value='/leave/new.do'/>" class="px-3 py-2 rounded-md bg-gray-900 text-white">신청</a>
		</div>
	</div>

	<table class="min-w-full text-sm">
		<thead class="bg-gray-50">
			<tr>
				<th class="px-4 py-2 text-left">기간</th>
				<th class="px-4 py-2 text-left">구분</th>
				<th class="px-4 py-2 text-left">승인자</th>
				<th class="px-4 py-2 text-left">승인 일시</th>
				<th class="px-4 py-2 text-left">상태</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty requests}">
					<c:forEach var="r" items="${requests}">
						<tr class="border-t">
							<td class="px-4 py-2"><c:out value="${r.startDate}" /> ~ <c:out value="${r.endDate}" /></td>
							<td>
								<c:choose>
									<c:when test="${r.type == 'ANNUAL'}">연차</c:when>
									<c:when test="${r.type == 'SICK'}">병가</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
							<td class="px-4 py-2"><c:out value="${empty r.approver ? '-' : r.approver}" /></td>
							<td class="px-4 py-2"><c:out value="${empty r.approvedAt ? '-' : r.approvedAt}" /></td>							
							<td>
								<c:choose>
									<c:when test="${r.status == 'PENDING'}">승인 대기</c:when>
									<c:when test="${r.status == 'APPROVED'}">승인 완료</c:when>
									<c:when test="${r.status == 'REJECTED'}">반려</c:when>
									<c:when test="${r.status == 'CANCELED'}">취소</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="px-4 py-6 text-center text-gray-500" colspan="5">
						휴가신청 기록이 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>

	<!-- 페이지네이션 -->
	<div class="mt-4 flex justify-center">
		<ui:pagination paginationInfo="${paginationInfo}" type="text" jsFunction="linkPage" />
	</div>
</div>

<script>
	function linkPage(pageNo){
		const url = new URL(location.href);
		url.searchParams.set('page', pageNo);
	
		const size = document.querySelector('#sizeForm select[name="size"]')?.value;
		if(size) url.searchParams.set('size', size);
	
		location.href = url.toString();
	}
</script>
