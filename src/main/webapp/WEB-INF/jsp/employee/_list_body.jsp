<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<div class="bg-white border rounded-xl p-6 shadow-sm">

	<!-- 검색 폼 -->
	<form id="searchForm" method="get"
		class="flex flex-wrap items-end gap-3 mb-4">
		<div>
			<label class="block text-sm mb-1">이름/이메일</label> <input name="q"
				value="${paramQ}" class="rounded border px-3 py-2" placeholder="검색어" />
		</div>
		<div>
			<label class="block text-sm mb-1">부서</label> <input name="dept"
				value="${paramDept}" class="rounded border px-3 py-2"
				placeholder="부서" />
		</div>
		<div>
			<label class="block text-sm mb-1">상태</label> <input name="status"
				value="${paramStatus}" class="rounded border px-3 py-2"
				placeholder="재직/휴직/퇴사" />
		</div>
		<div>
			<label class="block text-sm mb-1">페이지 크기</label> <input type="number"
				name="size" min="1" value="${size}"
				class="w-24 rounded border px-3 py-2" />
		</div>
		<button class="rounded-md bg-gray-900 text-white px-4 py-2 h-10">검색</button>
	</form>

	<div class="flex items-center justify-between mb-3">
		<h2 class="text-lg font-semibold">직원 목록</h2>
		<a href="<c:url value='/employee/join.do'/>"
			class="px-3 py-2 rounded-md bg-gray-900 text-white">직원 등록</a>
	</div>

	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">이름</th>
					<th class="px-4 py-2 text-left">이메일</th>
					<th class="px-4 py-2 text-left">부서</th>
					<th class="px-4 py-2 text-left">직급</th>
					<th class="px-4 py-2 text-left">상태</th>
					<th class="px-4 py-2 text-left">고용 형태</th>
					<th class="px-4 py-2"></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach var="e" items="${list}">
							<tr class="border-t">
								<td class="px-4 py-2"><c:out value="${e.name}" /></td>
								<td class="px-4 py-2"><c:out value="${e.email}" /></td>
								<td class="px-4 py-2"><c:out value="${e.department}" /></td>
								<td class="px-4 py-2"><c:out value="${e.position}" /></td>
								<td class="px-4 py-2"><c:out value="${e.status}" /></td>
								<td class="px-4 py-2"><c:out value="${e.employmentType}" /></td>
								<td class="px-4 py-2 text-right"><a class="underline"
									href="<c:url value='/employee/detail.do?id=${e.id}'/>">보기</a></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="px-4 py-6 text-center text-gray-500" colspan="7">등록된
								직원이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>

	<!-- 페이지네이션 -->
	<div class="mt-4 flex justify-center">
		<ui:pagination paginationInfo="${paginationInfo}" type="text" jsFunction="linkPage"/>
	</div>
</div>

<script>
	function goPage(pageNo) {
		const f = document.getElementById('searchForm');
		if (f) {
			let h = document.createElement('input');
			h.type = 'hidden';
			h.name = 'page';
			h.value = pageNo;
			f.appendChild(h);
			f.submit();
		} else {
			const u = new URL(location.href);
			u.searchParams.set('page', pageNo);
			location.href = u.toString();
		}
	}
</script>
