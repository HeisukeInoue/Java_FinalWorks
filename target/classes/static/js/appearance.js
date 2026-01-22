// API ベースURL
const API_BASE = '/api/appearance';

// URLパラメータからIDを取得
const urlParams = new URLSearchParams(window.location.search);
const appearanceId = urlParams.get('id');
const editMode = urlParams.get('edit') === 'true';

// DOM要素
const loading = document.getElementById('loading');
const appearanceContent = document.getElementById('appearanceContent');
const detailTitle = document.getElementById('detailTitle');
const detailMeta = document.getElementById('detailMeta');
const detailText = document.getElementById('detailText');
const editBtn = document.getElementById('editBtn');
const deleteBtn = document.getElementById('deleteBtn');
const editForm = document.getElementById('editForm');
const appearanceFormElement = document.getElementById('appearanceFormElement');
const editTitleInput = document.getElementById('editTitle');
const editDateInput = document.getElementById('editDate');
const editTextInput = document.getElementById('editText');
const cancelEditBtn = document.getElementById('cancelEditBtn');

// 初期化
document.addEventListener('DOMContentLoaded', () => {
	if (!appearanceId) {
		showError('出演情報IDが指定されていません');
		return;
	}

	loadAppearanceDetail();

	// イベントリスナー
	editBtn.addEventListener('click', showEditForm);
	deleteBtn.addEventListener('click', handleDelete);
	cancelEditBtn.addEventListener('click', hideEditForm);
	appearanceFormElement.addEventListener('submit', handleFormSubmit);

	// 編集モードで開かれた場合
	if (editMode) {
		loadAppearanceDetail().then(() => {
			showEditForm();
		});
	}
});

// 出演詳細を取得
async function loadAppearanceDetail() {
	try {
		loading.style.display = 'block';
		appearanceContent.style.display = 'none';

		const response = await fetch(`${API_BASE}/${appearanceId}`);

		if (!response.ok) {
			throw new Error('出演詳細の取得に失敗しました');
		}

		const apiResponse = await response.json();

		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		const detail = apiResponse.data;
		displayAppearanceDetail(detail);

		loading.style.display = 'none';
		appearanceContent.style.display = 'block';
	} catch (error) {
		showError(error.message);
		console.error('Error loading appearance detail:', error);
	}
}

// 出演詳細を表示
function displayAppearanceDetail(detail) {
	detailTitle.textContent = detail.title || '';

	// 日付をフォーマット
	const date = detail.date || '';
	const formattedDate = formatAppearanceDate(date);
	detailMeta.innerHTML = `
    <div class="d-flex align-items-center gap-3">
      <span><i class="bi bi-calendar3"></i> ${formattedDate}</span>
    </div>
  `;

	detailText.textContent = detail.text || '';

	// 編集フォームに値を設定
	editTitleInput.value = detail.title || '';
	editDateInput.value = date ? date.split('T')[0] : '';
	editTextInput.value = detail.text || '';
}

// 編集フォームを表示
function showEditForm() {
	editForm.style.display = 'block';
	editForm.scrollIntoView({ behavior: 'smooth' });
}

// 編集フォームを非表示
function hideEditForm() {
	editForm.style.display = 'none';
}

// フォーム送信処理
async function handleFormSubmit(e) {
	e.preventDefault();

	const title = editTitleInput.value.trim();
	const date = editDateInput.value;
	const text = editTextInput.value.trim();

	if (!title || !date || !text) {
		alert('すべての項目を入力してください');
		return;
	}

	try {
		const response = await fetch(`${API_BASE}/${appearanceId}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ title, date, text }),
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || '更新に失敗しました');
		}

		hideEditForm();
		loadAppearanceDetail();
		alert('出演情報を更新しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating appearance:', error);
	}
}

// 削除処理
async function handleDelete() {
	if (!confirm('本当にこの出演情報を削除しますか？')) {
		return;
	}

	try {
		const response = await fetch(`${API_BASE}/${appearanceId}`, {
			method: 'DELETE',
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || '削除に失敗しました');
		}

		alert('出演情報を削除しました');
		window.location.href = 'index.html';
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error deleting appearance:', error);
	}
}

// エラーを表示
function showError(message) {
	loading.innerHTML = `
    <div class="alert alert-danger" role="alert">
      <i class="bi bi-exclamation-triangle"></i> ${escapeHtml(message)}
    </div>
  `;
}

// ユーティリティ関数
function escapeHtml(text) {
	const div = document.createElement('div');
	div.textContent = text;
	return div.innerHTML;
}

function formatAppearanceDate(dateString) {
	if (!dateString) return '-';
	const date = new Date(dateString);
	return date.toLocaleDateString('ja-JP', {
		year: 'numeric',
		month: 'long',
		day: 'numeric',
	});
}
