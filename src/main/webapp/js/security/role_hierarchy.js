(function() {
	var tree = document.getElementById('roleTree');
	if (tree) {
		var rows = Array.prototype.slice.call(tree.querySelectorAll(':scope > div'));
		var byParent = {};

		rows.forEach(function(r) {
			var p = r.getAttribute('data-parent') || '';
			if (!byParent[p]) byParent[p] = [];
			byParent[p].push(r);
		});

		function childrenOf(role) {
			return byParent[role] || [];
		}

		function toggle(role, show) {
			childrenOf(role).forEach(function(ch) {
				ch.style.display = show ? '' : 'none';
				// 하위까지 접기 초기화
				var childRole = ch.getAttribute('data-role');
				toggle(childRole, false);
				var btn = ch.querySelector('.toggle');
				if (btn) btn.textContent = '▸';
			});
		}

		rows.forEach(function(r) {
			if (r.getAttribute('data-parent')) r.style.display = 'none';
		});

		var toggles = tree.querySelectorAll('.toggle');
		Array.prototype.forEach.call(toggles, function(btn) {
			btn.addEventListener('click', function(e) {
				var row = e.target.closest('div[data-role]');
				var role = row.getAttribute('data-role');
				var isOpen = e.target.textContent === '▾';

				if (isOpen) {
					toggle(role, false);
					e.target.textContent = '▸';
				} else {
					childrenOf(role).forEach(function(ch) {
						ch.style.display = '';
					});
					e.target.textContent = '▾';
				}
			});
		});
	}

	var form = document.getElementById('addEdgeForm');
	if (form) {
		form.addEventListener('submit', function(e) {
			var p = (document.getElementById('parentRole').value || '').trim();
			var c = (document.getElementById('childRole').value || '').trim();

			if (!p || !c) {
				e.preventDefault();
				alert('상위/하위를 모두 선택하세요.');
				return false;
			}
			if (p === c) {
				e.preventDefault();
				alert('상위와 하위가 동일할 수 없습니다.');
				return false;
			}
			return true;
		});
	}
})();
