<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">권한 계층</h2>

	<form method="post"
		action="<c:url value='/admin/security/role-hierarchy/add.do'/>"
		class="grid md:grid-cols-3 gap-3 mb-4">
		<select name="parentRole" class="border rounded px-3 py-2">
			<c:forEach var="r" items="${allRoles}">
				<option>${r}</option>
			</c:forEach>
		</select> <select name="childRole" class="border rounded px-3 py-2">
			<c:forEach var="r" items="${allRoles}">
				<option>${r}</option>
			</c:forEach>
		</select>
		<button class="px-4 py-2 rounded bg-gray-900 text-white">추가</button>
	</form>

	<table class="min-w-full text-sm">
		<thead class="bg-gray-50">
			<tr>
				<th class="px-4 py-2 text-left">상위</th>
				<th class="px-4 py-2 text-left">하위</th>
				<th class="px-4 py-2"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="r" items="${rows}">
				<tr class="border-t">
					<td class="px-4 py-2">${r.PARENT_ROLE}</td>
					<td class="px-4 py-2">${r.CHILD_ROLE}</td>
					<td class="px-4 py-2">
						<form method="post"
							action="<c:url value='/admin/security/role-hierarchy/delete.do'/>">
							<input type="hidden" name="parentRole" value="${r.PARENT_ROLE}" />
							<input type="hidden" name="childRole" value="${r.CHILD_ROLE}" />
							<button class="px-3 py-1 rounded border">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
