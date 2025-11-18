<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="mx-auto max-w-2xl">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-4">직원 정보 수정</h2>

		<!-- 읽기 전용 정보 (상태, 입사일/퇴사일, 최종 수정일) -->
		<div class="mb-4 grid grid-cols-1 md:grid-cols-3 gap-3 text-sm text-gray-700">
			<div>
				<div class="text-gray-500 text-xs mb-1">고용 상태</div>
				<div class="font-medium">
					<c:choose>
						<c:when test="${employee.status == 'ACTIVE'}">재직 중</c:when>
						<c:when test="${employee.status == 'LEAVE'}">휴직 중</c:when>
						<c:when test="${employee.status == 'RESIGNED'}">퇴사</c:when>
						<c:otherwise>-</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div>
				<div class="text-gray-500 text-xs mb-1">입사일 / 퇴사일</div>
				<div>
					<c:out value="${employee.hireDate}" />
					<c:if test="${not empty employee.resignDate}">
						<br />~ <c:out value="${employee.resignDate}" />
					</c:if>
				</div>
			</div>
			<div>
				<div class="text-gray-500 text-xs mb-1">정보 수정일</div>
				<div>
					<c:out value="${employee.updatedAt}" />
				</div>
			</div>
		</div>

		<form id="employeeEditForm" method="post"
			action="<c:url value='/employee/edit.do'/>"
			class="grid grid-cols-1 md:grid-cols-2 gap-4">

			<!-- PK (hidden) -->
			<input type="hidden" name="id" value="${employee.id}" />

			<!-- 이름 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">이름</label>
				<input name="name"
					value="${employee.name}"
					class="w-full rounded border px-3 py-2"
					required />
			</div>

			<!-- 이메일 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">이메일</label>
				<input type="email" name="email"
					value="${employee.email}"
					class="w-full rounded border px-3 py-2"
					required />
			</div>

			<!-- 사번 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">사번</label>
				<input name="employeeNumber" id="employeeNumberEdit"
					value="${employee.employeeNumber}"
					class="w-full rounded border px-3 py-2" />
				<p class="text-xs text-gray-500 mt-1">
					영문 대문자와 숫자만 사용 가능합니다. (예: EMP001)
				</p>
			</div>

			<!-- 비밀번호 (변경 시에만 입력) -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">비밀번호 변경</label>
				<input type="password" name="password"
					placeholder="변경 시에만 입력"
					class="w-full rounded border px-3 py-2" />
				<p class="text-xs text-gray-500 mt-1">
					비밀번호를 변경하지 않으려면 비워 두세요.
				</p>
			</div>

			<!-- 부서 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">부서</label>
				<input name="department"
					value="${employee.department}"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 직급 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">직급</label>
				<input name="position"
					value="${employee.position}"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 고용 형태 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">고용 형태</label>
				<select name="employmentType" class="w-full rounded border px-3 py-2">
					<option value="FULL_TIME"
						<c:if test="${employee.employmentType == 'FULL_TIME'}">selected</c:if>>
						정규직
					</option>
					<option value="PART_TIME"
						<c:if test="${employee.employmentType == 'PART_TIME'}">selected</c:if>>
						계약직
					</option>
					<option value="INTERN"
						<c:if test="${employee.employmentType == 'INTERN'}">selected</c:if>>
						인턴
					</option>
				</select>
			</div>

			<!-- 입사일 (Date → yyyy-MM-dd) -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">입사일</label>
				<input type="date" name="hireDate"
					value="${empty employee.hireDate ? '' : employee.hireDate}"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 전화번호 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">전화번호</label>
				<input name="phone" id="phoneEdit"
					value="${employee.phone}"
					placeholder="예: 010-1234-5678"
					class="w-full rounded border px-3 py-2" />
				<p class="text-xs text-gray-500 mt-1">
					숫자, -, +, (), 공백만 사용 가능합니다.
				</p>
			</div>

			<!-- 비상 연락처 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">비상 연락처</label>
				<input name="emergencyContactPhone" id="emergencyPhoneEdit"
					value="${employee.emergencyContactPhone}"
					placeholder="예: 010-0000-0000"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 주소 -->
			<div class="col-span-2">
				<label class="block text-sm mb-1">주소</label>
				<input name="address"
					value="${employee.address}"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 근무 시작 시간 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">근무 시작 시간</label>
				<input type="time" name="workStartTime"
					value="<c:out value='${empty employee.workStartTime ? "09:00" : employee.workStartTime}'/>"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 근무 종료 시간 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">근무 종료 시간</label>
				<input type="time" name="workEndTime"
					value="<c:out value='${empty employee.workEndTime ? "18:00" : employee.workEndTime}'/>"
					class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 버튼 -->
			<div class="col-span-2 flex justify-end gap-2 mt-2">
				<a href="<c:url value='/admin/employee/list.do'/>"
					class="px-4 py-2 rounded-md border">취소</a>
				<button type="submit"
					class="px-4 py-2 rounded-md bg-gray-900 text-white">저장</button>
			</div>
		</form>
	</div>
</div>

<c:if test="${not empty result}">
<script>
	const result = "${result}";
	if (result === "success") {
		alert("직원 정보가 수정되었습니다.");
		window.location.href = "<c:url value='/admin/employee/list.do'/>";
	} else {
		alert("직원 정보 수정에 실패했습니다. 다시 시도해주세요.");
	}
</script>
</c:if>

<script>
	(function() {
		var form = document.getElementById('employeeEditForm');
		if (!form) return;

		// 사번 자동 대문자
		var empNoInput = document.getElementById('employeeNumberEdit');
		if (empNoInput) {
			empNoInput.addEventListener('input', function() {
				this.value = this.value.toUpperCase();
			});
		}

		form.addEventListener('submit', function(e) {
			// 사번 패턴: ^[A-Z0-9]+$
			var empNo = empNoInput ? empNoInput.value.trim() : "";
			var empNoPattern = /^[A-Z0-9]+$/;
			if (empNo && !empNoPattern.test(empNo)) {
				alert("사번은 영문 대문자와 숫자만 입력 가능합니다.");
				empNoInput.focus();
				e.preventDefault();
				return;
			}

			// 전화번호 패턴: ^[0-9\-+() ]*$
			var phoneInput = document.getElementById('phoneEdit');
			var emergencyInput = document.getElementById('emergencyPhoneEdit');
			var phonePattern = /^[0-9\-+() ]*$/;

			if (phoneInput && phoneInput.value.trim() &&
				!phonePattern.test(phoneInput.value.trim())) {
				alert("전화번호는 숫자, -, +, (), 공백만 사용할 수 있습니다.");
				phoneInput.focus();
				e.preventDefault();
				return;
			}
			if (emergencyInput && emergencyInput.value.trim() &&
				!phonePattern.test(emergencyInput.value.trim())) {
				alert("비상 연락처는 숫자, -, +, (), 공백만 사용할 수 있습니다.");
				emergencyInput.focus();
				e.preventDefault();
				return;
			}
		});
	})();
</script>
