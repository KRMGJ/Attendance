(function() {
	var el = document.getElementById('sess-remaining');
	var btn = document.getElementById('sess-extend');
	if (!el || !btn) return;

	var REMAIN_API = '/auth/session/remaining.do';
	var EXTEND_API = '/auth/session/extend.do';
	var LOGIN_URL = '/login.do?expired=1';
	var WARN_SEC = 60;
	var INTERVAL = 1000;

	var lastRemain = null;

	let seenAuthorized = false;

	/**
	 * @param {number} n
	 * @description 초를 mm:ss 형식으로 변환
	 */
	function pad2(n) { return (n < 10 ? '0' : '') + n; }

	/**
	 * @param {number} sec
	 * @description 초를 mm:ss 형식으로 변환
	 * @return {string}
	 */
	function fmt(sec) {
		var s = Math.max(0, Math.floor(sec));
		var m = Math.floor(s / 60);
		var r = s % 60;
		return pad2(m) + ':' + pad2(r);
	}

	/**
	 * @description 서버에서 남은 세션 시간을 가져옴
	 * @return {Promise<number>} 남은 시간(초)
	 */
	function fetchRemain() {
		return fetch(REMAIN_API, { credentials: 'include' })
			.then(async (res) => {
				const ct = res.headers.get('content-type') || '';
				if (res.status === 401) return { auth: false };
				const data = ct.includes('application/json') ? await res.json()
					: JSON.parse(await res.text());
				return data;
			})
			.catch((e) => (
				console.error('Failed to fetch session remaining time:', e), null
			));
	}

	/**
	 * @param {number} sec
	 * @description 남은 시간 표시
	 */
	function paint(sec) {
		el.textContent = fmt(sec);
		if (sec <= 0) {
			el.className = 'text-sm font-mono px-2 py-0.5 rounded bg-red-100 text-red-700';
			return;
		}
		el.className = sec <= WARN_SEC
			? 'text-sm font-mono px-2 py-0.5 rounded bg-yellow-100 text-yellow-800'
			: 'text-sm font-mono px-2 py-0.5 rounded bg-gray-100';
	}

	/**
	 * 1초마다 남은 시간 감소
	 */
	setInterval(function() {
		if (lastRemain == null) return;
		lastRemain = Math.max(0, lastRemain - 1);
		paint(lastRemain);
	}, INTERVAL);

	/**
	 * 5초마다 서버에서 남은 시간 동기화
	 */
	setInterval(() => {
		fetchRemain().then((r) => {
			if (r && r.auth === false) {
				if (seenAuthorized) { window.location.href = LOGIN_URL; }
				return;
			}
			if (typeof r.remainingSeconds === 'number') {
				seenAuthorized = true;
				lastRemain = r.remainingSeconds;
				paint(lastRemain);
			}
		});
	}, 5000);

	/**
	 * 초기화
	 */
	setTimeout(() => fetchRemain().then((r) => {
		if (r && r.auth === false) return;
		if (typeof r.remainingSeconds === 'number') {
			seenAuthorized = true;
			lastRemain = r.remainingSeconds;
			paint(lastRemain);
		}
	}), 1000);

	/**
	 * 연장 버튼 클릭 처리
	 * @description 세션 연장 요청
	 * @return {Promise<void>}
	 */
	btn.addEventListener('click', function() {
		fetch(EXTEND_API, { method: 'POST', credentials: 'same-origin' })
			.then(function(res) {
				if (res.ok || res.status === 204) {
					return fetchRemain().then(function(s) {
						lastRemain = s;
						paint(lastRemain);
					});
				}
			});
	});
})();
