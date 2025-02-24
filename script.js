let data = {
    posts: [],
    authors: []
};

// Tải dữ liệu từ GitHub JSON file
async function loadData() {
    try {
        const response = await fetch('https://raw.githubusercontent.com/nguyenthanhtunghutechsg/NNPTUD_C2/24Feb/newdb.json');
        data = await response.json();

        // Kiểm tra nếu đã có dữ liệu trong localStorage, ưu tiên lấy từ đó
        const storedPosts = localStorage.getItem('posts');
        if (storedPosts) {
            data.posts = JSON.parse(storedPosts);
        }

        renderPosts();
        renderDeletedPosts();
        renderAuthors();
    } catch (error) {
        console.error('Lỗi tải dữ liệu:', error);
    }
}

// Render danh sách bài viết (chưa bị xóa)
function renderPosts() {
    const postList = document.getElementById('postList');
    postList.innerHTML = '';

    data.posts.forEach((post, index) => {
        if (!post.deleted) {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${post.title}</td>
                <td>${post.author}</td>
                <td>${post.views}</td>
                <td>
                    <button class="btn btn-warning btn-sm" onclick="editPost(${index})">Sửa</button>
                    <button class="btn btn-danger btn-sm" onclick="deletePost(${index})">Xóa</button>
                </td>
            `;
            postList.appendChild(row);
        }
    });

    localStorage.setItem('posts', JSON.stringify(data.posts));
}

// Render danh sách bài viết đã xóa
function renderDeletedPosts() {
    const deletedPostList = document.getElementById('deletedPostList');
    deletedPostList.innerHTML = '';

    data.posts.forEach((post, index) => {
        if (post.deleted) {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${post.title}</td>
                <td>${post.author}</td>
                <td>${post.views}</td>
                <td>
                    <button class="btn btn-success btn-sm" onclick="restorePost(${index})">Khôi phục</button>
                </td>
            `;
            deletedPostList.appendChild(row);
        }
    });

    localStorage.setItem('posts', JSON.stringify(data.posts));
}

// Render danh sách tác giả
function renderAuthors() {
    const authorDropdown = document.getElementById('authorDropdown');
    authorDropdown.innerHTML = '';

    data.authors.forEach(author => {
        author.postCount = data.posts.filter(post => post.author === author.name && !post.deleted).length;

        const option = document.createElement('option');
        option.value = author.name;
        option.textContent = `${author.name} (${author.postCount} bài viết)`;
        authorDropdown.appendChild(option);
    });
}

// Thêm hoặc cập nhật bài viết
function addOrUpdatePost() {
    const title = document.getElementById('postTitle').value;
    const author = document.getElementById('authorDropdown').value;
    const views = parseInt(document.getElementById('postViews').value) || 0;
    const editId = document.getElementById('editId').value;

    if (!title || !author) {
        alert("Vui lòng nhập tiêu đề và chọn tác giả!");
        return;
    }

    if (editId) {
        data.posts[editId].title = title;
        data.posts[editId].author = author;
        data.posts[editId].views = views;
    } else {
        const newPost = {
            id: Date.now().toString(),
            title,
            views,
            author,
            isPublished: false,
            deleted: false
        };
        data.posts.push(newPost);
    }

    resetForm();
    renderPosts();
    renderAuthors();
}

// Xóa mềm bài viết
function deletePost(index) {
    if (confirm("Bạn có chắc muốn xóa bài viết này không?")) {
        data.posts[index].deleted = true;
        renderPosts();
        renderDeletedPosts();
        renderAuthors();
    }
}

// Khôi phục bài viết đã xóa
function restorePost(index) {
    if (confirm("Bạn có muốn khôi phục bài viết này không?")) {
        data.posts[index].deleted = false;
        renderPosts();
        renderDeletedPosts();
        renderAuthors();
    }
}

// Chỉnh sửa bài viết
function editPost(index) {
    document.getElementById('postTitle').value = data.posts[index].title;
    document.getElementById('authorDropdown').value = data.posts[index].author;
    document.getElementById('postViews').value = data.posts[index].views;
    document.getElementById('editId').value = index;
}

// Reset form nhập liệu
function resetForm() {
    document.getElementById('postTitle').value = '';
    document.getElementById('postViews').value = '';
    document.getElementById('authorDropdown').selectedIndex = 0;
    document.getElementById('editId').value = '';
}

window.onload = loadData;
