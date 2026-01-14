// API ベースURL
const API_BASE = "/api/blogs";

// DOM要素
const loading = document.getElementById("loading");
const blogContent = document.getElementById("blogContent");
const detailTitle = document.getElementById("detailTitle");
const detailMeta = document.getElementById("detailMeta");
const detailText = document.getElementById("detailText");
const editBtn = document.getElementById("editBtn");
const deleteBtn = document.getElementById("deleteBtn");

// URLパラメータからIDを取得
function getBlogIdFromUrl() {
  const params = new URLSearchParams(window.location.search);
  return params.get("id");
}

// 初期化
document.addEventListener("DOMContentLoaded", async () => {
  const id = getBlogIdFromUrl();
  if (!id) {
    alert("ブログIDが指定されていません");
    window.location.href = "index.html";
    return;
  }

  await loadBlogDetail(id);

  // イベントリスナー
  editBtn.addEventListener("click", () => {
    window.location.href = `index.html?edit=${id}`;
  });

  deleteBtn.addEventListener("click", async () => {
    if (confirm("このブログを削除しますか？")) {
      await deleteBlog(id);
    }
  });
});

// ブログ詳細を取得
async function loadBlogDetail(id) {
  try {
    loading.style.display = "block";
    blogContent.style.display = "none";

    const response = await fetch(`${API_BASE}/${id}`);

    if (!response.ok) {
      if (response.status === 404) {
        alert("ブログが見つかりませんでした");
        window.location.href = "index.html";
        return;
      }
      throw new Error("ブログの取得に失敗しました");
    }

    const blog = await response.json();
    displayBlogDetail(blog);
  } catch (error) {
    alert(`エラー: ${error.message}`);
    console.error("Error loading blog detail:", error);
    window.location.href = "index.html";
  } finally {
    loading.style.display = "none";
  }
}

// ブログ詳細を表示
function displayBlogDetail(blog) {
  detailTitle.textContent = blog.title;
  detailMeta.innerHTML = `
    <div class="mb-2">
      <i class="bi bi-calendar3"></i> 作成日: ${formatDate(blog.createdAt)}
    </div>
    ${
      blog.updatedAt
        ? `<div><i class="bi bi-calendar-check"></i> 更新日: ${formatDate(
            blog.updatedAt
          )}</div>`
        : ""
    }
  `;
  detailText.textContent = blog.text;
  loading.style.display = "none";
  blogContent.style.display = "block";
}

// ブログを削除
async function deleteBlog(id) {
  try {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      throw new Error("削除に失敗しました");
    }

    alert("ブログを削除しました");
    window.location.href = "index.html";
  } catch (error) {
    alert(`エラー: ${error.message}`);
    console.error("Error deleting blog:", error);
  }
}

// ユーティリティ関数
function formatDate(dateString) {
  if (!dateString) return "-";
  const date = new Date(dateString);
  return date.toLocaleString("ja-JP", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
}
