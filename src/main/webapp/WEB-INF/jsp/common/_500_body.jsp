<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="mx-auto max-w-lg bg-white border rounded-xl p-8 text-center">
	<div class="text-3xl font-bold mb-2">500</div>
	<p class="text-gray-600 mb-4">서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>
	<a class="px-4 py-2 rounded-md bg-gray-900 text-white"
		href="<c:url value='/'/>">홈으로</a>
</div>
