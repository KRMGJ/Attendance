<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="bg-white border rounded-xl p-6 shadow-sm max-w-3xl">
	<div class="flex items-center justify-between mb-4">
		<h2 class="text-lg font-semibold">직원 상세</h2>
		<div class="flex gap-2">
			<a href="<c:url value='/employee/edit.do?id=${employee.id}'/>" class="px-3 py-2 rounded-md bg-gray-900 text-white">수정</a>
			<a href="<c:url value='/admin/employee/list.do'/>" class="px-3 py-2 rounded-md border">목록</a>
		</div>
	</div>

	<dl class="grid grid-cols-1 md:grid-cols-3 gap-x-6 gap-y-4 text-sm">

		<!-- 기본 정보 -->
		<dt class="text-gray-500">이름</dt>
		<dd class="md:col-span-2 font-medium">
			<c:out value="${employee.name}" />
		</dd>

		<dt class="text-gray-500">이메일</dt>
		<dd class="md:col-span-2 font-medium">
			<c:out value="${employee.email}" />
		</dd>

		<c:if test="${not empty employee.employeeNumber}">
			<dt class="text-gray-500">사번</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.employeeNumber}" />
			</dd>
		</c:if>

		<c:if test="${not empty employee.department}">
			<dt class="text-gray-500">부서</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.department}" />
			</dd>
		</c:if>

		<c:if test="${not empty employee.position}">
			<dt class="text-gray-500">직급</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.position}" />
			</dd>
		</c:if>

		<dt class="text-gray-500">고용형태</dt>
		<dd class="md:col-span-2">
			<c:out value="${employee.employmentType}" />
		</dd>

		<c:if test="${not empty employee.status}">
			<dt class="text-gray-500">상태</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.status}" />
			</dd>
		</c:if>

		<!-- 연락처 / 주소 -->
		<c:if test="${not empty employee.phone}">
			<dt class="text-gray-500">전화번호</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.phone}" />
			</dd>
		</c:if>

		<c:if test="${not empty employee.emergencyContactPhone}">
			<dt class="text-gray-500">비상 연락처</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.emergencyContactPhone}" />
			</dd>
		</c:if>

		<c:if test="${not empty employee.address}">
			<dt class="text-gray-500">주소</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.address}" />
			</dd>
		</c:if>

		<!-- 근무 시간 -->
		<dt class="text-gray-500">근무 시간</dt>
		<dd class="md:col-span-2">
			<c:out value="${employee.workStartTime}" /> ~
			<c:out value="${employee.workEndTime}" />
		</dd>

		<!-- 날짜들 -->
		<c:if test="${not empty employee.hireDate}">
			<dt class="text-gray-500">입사일</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.hireDate}" />
			</dd>
		</c:if>

		<c:if test="${not empty employee.resignDate}">
			<dt class="text-gray-500">퇴사일</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.resignDate}" />
			</dd>
		</c:if>

		<c:if test="${not empty employee.updatedAt}">
			<dt class="text-gray-500">정보 수정일</dt>
			<dd class="md:col-span-2">
				<c:out value="${employee.updatedAt}" />
			</dd>
		</c:if>

	</dl>

	<div class="mt-6 border-t pt-4 flex justify-end gap-2">
		<a href="<c:url value='/employee/edit.do?id=${employee.id}'/>"
			class="px-4 py-2 rounded-md bg-gray-900 text-white">정보 수정</a>

		<form method="post" action="<c:url value='/employee/delete.do'/>"
			class="inline"
			onsubmit="return confirm('정말 삭제하시겠습니까?');">
			<input type="hidden" name="id" value="${employee.id}" />
			<button class="px-4 py-2 rounded-md bg-rose-600 text-white">
				삭제
			</button>
		</form>
	</div>
</div>
