const mongoose = require('mongoose');
const Role = require('./models/roles');

mongoose.connect('mongodb://localhost:27017/Buoi5');

async function seedRoles() {
    try {
        await Role.deleteMany({}); // Xóa roles cũ để tránh trùng lặp
        const roles = await Role.insertMany([
            { name: "admin" },
            { name: "moderator" },
            { name: "user" }
        ]);
        console.log("✅ Đã thêm roles thành công:", roles);
    } catch (error) {
        console.error("❌ Lỗi khi thêm roles:", error);
    } finally {
        mongoose.connection.close();
    }
}

seedRoles();
