<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="mx-auto max-w-2xl">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-4">직원 등록</h2>

		<form method="post" action="<c:url value='/admin/employee/join.do'/>"
			class="grid grid-cols-1 md:grid-cols-2 gap-4">

			<!-- 이름 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">이름</label>
				<input name="name" class="w-full rounded border px-3 py-2" required />
			</div>

			<!-- 이메일 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">이메일</label>
				<input type="email" name="email" class="w-full rounded border px-3 py-2" required />
			</div>

			<!-- 직원번호 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">직원 번호</label>
				<input id="employeeNumberCreate" name="employeeNumber" placeholder="예: 23001234" class="w-full rounded border px-3 py-2" />
				<p class="text-xs text-gray-500 mt-1">영문 대문자 + 숫자만 사용 가능 (예: EMP001)</p>
			</div>

			<!-- 비밀번호 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">비밀번호</label>
				<input type="password" name="password" class="w-full rounded border px-3 py-2" required />
			</div>

			<!-- 부서 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">부서</label>
				<input name="department" class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 직급 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">직급</label>
				<input name="position" class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 고용형태 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">고용 형태</label>
				<select name="employmentType" class="w-full rounded border px-3 py-2">
					<option value="FULL_TIME">정규직</option>
					<option value="PART_TIME">계약직</option>
					<option value="INTERN">인턴</option>
				</select>
			</div>

			<!-- 입사일 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">입사일</label>
				<input type="date" name="hireDate" class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 전화번호 -->
            <div class="col-span-1">
				<label class="block text-sm mb-1">전화번호</label>
				<input id="phoneCreate" name="phone" placeholder="예: 010-1234-5678" class="w-full rounded border px-3 py-2" />
				<p class="text-xs text-gray-500 mt-1">숫자, -, +, (), 공백만 사용 가능</p>
			</div>

			<!-- 비상 연락처 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">비상 연락처</label>
				<input id="emergencyPhoneCreate" name="emergencyContactPhone" placeholder="예: 010-0000-0000" class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 주소 -->
			<div class="col-span-2">
				<label class="block text-sm mb-1">주소</label>
				<input name="address" class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 근무시간 -->
			<div class="col-span-1">
				<label class="block text-sm mb-1">근무 시작 시간</label>
				<input type="time" name="workStartTime" value="09:00" class="w-full rounded border px-3 py-2" />
			</div>

			<div class="col-span-1">
				<label class="block text-sm mb-1">근무 종료 시간</label>
				<input type="time" name="workEndTime" value="18:00" class="w-full rounded border px-3 py-2" />
			</div>

			<!-- 버튼 영역 -->
			<div class="col-span-2 flex justify-end gap-2 mt-2">
				<a href="<c:url value='/admin/employee/list.do'/>" class="px-4 py-2 rounded-md border">취소</a>
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

<script>
	(function() {
		var form = document.getElementById('employeeCreateForm');
		if (!form) return;

		// 사번은 자동 대문자 변환
		var empNoInput = document.getElementById('employeeNumberCreate');
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
				console.error("사번은 영문 대문자와 숫자만 입력 가능합니다.");
				empNoInput.focus();
				e.preventDefault();
				return;
			}

			// 전화번호 패턴: ^[0-9\-+() ]+$
			var phoneInput = document.getElementById('phoneCreate');
			var emergencyInput = document.getElementById('emergencyPhoneCreate');
			var phonePattern = /^[0-9\-+() ]*$/; // 빈값 허용

			if (phoneInput && phoneInput.value.trim() &&
				!phonePattern.test(phoneInput.value.trim())) {
				console.error("전화번호는 숫자, -, +, (), 공백만 사용할 수 있습니다.");
				phoneInput.focus();
				e.preventDefault();
				return;
			}

			if (emergencyInput && emergencyInput.value.trim() &&
				!phonePattern.test(emergencyInput.value.trim())) {
				console.error("비상 연락처는 숫자, -, +, (), 공백만 사용할 수 있습니다.");
				emergencyInput.focus();
				e.preventDefault();
				return;
			}
		});
	})();
</script>
