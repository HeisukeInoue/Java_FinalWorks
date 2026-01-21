// API ベースURL
const API_BASE = '/api/blogs';

// DOM要素
const loading = document.getElementById('loading');
const blogContent = document.getElementById('blogContent');
const detailTitle = document.getElementById('detailTitle');
const detailMeta = document.getElementById('detailMeta');
const detailText = document.getElementById('detailText');
const editBtn = document.getElementById('editBtn');
const deleteBtn = document.getElementById('deleteBtn');
const editForm = document.getElementById('editForm');
const blogFormElement = document.getElementById('blogFormElement');
const editTitleInput = document.getElementById('editTitle');
const editTextInput = document.getElementById('editText');
const cancelEditBtn = document.getElementById('cancelEditBtn');

// URLパラメータからIDを取得
function getblogIdFromUrl() {
	const params = new URLSearchParams(window.location.search);
	return params.get('id');
}

// URLパラメータから編集モードを取得
function isEditMode() {
	const params = new URLSearchParams(window.location.search);
	return params.get('edit') === 'true';
}

// 初期化
document.addEventListener('DOMContentLoaded', async () => {
	const id = getblogIdFromUrl();
	if (!id) {
		alert('ブログIDが指定されていません');
		window.location.href = 'index.html';
		return;
	}

	await loadblogDetail(id);

	// イベントリスナー
	editBtn.addEventListener('click', showEditForm);
	deleteBtn.addEventListener('click', async () => {
		if (confirm('このブログを削除しますか？')) {
			await deleteblog(id);
		}
	});
	cancelEditBtn.addEventListener('click', hideEditForm);
	blogFormElement.addEventListener('submit', handleFormSubmit);

	// 編集モードで開かれた場合
	if (isEditMode()) {
		showEditForm();
	}
});

// ブログ詳細を取得
async function loadblogDetail(id) {
	try {
		loading.style.display = 'block';
		blogContent.style.display = 'none';

		const response = await fetch(`${API_BASE}/${id}`);

		if (!response.ok) {
			if (response.status === 404) {
				alert('ブログが見つかりませんでした');
				window.location.href = 'index.html';
				return;
			}
			throw new Error('ブログの取得に失敗しました');
		}

		const apiResponse = await response.json();

		// ApiResponse.data からブログデータを取得
		const blog = apiResponse.data;

		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		if (!blog) {
			alert('ブログが見つかりませんでした');
			window.location.href = 'index.html';
			return;
		}

		displayblogDetail(blog);
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error loading blog detail:', error);
		window.location.href = 'index.html';
	} finally {
		loading.style.display = 'none';
	}
}

// ブログ詳細を表示
function displayblogDetail(blog) {
	detailTitle.textContent = blog.title;
	detailMeta.innerHTML = `
    <div class="mb-2">
      <i class="bi bi-calendar3"></i> 作成日: ${formatDate(blog.createdAt)}
    </div>
    ${
			blog.updatedAt
				? `<div><i class="bi bi-calendar-check"></i> 更新日: ${formatDate(blog.updatedAt)}</div>`
				: ''
		}
  `;
	detailText.textContent = blog.text;
	
	// 編集フォームに値を設定
	editTitleInput.value = blog.title || '';
	editTextInput.value = blog.text || '';
	
	loading.style.display = 'none';
	blogContent.style.display = 'block';
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

	const id = getblogIdFromUrl();
	const title = editTitleInput.value.trim();
	const text = editTextInput.value.trim();

	if (!title || !text) {
		alert('タイトルと本文を入力してください');
		return;
	}

	try {
		const response = await fetch(`${API_BASE}/${id}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ title, text }),
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || '更新に失敗しました');
		}

		hideEditForm();
		await loadblogDetail(id);
		alert('ブログを更新しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating blog:', error);
	}
}

// ブログを削除
async function deleteblog(id) {
	try {
		const response = await fetch(`${API_BASE}/${id}`, {
			method: 'DELETE',
		});

		if (!response.ok) {
			throw new Error('削除に失敗しました');
		}

		alert('ブログを削除しました');
		window.location.href = 'index.html';
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error deleting blog:', error);
	}
}

// ユーティリティ関数
function formatDate(dateString) {
	if (!dateString) return '-';
	const date = new Date(dateString);
	return date.toLocaleString('ja-JP', {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit',
	});
}
