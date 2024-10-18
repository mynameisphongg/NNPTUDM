<script>
    document.querySelector('form').onsubmit = function() {
        const content = document.querySelector('textarea[name="content"]').value;
        if (!content.trim()) {
            alert("Nội dung không được để trống!");
            return false; // Ngăn không cho gửi biểu mẫu
        }
        return true; // Tiếp tục gửi
    };
</script>
