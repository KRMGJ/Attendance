<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="mx-auto max-w-2xl">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-4">직원 등록</h2>
		<form method="post" action="<c:url value='/employee/join.do'/>"
			class="grid grid-cols-1 md:grid-cols-2 gap-4">
			<div class="col-span-1">
				<label class="block text-sm mb-1">이름</label> <input name="name"
					class="w-full rounded border px-3 py-2" required />
			</div>
			<div class="col-span-1">
				<label class="block text-sm mb-1">이메일</label> <input type="email"
					name="email" class="w-full rounded border px-3 py-2" required />
			</div>
			<div class="col-span-1">
				<label class="block text-sm mb-1">비밀번호</label> <input
					type="password" name="password"
					class="w-full rounded border px-3 py-2" required />
			</div>
			<div class="col-span-1">
				<label class="block text-sm mb-1">직급</label> <input name="position"
					class="w-full rounded border px-3 py-2" />
			</div>
			<div class="col-span-1">
				<label class="block text-sm mb-1">고용형태</label> <select
					name="employmentType" class="w-full rounded border px-3 py-2">
					<option value="FULL_TIME">정규직</option>
					<option value="PART_TIME">계약직</option>
					<option value="INTERN">인턴</option>
				</select>
			</div>
			<div class="col-span-2 flex justify-end gap-2 mt-2">
				<a href="<c:url value='/employee/list.do'/>"
					class="px-4 py-2 rounded-md border">취소</a>
				<button type="submit" id="joinBtn" class="px-4 py-2 rounded-md bg-gray-900 text-white">등록</button>
			</div>
		</form>
	</div>
</div>
<c:if test="${not empty result}">
<script>
	const result = "${result}";
	if (result === "success") {
	    alert("직원 등록이 완료되었습니다.");
	    window.location.href = "<c:url value='/admin/employee/list.do'/>";
	} else if (result === "fail") {
	    alert("직원 등록에 실패했습니다. 다시 시도해주세요.");
	}
</script>
</c:if>