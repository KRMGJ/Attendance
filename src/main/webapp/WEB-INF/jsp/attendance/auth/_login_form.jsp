<%@ page contentType="text/html; charset=UTF-8"%>
<div class="mx-auto max-w-md">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-4">로그인</h2>
		<form method="post" action="<c:url value='/login.do'/>"
			class="space-y-4">
			<div>
				<label class="block text-sm mb-1">아이디</label> <input name="username"
					class="w-full rounded border px-3 py-2" required />
			</div>
			<div>
				<label class="block text-sm mb-1">비밀번호</label> <input
					type="password" name="password"
					class="w-full rounded border px-3 py-2" required />
			</div>
			<button class="w-full rounded-md bg-gray-900 text-white py-2">로그인</button>
		</form>
		<div class="text-sm text-center mt-3">
			계정이 없나요? <a class="underline" href="<c:url value='/employee/join.do'/>">직원 등록</a>
		</div>
	</div>
</div>
