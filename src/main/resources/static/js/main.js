// API ベースURL
const API_BASE = '/api/blogs';

// DOM要素
const blogList = document.getElementById('blogList');
const blogForm = document.getElementById('blogForm');
const blogFormElement = document.getElementById('blogFormElement');
const newblogBtn = document.getElementById('newblogBtn');
const cancelBtn = document.getElementById('cancelBtn');
const formTitle = document.getElementById('formTitle');
const blogIdInput = document.getElementById('blogId');
const titleInput = document.getElementById('title');
const textInput = document.getElementById('text');
const pagination = document.getElementById('pagination');
const paginationList = pagination.querySelector('ul');
const appearanceContent = document.getElementById('appearanceContent');

let currentPage = 1;
let totalPages = 1;
let isEditMode = false;

// 初期化
document.addEventListener('DOMContentLoaded', () => {
	loadblogList();
	loadappearances();

	// イベントリスナー
	newblogBtn.addEventListener('click', showNewblogForm);
	cancelBtn.addEventListener('click', hideblogForm);
	blogFormElement.addEventListener('submit', handleFormSubmit);
});

// ブログ一覧を取得
async function loadblogList() {
	try {
		blogList.innerHTML = `
      <div class="col-12">
        <div class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">読み込み中...</span>
          </div>
          <p class="mt-2 text-muted">読み込み中...</p>
        </div>
      </div>
    `;
		const response = await fetch(`${API_BASE}?size=10&page=${currentPage}`);

		if (!response.ok) {
			throw new Error('ブログ一覧の取得に失敗しました');
		}

		const apiResponse = await response.json();
		
		// ApiResponse.data から PagedResponse を取得
		const data = apiResponse.data;
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		totalPages = data.totalPages || 1;
		displayblogList(data);
		displayPagination(data);
	} catch (error) {
		blogList.innerHTML = `
      <div class="col-12">
        <div class="alert alert-danger" role="alert">
          <i class="bi bi-exclamation-triangle"></i> エラー: ${error.message}
        </div>
      </div>
    `;
		console.error('Error loading blog list:', error);
	}
}

// ブログ一覧を表示
function displayblogList(data) {
	// PagedResponse の items からブログ一覧を取得
	const blogs = data.items || data.blogs || [];
	
	if (blogs.length === 0) {
		blogList.innerHTML = `
      <div class="col-12">
        <div class="card shadow-sm">
          <div class="card-body text-center py-5">
            <i class="bi bi-inbox fs-1 text-muted"></i>
            <p class="mt-3 text-muted">ブログがありません</p>
          </div>
        </div>
      </div>
    `;
		return;
	}

	const html = blogs
		.map(
			(blog) => `
        <div class="col-md-6">
          <div class="card shadow-sm h-100 blog-card" onclick="window.location.href='detail.html?id=${
						blog.id
					}'" style="cursor: pointer; transition: transform 0.2s;">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title fw-bold mb-3">${escapeHtml(blog.title)}</h5>
              <div class="mt-auto">
                <small class="text-muted">
                  <i class="bi bi-calendar3"></i> ${formatDate(blog.createdAt)}
                </small>
              </div>
            </div>
          </div>
        </div>
    `
		)
		.join('');

	blogList.innerHTML = html;
}

// ページネーションを表示
function displayPagination(data) {
	if (!data.totalPages || data.totalPages <= 1) {
		pagination.style.display = 'none';
		return;
	}

	pagination.style.display = 'block';
	totalPages = data.totalPages;
	const page = data.page || currentPage;
	currentPage = page;

	let html = '';

	// 前へボタン
	html += `
    <li class="page-item ${page === 1 ? 'disabled' : ''}">
      <a class="page-link" href="#" onclick="goToPage(${
				page - 1
			}); return false;" aria-label="前へ">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
  `;

	// ページ番号
	const maxVisible = 5;
	let startPage = Math.max(1, page - Math.floor(maxVisible / 2));
	let endPage = Math.min(totalPages, startPage + maxVisible - 1);

	if (endPage - startPage < maxVisible - 1) {
		startPage = Math.max(1, endPage - maxVisible + 1);
	}

	if (startPage > 1) {
		html += `
      <li class="page-item">
        <a class="page-link" href="#" onclick="goToPage(1); return false;">1</a>
      </li>
    `;
		if (startPage > 2) {
			html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
		}
	}

	for (let i = startPage; i <= endPage; i++) {
		html += `
      <li class="page-item ${i === page ? 'active' : ''}">
        <a class="page-link" href="#" onclick="goToPage(${i}); return false;">${i}</a>
      </li>
    `;
	}

	if (endPage < totalPages) {
		if (endPage < totalPages - 1) {
			html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
		}
		html += `
      <li class="page-item">
        <a class="page-link" href="#" onclick="goToPage(${totalPages}); return false;">${totalPages}</a>
      </li>
    `;
	}

	// 次へボタン
	html += `
    <li class="page-item ${page === totalPages ? 'disabled' : ''}">
      <a class="page-link" href="#" onclick="goToPage(${
				page + 1
			}); return false;" aria-label="次へ">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  `;

	paginationList.innerHTML = html;
}

// ページ移動
function goToPage(page) {
	if (page < 1 || page > totalPages || page === currentPage) {
		return;
	}
	currentPage = page;
	loadblogList();
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

// 新規投稿フォームを表示
function showNewblogForm() {
	isEditMode = false;
	formTitle.textContent = '新規投稿';
	blogIdInput.value = '';
	titleInput.value = '';
	textInput.value = '';
	blogForm.style.display = 'block';
	blogForm.scrollIntoView({ behavior: 'smooth' });
}

// 編集フォームを表示
function showEditblogForm(blog) {
	isEditMode = true;
	formTitle.textContent = 'ブログ編集';
	blogIdInput.value = blog.id;
	titleInput.value = blog.title;
	textInput.value = blog.text;
	blogForm.style.display = 'block';
	blogForm.scrollIntoView({ behavior: 'smooth' });
}

// フォームを非表示
function hideblogForm() {
	blogForm.style.display = 'none';
}

// フォーム送信処理
async function handleFormSubmit(e) {
	e.preventDefault();

	const title = titleInput.value.trim();
	const text = textInput.value.trim();

	if (!title || !text) {
		alert('タイトルと本文を入力してください');
		return;
	}

	try {
		const id = blogIdInput.value;
		const url = isEditMode ? `${API_BASE}/${id}` : API_BASE;
		const method = isEditMode ? 'PUT' : 'POST';

		const response = await fetch(url, {
			method: method,
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ title, text }),
		});

		if (!response.ok) {
			const errorData = await response.text();
			throw new Error(errorData || '投稿に失敗しました');
		}

		hideblogForm();
		loadblogList();
		alert(isEditMode ? 'ブログを更新しました' : 'ブログを投稿しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error submitting form:', error);
	}
}

// ユーティリティ関数
function escapeHtml(text) {
	const div = document.createElement('div');
	div.textContent = text;
	return div.innerHTML;
}

// 出演情報を取得
async function loadappearances() {
	try {
		const response = await fetch('/api/appearance');
		if (!response.ok) {
			throw new Error('出演情報の取得に失敗しました');
		}
		const apiResponse = await response.json();
		
		// ApiResponse.data から出演情報リストを取得
		const appearances = apiResponse.data || [];
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		displayappearances(appearances);
	} catch (error) {
		appearanceContent.innerHTML = `
      <div class="alert alert-warning" role="alert">
        <i class="bi bi-exclamation-triangle"></i> 出演情報を読み込めませんでした
      </div>
    `;
		console.error('Error loading appearances:', error);
	}
}

// 出演情報を表示
function displayappearances(appearances) {
	if (!appearances || appearances.length === 0) {
		appearanceContent.innerHTML = `
      <div class="text-muted">
        <i class="bi bi-calendar-x fs-4 d-block mb-2"></i>
        <p class="small mb-0">出演情報がありません</p>
      </div>
    `;
		return;
	}

	// 日付順にソート（最新のものから）
	const sortedappearances = [...appearances].sort((a, b) => {
		const dateA = new Date(a.date || a.getdate || 0);
		const dateB = new Date(b.date || b.getdate || 0);
		return dateB - dateA;
	});

	// 最新5件まで表示
	const recentappearances = sortedappearances.slice(0, 5);

	const html = recentappearances
		.map((appearance) => {
			const date = appearance.date || appearance.getdate || '';
			const formattedDate = formatappearanceDate(date);
			return `
        <div class="appearance-item mb-3 pb-3 border-bottom">
          <div class="d-flex justify-content-between align-items-start mb-2">
            <h6 class="fw-bold mb-0 small">${escapeHtml(appearance.title || '')}</h6>
          </div>
          <div class="text-muted">
            <i class="bi bi-calendar3 me-1"></i>
            <small>${formattedDate}</small>
          </div>
        </div>
      `;
		})
		.join('');

	appearanceContent.innerHTML = `
    <div class="appearance-list text-start">
      ${html}
    </div>
  `;
}

// 出演情報の日付をフォーマット
function formatappearanceDate(dateString) {
	if (!dateString) return '-';
	const date = new Date(dateString);
	return date.toLocaleDateString('ja-JP', {
		year: 'numeric',
		month: 'long',
		day: 'numeric',
	});
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
