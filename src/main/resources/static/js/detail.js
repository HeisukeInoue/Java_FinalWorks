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

// 編集フォーム要素
const editForm = document.getElementById('editForm');
const editFormElement = document.getElementById('editFormElement');
const editBlogIdInput = document.getElementById('editBlogId');
const editTitleInput = document.getElementById('editTitle');
const editTextInput = document.getElementById('editText');
const cancelEditBtn = document.getElementById('cancelEditBtn');

// 現在のブログデータ
let currentBlog = null;

// コメント関連
let commentsVisible = false;
let commentsPage = 1;
const commentsSize = 10;

// URLパラメータからIDを取得
function getblogIdFromUrl() {
	const params = new URLSearchParams(window.location.search);
	return params.get('id');
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
	editFormElement.addEventListener('submit', handleEditSubmit);

	// コメント表示ボタン
	document.getElementById('toggleCommentsBtn').addEventListener('click', toggleComments);
});

// ブログ詳細を取得
async function loadblogDetail(id) {
	try {
		loading.style.display = 'block';
		blogContent.style.display = 'none';
		editForm.style.display = 'none';

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

		currentBlog = blog;
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
	loading.style.display = 'none';
	blogContent.style.display = 'block';
}

// 編集フォームを表示
function showEditForm() {
	if (!currentBlog) return;

	editBlogIdInput.value = currentBlog.id;
	editTitleInput.value = currentBlog.title;
	editTextInput.value = currentBlog.text;

	blogContent.style.display = 'none';
	editForm.style.display = 'block';
	editTitleInput.focus();
}

// 編集フォームを非表示
function hideEditForm() {
	editForm.style.display = 'none';
	blogContent.style.display = 'block';
}

// 編集フォーム送信処理
async function handleEditSubmit(e) {
	e.preventDefault();

	const id = editBlogIdInput.value;
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
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || '更新に失敗しました');
		}

		alert('ブログを更新しました');

		// 更新後のデータを再取得して表示
		await loadblogDetail(id);
		hideEditForm();
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

// コメント表示切り替え
function toggleComments() {
	const commentsSection = document.getElementById('commentsSection');
	const toggleBtn = document.getElementById('toggleCommentsBtn');

	commentsVisible = !commentsVisible;

	if (commentsVisible) {
		commentsSection.style.display = 'block';
		toggleBtn.innerHTML = '<i class="bi bi-chevron-up"></i> コメントを閉じる';
		loadComments(getblogIdFromUrl(), 1);
	} else {
		commentsSection.style.display = 'none';
		toggleBtn.innerHTML = '<i class="bi bi-chevron-down"></i> コメントを見る';
	}
}

// コメント一覧を取得
async function loadComments(blogId, page) {
	const commentsLoading = document.getElementById('commentsLoading');
	const commentsList = document.getElementById('commentsList');
	const commentsPagination = document.getElementById('commentsPagination');

	try {
		commentsLoading.style.display = 'block';
		commentsList.innerHTML = '';

		const response = await fetch(
			`${API_BASE}/${blogId}/comments?size=${commentsSize}&page=${page}`
		);

		if (!response.ok) {
			throw new Error('コメントの取得に失敗しました');
		}

		const apiResponse = await response.json();
		const pagedResponse = apiResponse.data;

		commentsPage = page;
		displayComments(pagedResponse.items);
		displayCommentsPagination(pagedResponse.totalPages, blogId);
	} catch (error) {
		commentsList.innerHTML = '<p class="text-muted">コメントの読み込みに失敗しました</p>';
		console.error('Error loading comments:', error);
	} finally {
		commentsLoading.style.display = 'none';
	}
}

// コメント一覧を表示
function displayComments(comments) {
	const commentsList = document.getElementById('commentsList');

	if (!comments || comments.length === 0) {
		commentsList.innerHTML = '<p class="text-muted">コメントはまだありません</p>';
		return;
	}

	commentsList.innerHTML = comments
		.map(
			(comment) => `
		<div class="border-bottom py-3">
			<div class="d-flex justify-content-between align-items-start mb-2">
				<strong class="text-primary"><i class="bi bi-person-circle"></i> ${escapeHtml(
					comment.createdBy
				)}</strong>
				<small class="text-muted">${formatDate(comment.createdAt)}</small>
			</div>
			<p class="mb-0" style="white-space: pre-wrap;">${escapeHtml(comment.text)}</p>
		</div>
	`
		)
		.join('');
}

// コメントページネーション
function displayCommentsPagination(totalPages, blogId) {
	const paginationContainer = document.getElementById('commentsPagination');
	const paginationList = paginationContainer.querySelector('ul');

	if (totalPages <= 1) {
		paginationContainer.style.display = 'none';
		return;
	}

	paginationContainer.style.display = 'block';
	paginationList.innerHTML = '';

	for (let i = 1; i <= totalPages; i++) {
		const li = document.createElement('li');
		li.className = `page-item ${i === commentsPage ? 'active' : ''}`;
		li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
		li.addEventListener('click', (e) => {
			e.preventDefault();
			loadComments(blogId, i);
		});
		paginationList.appendChild(li);
	}
}

// HTMLエスケープ
function escapeHtml(text) {
	const div = document.createElement('div');
	div.textContent = text;
	return div.innerHTML;
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
