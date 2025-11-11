<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="mx-auto max-w-md">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-4">로그인</h2>
		<c:if test="${not empty error}">
        	<div class="text-red-600 text-sm mb-2">${error}</div>
    	</c:if>
    	<c:if test="${not empty msg}">
        	<div class="text-green-600 text-sm mb-2">${msg}</div>
    	</c:if>
		<form method="post" action="<c:url value='/loginProcess.do'/>"
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
