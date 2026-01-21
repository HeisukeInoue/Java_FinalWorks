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
const commentForm = document.getElementById('commentForm');
const commentTextInput = document.getElementById('commentText');
const commentsSection = document.getElementById('commentsSection');
const commentsLoading = document.getElementById('commentsLoading');
const commentsList = document.getElementById('commentsList');
const commentsPagination = document.getElementById('commentsPagination');

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
	await loadComments(id, 1);

	// イベントリスナー
	editBtn.addEventListener('click', showEditForm);
	deleteBtn.addEventListener('click', async () => {
		if (confirm('このブログを削除しますか？')) {
			await deleteblog(id);
		}
	});
	cancelEditBtn.addEventListener('click', hideEditForm);
	blogFormElement.addEventListener('submit', handleFormSubmit);
	commentForm.addEventListener('submit', handleCommentSubmit);

	// コメントセクションにイベント委譲を設定
	commentsList.addEventListener('click', (e) => {
		const deleteBtn = e.target.closest('.delete-comment-btn');
		if (deleteBtn) {
			e.preventDefault();
			e.stopPropagation();
			const commentId = deleteBtn.getAttribute('data-comment-id');
			if (commentId) {
				handleCommentDelete(parseInt(commentId));
			}
			return;
		}

		const editBtn = e.target.closest('.edit-comment-btn');
		if (editBtn) {
			e.preventDefault();
			e.stopPropagation();
			const commentId = editBtn.getAttribute('data-comment-id');
			if (commentId) {
				const editForm = document.getElementById(`edit-form-${commentId}`);
				const commentItem = editBtn.closest('.comment-item');
				const commentText = commentItem.querySelector('.comment-text');
				commentText.style.display = 'none';
				editForm.style.display = 'block';
			}
			return;
		}

		const saveBtn = e.target.closest('.save-comment-btn');
		if (saveBtn) {
			e.preventDefault();
			e.stopPropagation();
			const commentId = saveBtn.getAttribute('data-comment-id');
			if (commentId) {
				handleCommentEdit(parseInt(commentId));
			}
			return;
		}

		const cancelBtn = e.target.closest('.cancel-edit-btn');
		if (cancelBtn) {
			e.preventDefault();
			e.stopPropagation();
			const commentId = cancelBtn.getAttribute('data-comment-id');
			if (commentId) {
				const editForm = document.getElementById(`edit-form-${commentId}`);
				const commentItem = cancelBtn.closest('.comment-item');
				const commentText = commentItem.querySelector('.comment-text');
				editForm.style.display = 'none';
				commentText.style.display = 'block';
			}
			return;
		}
	});

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

// コメント一覧を取得
let currentCommentPage = 1;
const COMMENT_PAGE_SIZE = 10;

async function loadComments(blogId, page) {
	try {
		commentsLoading.style.display = 'block';
		commentsList.style.display = 'none';
		commentsPagination.style.display = 'none';

		const response = await fetch(`${API_BASE}/${blogId}/comments?size=${COMMENT_PAGE_SIZE}&page=${page}`);

		if (!response.ok) {
			throw new Error('コメントの取得に失敗しました');
		}

		const apiResponse = await response.json();

		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		const pagedData = apiResponse.data;
		displayComments(pagedData);
		displayCommentPagination(pagedData, blogId);
	} catch (error) {
		commentsLoading.innerHTML = `
			<div class="alert alert-warning" role="alert">
				<i class="bi bi-exclamation-triangle"></i> コメントの読み込みに失敗しました
			</div>
		`;
		console.error('Error loading comments:', error);
	} finally {
		commentsLoading.style.display = 'none';
		commentsList.style.display = 'block';
	}
}

// コメント一覧を表示
function displayComments(pagedData) {
	const comments = pagedData.items || [];

	if (comments.length === 0) {
		commentsList.innerHTML = `
			<div class="text-muted text-center py-3">
				<i class="bi bi-chat"></i> コメントはまだありません
			</div>
		`;
		return;
	}

	const html = comments
		.map((comment) => `
			<div class="comment-item mb-3 pb-3 border-bottom" data-comment-id="${comment.id}">
				<div class="d-flex justify-content-between align-items-start mb-2">
					<div class="flex-grow-1">
						<div class="d-flex align-items-center gap-2 mb-2">
							<strong class="small">${escapeHtml(comment.createdBy || 'user')}</strong>
							<small class="text-muted">${formatDate(comment.createdAt)}</small>
						</div>
						<div class="comment-text">${escapeHtml(comment.text)}</div>
					</div>
					<div class="comment-actions ms-3">
						<button class="btn btn-sm btn-outline-primary edit-comment-btn" data-comment-id="${comment.id}">
							<i class="bi bi-pencil"></i>
						</button>
						<button class="btn btn-sm btn-outline-danger delete-comment-btn" data-comment-id="${comment.id}">
							<i class="bi bi-trash"></i>
						</button>
					</div>
				</div>
				<!-- 編集フォーム（非表示） -->
				<div class="comment-edit-form mt-2" id="edit-form-${comment.id}" style="display: none;">
					<textarea class="form-control mb-2" id="edit-text-${comment.id}" rows="2">${escapeHtml(comment.text)}</textarea>
					<div class="d-flex gap-2">
						<button class="btn btn-sm btn-primary save-comment-btn" data-comment-id="${comment.id}">
							<i class="bi bi-check"></i> 保存
						</button>
						<button class="btn btn-sm btn-outline-secondary cancel-edit-btn" data-comment-id="${comment.id}">
							キャンセル
						</button>
					</div>
				</div>
			</div>
		`)
		.join('');

	commentsList.innerHTML = html;
}

// コメントページネーションを表示
function displayCommentPagination(pagedData, blogId) {
	if (!pagedData.totalPages || pagedData.totalPages <= 1) {
		commentsPagination.style.display = 'none';
		return;
	}

	commentsPagination.style.display = 'block';
	currentCommentPage = pagedData.page || 1;
	const totalPages = pagedData.totalPages;

	let html = '';

	// 前へボタン
	html += `
		<button class="btn btn-sm btn-outline-secondary ${currentCommentPage === 1 ? 'disabled' : ''}" 
			onclick="goToCommentPage(${currentCommentPage - 1}, ${blogId})" ${currentCommentPage === 1 ? 'disabled' : ''}>
			<i class="bi bi-chevron-left"></i> 前へ
		</button>
	`;

	// ページ番号
	html += `<span class="mx-3">ページ ${currentCommentPage} / ${totalPages}</span>`;

	// 次へボタン
	html += `
		<button class="btn btn-sm btn-outline-secondary ${currentCommentPage === totalPages ? 'disabled' : ''}" 
			onclick="goToCommentPage(${currentCommentPage + 1}, ${blogId})" ${currentCommentPage === totalPages ? 'disabled' : ''}>
			次へ <i class="bi bi-chevron-right"></i>
		</button>
	`;

	commentsPagination.innerHTML = `<div class="d-flex justify-content-center align-items-center">${html}</div>`;
}

// コメントページ移動
function goToCommentPage(page, blogId) {
	if (page < 1) return;
	loadComments(blogId, page);
	window.scrollTo({ top: commentsSection.offsetTop, behavior: 'smooth' });
}

// コメント投稿
async function handleCommentSubmit(e) {
	e.preventDefault();

	const blogId = getblogIdFromUrl();
	const text = commentTextInput.value.trim();

	if (!text) {
		alert('コメントを入力してください');
		return;
	}

	try {
		const response = await fetch(`${API_BASE}/${blogId}/comments`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ text, createdBy: 'user' }),
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || 'コメントの投稿に失敗しました');
		}

		commentTextInput.value = '';
		await loadComments(blogId, 1);
		alert('コメントを投稿しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error posting comment:', error);
	}
}

// コメント編集
async function handleCommentEdit(commentId) {
	const blogId = getblogIdFromUrl();
	const editForm = document.getElementById(`edit-form-${commentId}`);
	const editText = document.getElementById(`edit-text-${commentId}`);
	const text = editText.value.trim();

	if (!text) {
		alert('コメントを入力してください');
		return;
	}

	try {
		const response = await fetch(`${API_BASE}/${blogId}/comments/${commentId}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ text }),
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || 'コメントの更新に失敗しました');
		}

		await loadComments(blogId, currentCommentPage);
		alert('コメントを更新しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating comment:', error);
	}
}

// コメント削除
async function handleCommentDelete(commentId) {
	console.log('handleCommentDelete called with commentId:', commentId);
	
	if (!confirm('このコメントを削除しますか？')) {
		console.log('Delete cancelled by user');
		return;
	}

	const blogId = getblogIdFromUrl();
	console.log('Deleting comment, blogId:', blogId, 'commentId:', commentId);

	try {
		const url = `${API_BASE}/${blogId}/comments/${commentId}`;
		console.log('DELETE request to:', url);
		
		const response = await fetch(url, {
			method: 'DELETE',
		});

		console.log('Response status:', response.status);
		console.log('Response ok:', response.ok);

		if (!response.ok) {
			const errorData = await response.json().catch(() => ({ error: 'Unknown error' }));
			console.error('Delete failed:', errorData);
			throw new Error(errorData.error || 'コメントの削除に失敗しました');
		}

		const result = await response.json().catch(() => null);
		console.log('Delete successful, result:', result);

		// コメント一覧を再読み込み
		await loadComments(blogId, currentCommentPage);
		alert('コメントを削除しました');
	} catch (error) {
		console.error('Error deleting comment:', error);
		alert(`エラー: ${error.message}`);
	}
}


// ユーティリティ関数
function escapeHtml(text) {
	const div = document.createElement('div');
	div.textContent = text;
	return div.innerHTML;
}

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
