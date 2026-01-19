// API ベースURL
const APPEARANCE_API_BASE = '/api/appearance';

// DOM要素
const loading = document.getElementById('loading');
const appearanceContent = document.getElementById('appearanceContent');
const detailTitle = document.getElementById('detailTitle');
const detailMeta = document.getElementById('detailMeta');
const detailText = document.getElementById('detailText');
const appearanceInfoDisplay = document.getElementById('appearanceInfoDisplay');
const appearanceInfoEdit = document.getElementById('appearanceInfoEdit');
const editBtn = document.getElementById('editBtn');
const deleteBtn = document.getElementById('deleteBtn');
const appearanceEditForm = document.getElementById('appearanceEditForm');
const saveAppearanceBtn = document.getElementById('saveAppearanceBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const editTitle = document.getElementById('editTitle');
const editDate = document.getElementById('editDate');
const editText = document.getElementById('editText');

// 現在の出演情報IDとデータを保持
let currentAppearanceId = null;
let currentAppearance = null;

// URLパラメータからIDを取得
function getAppearanceIdFromUrl() {
	const params = new URLSearchParams(window.location.search);
	return params.get('id');
}

// 初期化
document.addEventListener('DOMContentLoaded', async () => {
	const id = getAppearanceIdFromUrl();
	if (!id) {
		alert('出演情報IDが指定されていません');
		window.location.href = 'index.html';
		return;
	}

	currentAppearanceId = parseInt(id);
	await loadAppearanceDetail(id);

	// イベントリスナー
	editBtn.addEventListener('click', showEditForm);
	deleteBtn.addEventListener('click', handleDelete);
	cancelEditBtn.addEventListener('click', hideEditForm);
	appearanceEditForm.addEventListener('submit', handleAppearanceUpdate);
});

// 出演情報詳細を取得
async function loadAppearanceDetail(id) {
	try {
		loading.style.display = 'block';
		appearanceContent.style.display = 'none';

		const response = await fetch(`${APPEARANCE_API_BASE}/${id}`);

		if (!response.ok) {
			if (response.status === 404) {
				alert('出演情報が見つかりませんでした');
				window.location.href = 'index.html';
				return;
			}
			throw new Error('出演情報の取得に失敗しました');
		}

		const apiResponse = await response.json();

		// ApiResponse.data から出演情報データを取得
		const appearance = apiResponse.data;

		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		if (!appearance) {
			alert('出演情報が見つかりませんでした');
			window.location.href = 'index.html';
			return;
		}

		displayAppearanceDetail(appearance);
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error loading appearance detail:', error);
		window.location.href = 'index.html';
	} finally {
		loading.style.display = 'none';
	}
}

// 出演情報詳細を表示
function displayAppearanceDetail(appearance) {
	// 現在の出演情報を保存
	currentAppearance = appearance;
	
	detailTitle.textContent = appearance.title || '出演情報';
	
	// 日付をフォーマット
	const date = appearance.date || '';
	const formattedDate = formatAppearanceDate(date);
	
	detailMeta.innerHTML = `
    <div class="mb-2">
      <i class="bi bi-calendar3"></i> 日付: ${formattedDate}
    </div>
  `;
	
	// 詳細テキストを表示
	detailText.textContent = appearance.text || '詳細情報がありません';
	
	loading.style.display = 'none';
	appearanceContent.style.display = 'block';
}

// 編集フォームを表示
function showEditForm() {
	if (!currentAppearance) {
		alert('出演情報が読み込まれていません');
		return;
	}
	
	// 現在の値をフォームに設定
	editTitle.value = currentAppearance.title || '';
	
	// 日付をYYYY-MM-DD形式に変換
	const date = currentAppearance.date || '';
	if (date) {
		const dateObj = new Date(date);
		const year = dateObj.getFullYear();
		const month = String(dateObj.getMonth() + 1).padStart(2, '0');
		const day = String(dateObj.getDate()).padStart(2, '0');
		editDate.value = `${year}-${month}-${day}`;
	} else {
		editDate.value = '';
	}
	
	editText.value = currentAppearance.text || '';
	
	// 表示/非表示を切り替え
	appearanceInfoDisplay.style.display = 'none';
	appearanceInfoEdit.style.display = 'block';
	editBtn.style.display = 'none';
	deleteBtn.style.display = 'none';
}

// 編集フォームを非表示
function hideEditForm() {
	appearanceInfoDisplay.style.display = 'block';
	appearanceInfoEdit.style.display = 'none';
	editBtn.style.display = 'block';
	deleteBtn.style.display = 'block';
}

// 出演情報を更新
async function handleAppearanceUpdate(e) {
	e.preventDefault();
	
	try {
		saveAppearanceBtn.disabled = true;
		saveAppearanceBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>保存中...';
		
		const appearanceData = {
			date: editDate.value,
			title: editTitle.value.trim(),
			text: editText.value.trim()
		};
		
		// バリデーション
		if (!appearanceData.title || !appearanceData.date || !appearanceData.text) {
			alert('すべての項目を入力してください');
			saveAppearanceBtn.disabled = false;
			saveAppearanceBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
			return;
		}
		
		// エンドポイント: PUT /api/appearance/{id}
		const response = await fetch(`${APPEARANCE_API_BASE}/${currentAppearanceId}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(appearanceData),
		});
		
		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || '出演情報の更新に失敗しました');
		}
		
		const apiResponse = await response.json();
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		// 更新後の出演情報を表示
		const updatedAppearance = apiResponse.data;
		if (updatedAppearance) {
			displayAppearanceDetail(updatedAppearance);
			hideEditForm();
			alert('出演情報を更新しました');
		} else {
			// 更新後の情報が取得できない場合は、再度読み込み
			await loadAppearanceDetail(currentAppearanceId);
			hideEditForm();
			alert('出演情報を更新しました');
		}
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating appearance:', error);
	} finally {
		saveAppearanceBtn.disabled = false;
		saveAppearanceBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
	}
}

// 出演情報を削除
async function handleDelete() {
	if (!confirm('この出演情報を削除しますか？')) {
		return;
	}
	
	try {
		deleteBtn.disabled = true;
		deleteBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>削除中...';
		
		const response = await fetch(`${APPEARANCE_API_BASE}/${currentAppearanceId}`, {
			method: 'DELETE',
		});
		
		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || '削除に失敗しました');
		}
		
		alert('出演情報を削除しました');
		window.location.href = 'index.html';
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error deleting appearance:', error);
	} finally {
		deleteBtn.disabled = false;
		deleteBtn.innerHTML = '<i class="bi bi-trash"></i> 削除';
	}
}

// 出演情報の日付をフォーマット
function formatAppearanceDate(dateString) {
	if (!dateString) return '-';
	const date = new Date(dateString);
	return date.toLocaleDateString('ja-JP', {
		year: 'numeric',
		month: 'long',
		day: 'numeric',
	});
}

