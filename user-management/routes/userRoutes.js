const express = require("express");
const router = express.Router();
const User = require("../models/User");

// Tạo User
router.post("/", async (req, res) => {
  try {
    const user = new User(req.body);
    await user.save();
    res.status(201).json({ message: "User created successfully", user });
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// Lấy tất cả Users + tìm kiếm theo username, fullName, loginCount (gte, lte)
router.get("/", async (req, res) => {
  try {
    const { username, fullName, minLogin, maxLogin } = req.query;
    let query = { isDeleted: false };

    if (username) query.username = new RegExp(username, "i");
    if (fullName) query.fullName = new RegExp(fullName, "i");
    if (minLogin) query.loginCount = { ...query.loginCount, $gte: Number(minLogin) };
    if (maxLogin) query.loginCount = { ...query.loginCount, $lte: Number(maxLogin) };

    const users = await User.find(query).populate("role");
    res.json(users);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Lấy User theo ID
router.get("/:id", async (req, res) => {
  try {
    const user = await User.findById(req.params.id).populate("role");
    if (!user || user.isDeleted) return res.status(404).json({ message: "User not found" });
    res.json(user);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Lấy User theo Username
router.get("/username/:username", async (req, res) => {
  try {
    const user = await User.findOne({ username: req.params.username, isDeleted: false }).populate("role");
    if (!user) return res.status(404).json({ message: "User not found" });
    res.json(user);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Cập nhật User
router.put("/:id", async (req, res) => {
  try {
    const user = await User.findById(req.params.id);
    if (!user || user.isDeleted) return res.status(404).json({ message: "User not found" });

    Object.assign(user, req.body);
    await user.save();

    res.json({ message: "User updated successfully", user });
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// Xóa mềm User
router.delete("/:id", async (req, res) => {
  try {
    const user = await User.findByIdAndUpdate(req.params.id, { isDeleted: true }, { new: true });
    if (!user) return res.status(404).json({ message: "User not found" });

    res.json({ message: "User soft deleted", user });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});
// Xóa cứng User (Thêm mới)
router.delete("/hard/:id", async (req, res) => {
  try {
    const user = await User.findByIdAndDelete(req.params.id);
    if (!user) return res.status(404).json({ message: "User not found" });

    res.json({ message: "User hard deleted successfully", user });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});
// Kích hoạt tài khoản
router.post("/activate", async (req, res) => {
  try {
    const { email, username } = req.body;
    const user = await User.findOne({ email, username, isDeleted: false });

    if (!user) return res.status(404).json({ message: "User not found" });

    user.status = true;
    await user.save();

    res.json({ message: "Account activated successfully", user });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Gán Role cho User
router.put("/:id/role", async (req, res) => {
  try {
    const { role } = req.body;
    const user = await User.findById(req.params.id);
    
    if (!user || user.isDeleted) return res.status(404).json({ message: "User not found" });

    user.role = role;
    await user.save();

    res.json({ message: "Role updated successfully", user });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
