<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">권한 계층</h2>

	<!-- 엣지 추가 -->
	<form id="addEdgeForm" method="post"
		action="<c:url value='/admin/security/role-hierarchy/add.do'/>"
		class="grid md:grid-cols-3 gap-3 mb-6">
		<select name="parentRole" id="parentRole"
			class="border rounded px-3 py-2">
			<option value="">상위 권한 선택</option>
			<c:forEach var="r" items="${allRoles}">
				<option value="${r}">${r}</option>
			</c:forEach>
		</select> <select name="childRole" id="childRole"
			class="border rounded px-3 py-2">
			<option value="">하위 권한 선택</option>
			<c:forEach var="r" items="${allRoles}">
				<option value="${r}">${r}</option>
			</c:forEach>
		</select>

		<button class="px-4 py-2 rounded bg-gray-900 text-white">추가</button>
	</form>

	<!-- 트리 리스트 -->
	<div class="space-y-1" id="roleTree">
		<c:forEach var="r" items="${tree}">
			<c:set var="role" value="${r.ROLE}" />
			<c:set var="parent" value="${r.PARENT}" />
			<c:set var="depth" value="${r.DEPTH}" />
			<c:set var="isRoot" value="${r.IS_ROOT}" />
			<div class="flex items-center" data-role="${role}"
				data-parent="${parent}">
				<button type="button"
					class="toggle mr-1 px-1 rounded border text-xs"
					style="visibility:${isRoot==1 || depth>0 ? 'visible':'hidden'}">▸</button>
				<div class="py-1 rounded" style="padding-left:${depth*16}px">
					<span class="font-mono">${role}</span>
					<c:if test="${not empty parent}">
						<span class="text-gray-400 text-xs ml-2">← ${parent}</span>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</div>

	<!-- 상속 요약(모든 상->하) -->
	<div class="mt-8">
		<h3 class="font-semibold mb-2 text-sm">상속 요약</h3>
		<div class="overflow-x-auto">
			<table class="min-w-full text-xs">
				<thead class="bg-gray-50">
					<tr>
						<th class="px-3 py-2 text-left">상위</th>
						<th class="px-3 py-2 text-left">하위</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${closure}">
						<tr class="border-t">
							<td class="px-3 py-1 font-mono">${e.PARENT}</td>
							<td class="px-3 py-1 font-mono">${e.CHILD}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<!-- 현재 엣지 목록 + 삭제 -->
	<div class="mt-8">
		<h3 class="font-semibold mb-2 text-sm">현재 연결(직접 상속)</h3>
		<div class="overflow-x-auto">
			<table class="min-w-full text-xs">
				<thead class="bg-gray-50">
					<tr>
						<th class="px-3 py-2 text-left">상위</th>
						<th class="px-3 py-2 text-left">하위</th>
						<th class="px-3 py-2"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${edges}">
						<tr class="border-t">
							<td class="px-3 py-1 font-mono">${e.PARENT_ROLE}</td>
							<td class="px-3 py-1 font-mono">${e.CHILD_ROLE}</td>
							<td class="px-3 py-1">
								<form method="post"
									action="<c:url value='/admin/security/role-hierarchy/delete.do'/>"
									onsubmit="return confirm('삭제하시겠습니까?');">
									<input type="hidden" name="parentRole" value="${e.PARENT_ROLE}" />
									<input type="hidden" name="childRole" value="${e.CHILD_ROLE}" />
									<button class="px-2 py-1 rounded border">삭제</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script defer src="<c:url value='/js/security/role_hierarchy.js'/>"></script>

