<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="mx-auto max-w-2xl">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-4">직원 정보 수정</h2>

		<form method="post" action="<c:url value='/employee/edit.do'/>"
			class="grid grid-cols-1 md:grid-cols-2 gap-4">
			<input type="hidden" name="id" value="${employee.id}" />

			<div class="col-span-1">
				<label class="block text-sm mb-1">이름</label> <input name="name"
					value="${employee.name}" class="w-full rounded border px-3 py-2"
					required />
			</div>

			<div class="col-span-1">
				<label class="block text-sm mb-1">이메일</label> <input type="email"
					name="email" value="${employee.email}"
					class="w-full rounded border px-3 py-2" required />
			</div>

			<div class="col-span-1">
				<label class="block text-sm mb-1">직급</label> <input name="position"
					value="${employee.position}"
					class="w-full rounded border px-3 py-2" />
			</div>

			<div class="col-span-1">
				<label class="block text-sm mb-1">고용형태</label> <select
					name="employmentType" class="w-full rounded border px-3 py-2">
					<option value="FULL_TIME"
						<c:if test="${employee.employmentType=='FULL_TIME'}">selected</c:if>>정규직</option>
					<option value="PART_TIME"
						<c:if test="${employee.employmentType=='PART_TIME'}">selected</c:if>>계약직</option>
					<option value="INTERN"
						<c:if test="${employee.employmentType=='INTERN'}">selected</c:if>>인턴</option>
				</select>
			</div>

			<!-- 선택: 비밀번호 변경. 비워두면 변경 안함 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">새 비밀번호(선택)</label> <input
					type="password" name="password" placeholder="변경 시에만 입력"
					class="w-full rounded border px-3 py-2" />
			</div>
			<div class="col-span-1">
				<label class="block text-sm mb-1">새 비밀번호 확인</label> <input
					type="password" name="passwordConfirm"
					class="w-full rounded border px-3 py-2" />
			</div>

			<div class="col-span-2 flex justify-end gap-2 mt-2">
				<a href="<c:url value='/employee/detail.do?id=${employee.id}'/>"
					class="px-4 py-2 rounded-md border">취소</a>
				<button class="px-4 py-2 rounded-md bg-gray-900 text-white">저장</button>
			</div>
		</form>
	</div>
</div>
