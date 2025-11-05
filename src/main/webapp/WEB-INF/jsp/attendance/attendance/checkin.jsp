<%@ page contentType="text/html; charset=UTF-8"%>
<div class="grid md:grid-cols-2 gap-6">
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-2">출근</h2>
		<form method="post" action="<c:url value='/attendance/checkin.do'/>">
			<button class="px-4 py-2 rounded-md bg-emerald-600 text-white">
				출근 등록
			</button>
		</form>
	</div>
	<div class="bg-white border rounded-xl p-6 shadow-sm">
		<h2 class="text-lg font-semibold mb-2">퇴근</h2>
		<form method="post" action="<c:url value='/attendance/checkout.do'/>">
			<button class="px-4 py-2 rounded-md bg-rose-600 text-white">
				퇴근 등록
			</button>
		</form>
	</div>
</div>
