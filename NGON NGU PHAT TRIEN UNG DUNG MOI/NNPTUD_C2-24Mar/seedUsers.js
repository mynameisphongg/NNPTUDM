const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const User = require('./models/users');
const Role = require('./models/roles');

mongoose.connect('mongodb://localhost:27017/Buoi5');

async function seedUsers() {
    try {
        await User.deleteMany({});
        console.log("✅ Đã xóa toàn bộ user cũ");

        // Tìm ObjectId của roles
        const adminRole = await Role.findOne({ name: 'admin' });
        const modRole = await Role.findOne({ name: 'moderator' });
        const userRole = await Role.findOne({ name: 'user' });

        if (!adminRole || !modRole || !userRole) {
            throw new Error("❌ Chưa có roles trong database. Hãy chạy `node seedRoles.js` trước.");
        }

        // Danh sách user (3 user mỗi role)
        const users = [
            // Admin Users
            { username: "admin1", password: await bcrypt.hash("Admin@123", 10), email: "admin1@example.com", fullName: "Alice Admin", avatarUrl: "https://example.com/admin1.jpg", role: adminRole._id },
            { username: "admin2", password: await bcrypt.hash("Admin@123", 10), email: "admin2@example.com", fullName: "Bob Admin", avatarUrl: "https://example.com/admin2.jpg", role: adminRole._id },
            { username: "admin3", password: await bcrypt.hash("Admin@123", 10), email: "admin3@example.com", fullName: "Charlie Admin", avatarUrl: "https://example.com/admin3.jpg", role: adminRole._id },

            // Moderator Users
            { username: "mod1", password: await bcrypt.hash("Mod@123", 10), email: "mod1@example.com", fullName: "David Moderator", avatarUrl: "https://example.com/mod1.jpg", role: modRole._id },
            { username: "mod2", password: await bcrypt.hash("Mod@123", 10), email: "mod2@example.com", fullName: "Eve Moderator", avatarUrl: "https://example.com/mod2.jpg", role: modRole._id },
            { username: "mod3", password: await bcrypt.hash("Mod@123", 10), email: "mod3@example.com", fullName: "Frank Moderator", avatarUrl: "https://example.com/mod3.jpg", role: modRole._id },

            // Regular Users
            { username: "user1", password: await bcrypt.hash("User@123", 10), email: "user1@example.com", fullName: "Grace User", avatarUrl: "https://example.com/user1.jpg", role: userRole._id },
            { username: "user2", password: await bcrypt.hash("User@123", 10), email: "user2@example.com", fullName: "Hank User", avatarUrl: "https://example.com/user2.jpg", role: userRole._id },
            { username: "user3", password: await bcrypt.hash("User@123", 10), email: "user3@example.com", fullName: "Ivy User", avatarUrl: "https://example.com/user3.jpg", role: userRole._id }
        ];

        await User.insertMany(users);
        console.log("✅ Seed users added successfully!");
    } catch (error) {
        console.error("❌ Error seeding users:", error);
    } finally {
        mongoose.connection.close();
    }
}

// Chạy hàm seed
seedUsers();
