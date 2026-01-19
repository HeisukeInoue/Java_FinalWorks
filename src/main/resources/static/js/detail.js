// API ベースURL
const API_BASE = '/api/blogs';

// DOM要素
const loading = document.getElementById('loading');
const blogContent = document.getElementById('blogContent');
const detailTitle = document.getElementById('detailTitle');
const detailMeta = document.getElementById('detailMeta');
const detailText = document.getElementById('detailText');
const blogInfoDisplay = document.getElementById('blogInfoDisplay');
const blogInfoEdit = document.getElementById('blogInfoEdit');
const editBtn = document.getElementById('editBtn');
const deleteBtn = document.getElementById('deleteBtn');
const blogEditForm = document.getElementById('blogEditForm');
const saveBlogBtn = document.getElementById('saveBlogBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const editTitle = document.getElementById('editTitle');
const editText = document.getElementById('editText');
const toggleCommentsBtn = document.getElementById('toggleCommentsBtn');
const commentsSection = document.getElementById('commentsSection');
const commentsLoading = document.getElementById('commentsLoading');
const commentsList = document.getElementById('commentsList');
const commentsPagination = document.getElementById('commentsPagination');
const commentForm = document.getElementById('commentForm');
const commentTextInput = document.getElementById('commentTextInput');
const postCommentBtn = document.getElementById('postCommentBtn');

// コメント関連の変数
let currentBlogId = null;
let currentBlog = null;
let commentsPage = 1;
let commentsTotalPages = 1;
const commentsSize = 10;
let commentsVisible = false;

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

	currentBlogId = parseInt(id);
	await loadblogDetail(id);

	// イベントリスナー
	editBtn.addEventListener('click', showEditForm);
	deleteBtn.addEventListener('click', async () => {
		if (confirm('このブログを削除しますか？')) {
			await deleteblog(id);
		}
	});
	cancelEditBtn.addEventListener('click', hideEditForm);
	blogEditForm.addEventListener('submit', handleBlogUpdate);
	toggleCommentsBtn.addEventListener('click', toggleComments);
	commentForm.addEventListener('submit', handleCommentSubmit);
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
	// 現在のブログ情報を保存
	currentBlog = blog;
	
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
	if (!currentBlog) {
		alert('ブログ情報が読み込まれていません');
		return;
	}
	
	// 現在の値をフォームに設定
	editTitle.value = currentBlog.title || '';
	editText.value = currentBlog.text || '';
	
	// 表示/非表示を切り替え
	blogInfoDisplay.style.display = 'none';
	blogInfoEdit.style.display = 'block';
}

// 編集フォームを非表示
function hideEditForm() {
	blogInfoDisplay.style.display = 'block';
	blogInfoEdit.style.display = 'none';
}

// ブログを更新
async function handleBlogUpdate(e) {
	e.preventDefault();
	
	try {
		saveBlogBtn.disabled = true;
		saveBlogBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>保存中...';
		
		const blogData = {
			title: editTitle.value.trim(),
			text: editText.value.trim()
		};
		
		// バリデーション
		if (!blogData.title || !blogData.text) {
			alert('タイトルと本文を入力してください');
			saveBlogBtn.disabled = false;
			saveBlogBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
			return;
		}
		
		// エンドポイント: PUT /api/blogs/{id}
		const response = await fetch(`${API_BASE}/${currentBlogId}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(blogData),
		});
		
		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || 'ブログの更新に失敗しました');
		}
		
		const apiResponse = await response.json();
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		// 更新後のブログ情報を表示
		const updatedBlog = apiResponse.data;
		if (updatedBlog) {
			displayblogDetail(updatedBlog);
			hideEditForm();
			alert('ブログを更新しました');
		} else {
			// 更新後の情報が取得できない場合は、再度読み込み
			await loadblogDetail(currentBlogId);
			hideEditForm();
			alert('ブログを更新しました');
		}
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating blog:', error);
	} finally {
		saveBlogBtn.disabled = false;
		saveBlogBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
	}
}

// ブログを削除
async function deleteblog(id) {
	try {
		const response = await fetch(`${API_BASE}/${id}`, {
			method: 'DELETE',
		});

		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || '削除に失敗しました');
		}

		alert('ブログを削除しました');
		window.location.href = 'index.html';
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error deleting blog:', error);
	}
}

// コメントセクションの表示/非表示を切り替え
function toggleComments() {
	commentsVisible = !commentsVisible;
	if (commentsVisible) {
		commentsSection.style.display = 'block';
		toggleCommentsBtn.innerHTML = '<i class="bi bi-eye-slash"></i> コメントを隠す';
		loadComments();
	} else {
		commentsSection.style.display = 'none';
		toggleCommentsBtn.innerHTML = '<i class="bi bi-eye"></i> コメントを見る';
	}
}

// コメント一覧を取得
async function loadComments() {
	try {
		commentsLoading.style.display = 'block';
		commentsList.innerHTML = '';

		const response = await fetch(
			`${API_BASE}/${currentBlogId}/comments?size=${commentsSize}&page=${commentsPage}`
		);

		if (!response.ok) {
			throw new Error('コメントの取得に失敗しました');
		}

		const apiResponse = await response.json();

		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		const data = apiResponse.data;
		commentsTotalPages = data.totalPages || 1;

		displayComments(data.items || []);
		displayCommentsPagination(data);
	} catch (error) {
		commentsList.innerHTML = `
			<div class="alert alert-danger" role="alert">
				<i class="bi bi-exclamation-triangle"></i> エラー: ${error.message}
			</div>
		`;
		console.error('Error loading comments:', error);
	} finally {
		commentsLoading.style.display = 'none';
	}
}

// コメント一覧を表示
function displayComments(comments) {
	if (!comments || comments.length === 0) {
		commentsList.innerHTML = `
			<div class="text-muted text-center py-3">
				<i class="bi bi-chat"></i> コメントがありません
			</div>
		`;
		return;
	}

	const html = comments
		.map(
			(comment) => `
			<div class="card mb-3">
				<div class="card-body">
					<div class="d-flex justify-content-between align-items-start mb-2">
						<div>
							<strong class="text-primary">${escapeHtml(comment.createdBy || 'user')}</strong>
							<small class="text-muted ms-2">
								<i class="bi bi-calendar3"></i> ${formatDate(comment.createdAt)}
							</small>
						</div>
					</div>
					<p class="mb-0" style="white-space: pre-wrap;">${escapeHtml(comment.text || '')}</p>
				</div>
			</div>
		`
		)
		.join('');

	commentsList.innerHTML = html;
}

// コメントのページネーションを表示
function displayCommentsPagination(data) {
	if (!data.totalPages || data.totalPages <= 1) {
		commentsPagination.innerHTML = '';
		return;
	}

	const page = data.page || commentsPage;
	commentsPage = page;

	let html = '';

	// 前へボタン
	html += `
		<button class="btn btn-outline-primary btn-sm me-2" 
			onclick="goToCommentsPage(${page - 1}); return false;"
			${page === 1 ? 'disabled' : ''}>
			<i class="bi bi-chevron-left"></i> 前へ
		</button>
	`;

	// ページ番号
	html += `<span class="mx-2">ページ ${page} / ${data.totalPages}</span>`;

	// 次へボタン
	html += `
		<button class="btn btn-outline-primary btn-sm ms-2" 
			onclick="goToCommentsPage(${page + 1}); return false;"
			${page === data.totalPages ? 'disabled' : ''}>
			次へ <i class="bi bi-chevron-right"></i>
		</button>
	`;

	commentsPagination.innerHTML = html;
}

// コメントページ移動（グローバルスコープで定義）
window.goToCommentsPage = function(page) {
	if (page < 1 || page > commentsTotalPages || page === commentsPage) {
		return;
	}
	commentsPage = page;
	loadComments();
};

// コメント投稿
async function handleCommentSubmit(e) {
	e.preventDefault();

	const text = commentTextInput.value.trim();

	if (!text) {
		alert('コメントを入力してください');
		return;
	}

	try {
		postCommentBtn.disabled = true;
		postCommentBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>投稿中...';

		const response = await fetch(`${API_BASE}/${currentBlogId}/comments`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				text: text,
				createdBy: 'user', // デフォルト値
			}),
		});

		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || 'コメントの投稿に失敗しました');
		}

		const apiResponse = await response.json();
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		// コメント入力欄をクリア
		commentTextInput.value = '';

		// コメント一覧を再読み込み
		commentsPage = 1;
		await loadComments();

		alert('コメントを投稿しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error posting comment:', error);
	} finally {
		postCommentBtn.disabled = false;
		postCommentBtn.innerHTML = '<i class="bi bi-send"></i> コメントを投稿';
	}
}

// HTMLエスケープ
function escapeHtml(text) {
	if (!text) return '';
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
