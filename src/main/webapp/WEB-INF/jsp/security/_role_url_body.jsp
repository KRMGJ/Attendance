<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">URL ↔ 권한 매핑</h2>

	<form method="post"
		action="<c:url value='/admin/security/role-url/add.do'/>"
		class="grid md:grid-cols-4 gap-3 mb-4">
		<select name="authority" class="border rounded px-3 py-2">
			<c:forEach var="r" items="${allRoles}">
				<option>${r}</option>
			</c:forEach>
		</select> <input name="pattern" class="border rounded px-3 py-2"
			placeholder="/admin/**" /> <input name="sort" type="number" value="0"
			class="border rounded px-3 py-2" />
		<button class="px-4 py-2 rounded bg-gray-900 text-white">추가</button>
	</form>

	<table class="min-w-full text-sm">
		<thead class="bg-gray-50">
			<tr>
				<th class="px-4 py-2 text-left">권한</th>
				<th class="px-4 py-2 text-left">패턴</th>
				<th class="px-4 py-2 text-left">정렬</th>
				<th class="px-4 py-2"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="r" items="${rows}">
				<tr class="border-t">
					<td class="px-4 py-2">${r.AUTHORITY}</td>
					<td class="px-4 py-2">${r.ROLE_PATTERN}</td>
					<td class="px-4 py-2">${r.ROLE_SORT}</td>
					<td class="px-4 py-2">
						<form method="post"
							action="<c:url value='/admin/security/role-url/delete.do'/>">
							<input type="hidden" name="authority" value="${r.AUTHORITY}" /> <input
								type="hidden" name="pattern" value="${r.ROLE_PATTERN}" />
							<button class="px-3 py-1 rounded border">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
