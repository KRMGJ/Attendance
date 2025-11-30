(function() {
	var el = document.getElementById('sess-remaining');
	var btn = document.getElementById('sess-extend');
	if (!el || !btn) return;

	var EXTEND_API = '/auth/session/extend.do';
	var LOGIN_URL = '/login.do?expired=1';
	var LOGIN_PATH = '/login.do';
	var WARN_SEC = 60;
	var TICK_MS = 1000;

	if (location.pathname === LOGIN_PATH) return;

	var lastRemain = null;
	var timerId = null;

	/**
	 * 초 단위 시간을 "MM:SS" 형식으로 포맷팅
	 */
	function pad2(n) { return (n < 10 ? '0' : '') + n; }

	/**
	 * 초 단위 시간을 "MM:SS" 형식으로 포맷팅
	 */
	function fmt(sec) {
		var s = Math.max(0, Math.floor(sec));
		var m = Math.floor(s / 60), r = s % 60;
		return pad2(m) + ':' + pad2(r);
	}

	/**
	 * 남은시간을 표시
	 */
	function paint(sec) {
		el.textContent = fmt(sec);
		if (sec <= 0) {
			el.className = 'text-sm font-mono px-2 py-0.5 rounded bg-red-100 text-red-700';
		} else {
			el.className = sec <= WARN_SEC
				? 'text-sm font-mono px-2 py-0.5 rounded bg-yellow-100 text-yellow-800'
				: 'text-sm font-mono px-2 py-0.5 rounded bg-gray-100';
		}
	}

	/**
	 * 초기화
	 */
	(function init() {
		var exp = Number(el.getAttribute('data-expires') || 0);
		if (!exp) return; // 값이 없으면 표시만 유지
		var now = Date.now();
		lastRemain = Math.max(0, Math.floor((exp - now) / 1000));
		paint(lastRemain);
	})();

	/**
	 * 1초 간격으로 남은시간 감소 및 표시
	 */
	timerId = setInterval(function() {
		if (lastRemain == null) return;
		lastRemain = Math.max(0, lastRemain - 1);
		paint(lastRemain);
		if (lastRemain === 0) {
			clearInterval(timerId);
			location.href = '/logout.do';
		}
	}, TICK_MS);

	/**
	 * 연장 버튼 클릭 처리
	 */
	btn.addEventListener('click', function() {
		fetch(EXTEND_API, { method: 'POST', credentials: 'same-origin' })
			.then(function(res) { return res.ok ? res.json() : Promise.reject(); })
			.then(function(data) {
				if (data && data.expiresAt) {
					el.setAttribute('data-expires', String(data.expiresAt));
					var now = Date.now();
					lastRemain = Math.max(0, Math.floor((data.expiresAt - now) / 1000));
					paint(lastRemain);
				} else if (typeof data.remainingSeconds === 'number') {
					lastRemain = data.remainingSeconds;
					paint(lastRemain);
				}
			})
			.catch(function() {
				// 실패 시 아무 것도 하지 않음. 필요시 토스트 처리
			});
	});
})();
