<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">사용자 권한 부여</h2>

	<form class="flex gap-2 mb-4" method="get">
		<input name="q" value="${param.q}" class="border rounded px-3 py-2"
			placeholder="아이디/이메일" />
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
				<c:forEach var="u" items="${users}">
					<tr class="border-t align-top">
						<td class="px-4 py-2">${u.USERNAME}</td>
						<td class="px-4 py-2">${u.EMAIL}</td>
						<td class="px-4 py-2"><c:out value="${u.ENABLED==1?'Y':'N'}" /></td>
						<td class="px-4 py-2">
							<form method="post"
								action="<c:url value='/admin/security/users/assign.do'/>"
								class="flex flex-wrap gap-3">
								<input type="hidden" name="username" value="${u.USERNAME}" />
								<c:forEach var="r" items="${allRoles}">
									<label class="inline-flex items-center gap-2"> <input
										type="checkbox" name="roles" value="${r}"
										<c:if test="${fn:contains(securityAdminService.findUserRoles(u.USERNAME), r)}">checked</c:if> />
										<span class="text-xs">${r}</span>
									</label>
								</c:forEach>
								<button class="px-3 py-1 rounded bg-gray-900 text-white">저장</button>
							</form>
						</td>
						<td class="px-4 py-2"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
