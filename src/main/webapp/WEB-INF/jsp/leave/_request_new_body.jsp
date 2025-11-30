<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="mx-auto max-w-xl bg-white border rounded-xl p-6 shadow-sm">
	<h2 class="text-lg font-semibold mb-4">휴가 신청</h2>
	<form method="post" action="<c:url value='/leave/new.do'/>"
		class="space-y-4">
		<div>
			<label class="block text-sm mb-1">구분</label> 
			<select name="type" class="w-full rounded border px-3 py-2">
				<option value="ANNUAL">연차</option>
				<option value="SICK">병가</option>
			</select>
		</div>
		<div class="grid grid-cols-2 gap-3">
			<div>
				<label class="block text-sm mb-1">시작일</label> 
				<input type="date" name="startDate" class="w-full rounded border px-3 py-2" required />
			</div>
			<div>
				<label class="block text-sm mb-1">종료일</label> 
				<input type="date" name="endDate" class="w-full rounded border px-3 py-2" required />
			</div>
		</div>
		<div>
			<label class="block text-sm mb-1">사유</label>
			<textarea name="reason" rows="3" class="w-full rounded border px-3 py-2"></textarea>
		</div>
		<div class="flex justify-end gap-2">
			<a href="<c:url value='/leave/requests.do'/>" class="px-4 py-2 rounded-md border">취소</a>
			<button class="px-4 py-2 rounded-md bg-gray-900 text-white">신청</button>
		</div>
	</form>
</div>
<c:if test="${not empty message}">
	<script>
        alert("<c:out value='${message}'/>");
    </script>
</c:if>