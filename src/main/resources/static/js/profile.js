// API ベースURL
const PROFILE_API_BASE = '/api/profile';

// DOM要素
const loading = document.getElementById('loading');
const profileContent = document.getElementById('profileContent');
const profileImageContainer = document.getElementById('profileImageContainer');
const profileName = document.getElementById('profileName');
const profileInfo = document.getElementById('profileInfo');
const profileInfoDisplay = document.getElementById('profileInfoDisplay');
const profileInfoEdit = document.getElementById('profileInfoEdit');
const uploadImageBtn = document.getElementById('uploadImageBtn');
const imageFileInput = document.getElementById('imageFileInput');
const editProfileBtn = document.getElementById('editProfileBtn');
const profileEditForm = document.getElementById('profileEditForm');
const saveProfileBtn = document.getElementById('saveProfileBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const editName = document.getElementById('editName');
const editAge = document.getElementById('editAge');
const editFrom = document.getElementById('editFrom');
const editHeight = document.getElementById('editHeight');
const editHobby = document.getElementById('editHobby');

// 現在のプロフィール情報を保持
let currentProfile = null;

// 現在のタレントID（デフォルトは1、URLパラメータから取得可能）
let currentTalentId = 1;

// URLパラメータからIDを取得
function getTalentIdFromUrl() {
	const params = new URLSearchParams(window.location.search);
	return params.get('id') || 1;
}

// 初期化
document.addEventListener('DOMContentLoaded', async () => {
	currentTalentId = parseInt(getTalentIdFromUrl());
	
	await loadProfile();
	
	// イベントリスナー
	uploadImageBtn.addEventListener('click', () => {
		imageFileInput.click();
	});
	
	imageFileInput.addEventListener('change', async (e) => {
		if (e.target.files && e.target.files[0]) {
			await uploadProfileImage(e.target.files[0]);
		}
	});
	
	// プロフィール編集関連のイベントリスナー
	editProfileBtn.addEventListener('click', showEditForm);
	cancelEditBtn.addEventListener('click', hideEditForm);
	profileEditForm.addEventListener('submit', handleProfileUpdate);
});

// プロフィール情報を取得
async function loadProfile() {
	try {
		loading.style.display = 'block';
		profileContent.style.display = 'none';
		
		// プロフィール全体の情報を取得
		const response = await fetch(`${PROFILE_API_BASE}/${currentTalentId}`);
		
		if (!response.ok) {
			if (response.status === 404) {
				alert('プロフィール情報が見つかりませんでした');
				return;
			}
			throw new Error('プロフィール情報の取得に失敗しました');
		}
		
		const apiResponse = await response.json();
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		const profile = apiResponse.data;
		if (!profile) {
			throw new Error('プロフィール情報が取得できませんでした');
		}
		
		// プロフィール情報を表示
		displayProfile(profile);
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error loading profile:', error);
	} finally {
		loading.style.display = 'none';
	}
}

// プロフィール画像URLを取得（画像アップロード後の更新用）
async function loadProfileImage() {
	try {
		const response = await fetch(`${PROFILE_API_BASE}/${currentTalentId}`);
		
		if (!response.ok) {
			if (response.status === 404) {
				return null; // 画像が設定されていない
			}
			throw new Error('プロフィール画像の取得に失敗しました');
		}
		
		const apiResponse = await response.json();
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		// ProfileオブジェクトからimageLinkを取得
		if (apiResponse.data && apiResponse.data.imageLink) {
			return apiResponse.data.imageLink;
		}
		
		return null;
	} catch (error) {
		console.error('Error loading profile image:', error);
		return null;
	}
}

// プロフィール情報を表示
function displayProfile(profile) {
	// 現在のプロフィール情報を保存
	currentProfile = profile;
	
	// プロフィール画像を表示
	const imageUrl = profile.imageLink || null;
	displayProfileImage(imageUrl);
	
	// プロフィール名を表示
	profileName.textContent = profile.name || 'タレントプロフィール';
	
	// プロフィール情報を表示
	displayProfileInfo(profile);
	
	profileContent.style.display = 'block';
}

// プロフィール情報（名前以外）を表示
function displayProfileInfo(profile) {
	const infoHtml = `
		<div class="row g-3">
			<div class="col-12">
				<div class="d-flex align-items-center mb-3">
					<i class="bi bi-person-fill text-primary me-2 fs-5"></i>
					<div>
						<strong class="text-muted small">名前</strong>
						<p class="mb-0 fw-bold">${escapeHtml(profile.name || '-')}</p>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="d-flex align-items-center mb-3">
					<i class="bi bi-calendar3 text-primary me-2 fs-5"></i>
					<div>
						<strong class="text-muted small">年齢</strong>
						<p class="mb-0">${profile.age ? profile.age + '歳' : '-'}</p>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="d-flex align-items-center mb-3">
					<i class="bi bi-geo-alt-fill text-primary me-2 fs-5"></i>
					<div>
						<strong class="text-muted small">出身地</strong>
						<p class="mb-0">${escapeHtml(profile.from || '-')}</p>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="d-flex align-items-center mb-3">
					<i class="bi bi-rulers text-primary me-2 fs-5"></i>
					<div>
						<strong class="text-muted small">身長</strong>
						<p class="mb-0">${profile.height ? profile.height + 'cm' : '-'}</p>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="d-flex align-items-center mb-3">
					<i class="bi bi-heart-fill text-primary me-2 fs-5"></i>
					<div>
						<strong class="text-muted small">趣味</strong>
						<p class="mb-0">${escapeHtml(profile.hobby || '-')}</p>
					</div>
				</div>
			</div>
		</div>
	`;
	
	profileInfo.innerHTML = infoHtml;
}

// プロフィール画像を表示
function displayProfileImage(imageUrl) {
	if (imageUrl) {
		profileImageContainer.innerHTML = `
			<img src="${escapeHtml(imageUrl)}" 
			     alt="プロフィール画像" 
			     class="img-fluid rounded-circle mb-3"
			     id="profileImage"
			     style="max-width: 250px; height: auto; border: 3px solid #dee2e6; object-fit: cover;">
		`;
	} else {
		profileImageContainer.innerHTML = `
			<div class="text-muted mb-3">
				<i class="bi bi-image fs-1 d-block mb-2"></i>
				<p class="small mb-0">プロフィール画像が設定されていません</p>
			</div>
		`;
	}
}

// プロフィール画像をアップロード
async function uploadProfileImage(file) {
	try {
		// ファイルサイズチェック（10MB制限）
		if (file.size > 10 * 1024 * 1024) {
			alert('ファイルサイズは10MB以下にしてください');
			return;
		}
		
		// 画像ファイルかチェック
		if (!file.type.startsWith('image/')) {
			alert('画像ファイルを選択してください');
			return;
		}
		
		// アップロード中の表示
		uploadImageBtn.disabled = true;
		uploadImageBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>アップロード中...';
		
		const formData = new FormData();
		// バックエンドの@RequestPart("image_link")に合わせてキー名を変更
		formData.append('image_link', file);
		
		// エンドポイント: PUT /api/profile/{id}/image
		const response = await fetch(`${PROFILE_API_BASE}/${currentTalentId}/image`, {
			method: 'PUT',
			body: formData
		});
		
		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || 'アップロードに失敗しました');
		}
		
		const apiResponse = await response.json();
		
		// レスポンスはProfileオブジェクトなので、imageLinkを取得
		let updatedUrl = null;
		if (apiResponse.data && apiResponse.data.imageLink) {
			updatedUrl = apiResponse.data.imageLink;
		}
		
		// アップロード成功後、即座に画像を更新（キャッシュ回避のためタイムスタンプを追加）
		if (updatedUrl) {
			const cacheBuster = new Date().getTime();
			const urlWithCache = updatedUrl + (updatedUrl.includes('?') ? '&' : '?') + 't=' + cacheBuster;
			displayProfileImage(urlWithCache);
		} else {
			// URLが取得できない場合は、再度プロフィール情報を取得
			const imageUrl = await loadProfileImage();
			if (imageUrl) {
				const cacheBuster = new Date().getTime();
				const urlWithCache = imageUrl + (imageUrl.includes('?') ? '&' : '?') + 't=' + cacheBuster;
				displayProfileImage(urlWithCache);
			}
		}
		
		alert('プロフィール画像を更新しました');
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error uploading profile image:', error);
	} finally {
		// ボタンを元に戻す
		uploadImageBtn.disabled = false;
		uploadImageBtn.innerHTML = '<i class="bi bi-upload"></i> 画像をアップロード';
		// ファイル入力をリセット
		imageFileInput.value = '';
	}
}

// 編集フォームを表示
function showEditForm() {
	if (!currentProfile) {
		alert('プロフィール情報が読み込まれていません');
		return;
	}
	
	// 現在の値をフォームに設定
	editName.value = currentProfile.name || '';
	editAge.value = currentProfile.age || '';
	editFrom.value = currentProfile.from || '';
	editHeight.value = currentProfile.height || '';
	editHobby.value = currentProfile.hobby || '';
	
	// 表示/非表示を切り替え
	profileInfoDisplay.style.display = 'none';
	profileInfoEdit.style.display = 'block';
	editProfileBtn.style.display = 'none';
}

// 編集フォームを非表示
function hideEditForm() {
	profileInfoDisplay.style.display = 'block';
	profileInfoEdit.style.display = 'none';
	editProfileBtn.style.display = 'block';
}

// プロフィール情報を更新
async function handleProfileUpdate(e) {
	e.preventDefault();
	
	try {
		saveProfileBtn.disabled = true;
		saveProfileBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>保存中...';
		
		const profileData = {
			image_link: currentProfile.imageLink || '',
			name: editName.value.trim(),
			age: parseInt(editAge.value) || 0,
			from: editFrom.value.trim(),
			height: parseInt(editHeight.value) || 0,
			hobby: editHobby.value.trim()
		};
		
		// バリデーション
		if (!profileData.name || !profileData.from || !profileData.hobby) {
			alert('すべての項目を入力してください');
			saveProfileBtn.disabled = false;
			saveProfileBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
			return;
		}
		
		// エンドポイント: PUT /api/profile/{id}/info
		const response = await fetch(`${PROFILE_API_BASE}/${currentTalentId}/info`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(profileData),
		});
		
		if (!response.ok) {
			const apiResponse = await response.json();
			throw new Error(apiResponse.error || 'プロフィール情報の更新に失敗しました');
		}
		
		const apiResponse = await response.json();
		
		if (apiResponse.error) {
			throw new Error(apiResponse.error);
		}
		
		// 更新後のプロフィール情報を表示
		const updatedProfile = apiResponse.data;
		if (updatedProfile) {
			displayProfile(updatedProfile);
			hideEditForm();
			alert('プロフィール情報を更新しました');
		} else {
			// 更新後の情報が取得できない場合は、再度読み込み
			await loadProfile();
			hideEditForm();
			alert('プロフィール情報を更新しました');
		}
	} catch (error) {
		alert(`エラー: ${error.message}`);
		console.error('Error updating profile:', error);
	} finally {
		saveProfileBtn.disabled = false;
		saveProfileBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
	}
}

// HTMLエスケープ
function escapeHtml(text) {
	if (!text) return '';
	const div = document.createElement('div');
	div.textContent = text;
	return div.innerHTML;
}

