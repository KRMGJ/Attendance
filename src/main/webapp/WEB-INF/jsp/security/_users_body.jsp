<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<div class="bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">사용자 권한 부여</h2>

	<!-- 검색/필터/페이지 크기 -->
	<form id="searchForm" class="flex flex-wrap items-end gap-2 mb-4"
		method="get">
		<div>
			<label class="block text-sm mb-1">아이디/이메일</label> <input name="q"
				value="${paramQ}" class="border rounded px-3 py-2"
				placeholder="아이디/이메일" />
		</div>
		<div>
			<label class="block text-sm mb-1">역할</label> <select name="role"
				class="border rounded px-3 py-2">
				<option value="">전체</option>
				<c:forEach var="r" items="${allRoles}">
					<option value="${r}" ${paramRole == r ? 'selected' : ''}>${r}</option>
				</c:forEach>
			</select>
		</div>
		<div>
			<label class="block text-sm mb-1">페이지 크기</label> <select name="size"
				class="border rounded px-3 py-2">
				<c:set var="sz"
					value="${empty size ? (empty param.size ? 20 : param.size) : size}" />
				<option value="10" ${sz==10 ? 'selected' : ''}>10</option>
				<option value="20" ${sz==20 ? 'selected' : ''}>20</option>
				<option value="50" ${sz==50 ? 'selected' : ''}>50</option>
			</select>
		</div>
		<button class="px-4 py-2 rounded bg-gray-900 text-white">검색</button>
	</form>

	<div class="overflow-x-auto">
		<table class="min-w-full text-sm">
			<thead class="bg-gray-50">
				<tr>
					<th class="px-4 py-2 text-left">Username</th>
					<th class="px-4 py-2 text-left">Email</th>
					<th class="px-4 py-2 text-left">Enabled</th>
					<th class="px-4 py-2 text-left">권한</th>
					<th class="px-4 py-2"></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty users}">
						<c:forEach var="u" items="${users}">
							<tr class="border-t align-top">
								<td class="px-4 py-2">${u.USERNAME}</td>
								<td class="px-4 py-2">${u.EMAIL}</td>
								<td class="px-4 py-2"><c:out
										value="${u.ENABLED==1?'Y':'N'}" /></td>
								<td class="px-4 py-2">
									<form method="post"
										action="<c:url value='/admin/security/users/assign.do'/>"
										class="flex flex-wrap gap-3 items-center">
										<input type="hidden" name="username" value="${u.USERNAME}" />

										<!-- ROLES는 CSV 문자열(예: 'ROLE_ADMIN,ROLE_USER')이라고 가정 -->
										<c:set var="rolesCsv" value="," />
										<c:set var="rolesCsv" value="${rolesCsv}${u.ROLES}${rolesCsv}" />

										<c:forEach var="r" items="${allRoles}">
											<label class="inline-flex items-center gap-2"> <input
												type="checkbox" name="roles" value="${r}" <c:if
													test="${fn:contains(rolesCsv, ',' += r += ',')}">checked</c:if>
												/> <span class="text-xs">${r}</span>
											</label>
										</c:forEach>

										<button class="px-3 py-1 rounded bg-gray-900 text-white">저장</button>
									</form>
								</td>
								<td class="px-4 py-2"></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="px-4 py-6 text-center text-gray-500" colspan="5">사용자가
								없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>

	<!-- 페이지네이션 -->
	<div class="mt-4 flex justify-center">
		<ui:pagination paginationInfo="${paginationInfo}" type="text"
			jsFunction="goPage" />
	</div>
</div>

<script>
	function goPage(pageNo) {
		const f = document.getElementById('searchForm');
		if (f) {
			const h = document.createElement('input');
			h.type = 'hidden';
			h.name = 'page';
			h.value = pageNo;
			f.appendChild(h);
			f.submit();
		} else {
			const url = new URL(location.href);
			url.searchParams.set('page', pageNo);
			location.href = url.toString();
		}
	}
</script>
