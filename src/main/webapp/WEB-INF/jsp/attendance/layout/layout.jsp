<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<script src="https://cdn.tailwindcss.com"></script>
<title><c:out value="${pageTitle != null ? pageTitle : '근태관리'}" /></title>
</head>
<body class="min-h-screen bg-gray-50 text-gray-900">
	<div class="flex min-h-screen">
		<jsp:include page="/WEB-INF/jsp/attendance/layout/sidebar.jspf" />
		<div class="flex-1 flex flex-col">
			<jsp:include page="/WEB-INF/jsp/attendance/layout/header.jspf" />
			<main class="p-6">
				<jsp:include page="/WEB-INF/jsp/attendance/layout/flash.jspf" />
				<jsp:include page="${content}" />
			</main>
			<jsp:include page="/WEB-INF/jsp/attendance/layout/footer.jspf" />
		</div>
	</div>
</body>
</html>
