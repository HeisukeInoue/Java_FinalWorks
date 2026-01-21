// API ベースURL
const API_BASE = '/api/profile';

// URLパラメータからIDを取得
const urlParams = new URLSearchParams(window.location.search);
const profileId = urlParams.get('id') || 1; // デフォルトは1

// DOM要素
const loading = document.getElementById('loading');
const profileDisplay = document.getElementById('profileDisplay');
const profileImage = document.getElementById('profileImage');
const imageUpload = document.getElementById('imageUpload');
const editBtn = document.getElementById('editBtn');
const cancelBtn = document.getElementById('cancelBtn');
const profileForm = document.getElementById('profileForm');
const profileInfo = document.getElementById('profileInfo');
const editForm = document.getElementById('editForm');

// 表示用要素
const displayName = document.getElementById('displayName');
const displayAge = document.getElementById('displayAge');
const displayFrom = document.getElementById('displayFrom');
const displayHeight = document.getElementById('displayHeight');
const displayHobby = document.getElementById('displayHobby');

// 編集用要素
const editName = document.getElementById('editName');
const editAge = document.getElementById('editAge');
const editFrom = document.getElementById('editFrom');
const editHeight = document.getElementById('editHeight');
const editHobby = document.getElementById('editHobby');

// 初期化
document.addEventListener('DOMContentLoaded', () => {
	loadProfile();

	// イベントリスナー
	editBtn.addEventListener('click', showEditForm);
	cancelBtn.addEventListener('click', hideEditForm);
	profileForm.addEventListener('submit', handleProfileUpdate);
	imageUpload.addEventListener('change', handleImageUpload);
});

// プロフィールを取得
async function loadProfile() {
	try {
		loading.style.display = 'block';
		profileDisplay.style.display = 'none';

		const response = await fetch(`${API_BASE}/${profileId}`);

		if (!response.ok) {
			if (response.status === 404) {
				alert('プロフィールが見つかりませんでした');
				window.location.href = 'index.html';
				return;
			}
			throw new Error('プロフィールの取得に失敗しました');
		}

		const apiResponse = await response.json();

		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}

		const profile = apiResponse.data;
		displayProfile(profile);

		loading.style.display = 'none';
		profileDisplay.style.display = 'block';
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error loading profile:', error);
		loading.innerHTML = `
			<div class="alert alert-danger" role="alert">
				<i class="bi bi-exclamation-triangle"></i> ${escapeHtml(error.message)}
			</div>
		`;
	}
}

// プロフィールを表示
function displayProfile(profile) {
	// 画像を表示
	if (profile.imageLink) {
		profileImage.src = profile.imageLink + '?t=' + Date.now(); // キャッシュ回避
	} else {
		profileImage.src = 'data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'300\' height=\'300\'%3E%3Crect fill=\'%23ddd\' width=\'300\' height=\'300\'/%3E%3Ctext fill=\'%23999\' x=\'50%25\' y=\'50%25\' text-anchor=\'middle\' dy=\'.3em\'%3E画像なし%3C/text%3E%3C/svg%3E';
	}

	// プロフィール情報を表示
	displayName.textContent = profile.name || '-';
	displayAge.textContent = profile.age ? `${profile.age}歳` : '-';
	displayFrom.textContent = profile.from || '-';
	displayHeight.textContent = profile.height ? `${profile.height}cm` : '-';
	displayHobby.textContent = profile.hobby || '-';

	// 編集フォームに値を設定
	editName.value = profile.name || '';
	editAge.value = profile.age || '';
	editFrom.value = profile.from || '';
	editHeight.value = profile.height || '';
	editHobby.value = profile.hobby || '';
}

// 編集フォームを表示
function showEditForm() {
	profileInfo.style.display = 'none';
	editForm.style.display = 'block';
	editBtn.style.display = 'none';
}

// 編集フォームを非表示
function hideEditForm() {
	profileInfo.style.display = 'block';
	editForm.style.display = 'none';
	editBtn.style.display = 'block';
}

// プロフィール情報を更新
async function handleProfileUpdate(e) {
	e.preventDefault();

	const name = editName.value.trim();
	const age = parseInt(editAge.value);
	const from = editFrom.value.trim();
	const height = parseInt(editHeight.value);
	const hobby = editHobby.value.trim();

	if (!name || !from || !hobby || isNaN(age) || isNaN(height)) {
		alert('すべての項目を正しく入力してください');
		return;
	}

	try {
		const response = await fetch(`${API_BASE}/${profileId}/info`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				image_link: profileImage.src, // 現在の画像URLを保持
				name: name,
				age: age,
				from: from,
				height: height,
				hobby: hobby,
			}),
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || '更新に失敗しました');
		}

		hideEditForm();
		await loadProfile();
		alert('プロフィールを更新しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating profile:', error);
	}
}

// 画像をアップロード
async function handleImageUpload(e) {
	const file = e.target.files[0];
	if (!file) {
		return;
	}

	// ファイルサイズチェック（10MB）
	if (file.size > 10 * 1024 * 1024) {
		alert('ファイルサイズは10MB以下にしてください');
		imageUpload.value = '';
		return;
	}

	// ファイルタイプチェック
	if (!file.type.startsWith('image/')) {
		alert('画像ファイルを選択してください');
		imageUpload.value = '';
		return;
	}

	try {
		const formData = new FormData();
		formData.append('image_link', file);

		const response = await fetch(`${API_BASE}/${profileId}/image`, {
			method: 'PUT',
			body: formData,
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.error || '画像のアップロードに失敗しました');
		}

		const apiResponse = await response.json();
		const updatedProfile = apiResponse.data;

		// 画像を即座に更新（キャッシュ回避）
		if (updatedProfile.imageLink) {
			profileImage.src = updatedProfile.imageLink + '?t=' + Date.now();
		}

		imageUpload.value = '';
		alert('画像を更新しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error uploading image:', error);
		imageUpload.value = '';
	}
}

// ユーティリティ関数
function escapeHtml(text) {
	const div = document.createElement('div');
	div.textContent = text;
	return div.innerHTML;
}
