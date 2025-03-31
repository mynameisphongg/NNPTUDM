const express = require('express');
const Menu = require('../models/Menu');
const router = express.Router();

// Tạo menu mới
router.post('/', async (req, res) => {
  try {
    const { text, url, parent } = req.body;
    const menu = new Menu({ text, url, parent: parent || null });
    await menu.save();
    res.status(201).json(menu);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Lấy danh sách menu theo bậc cha-con
router.get('/', async (req, res) => {
  try {
    const menus = await Menu.find().lean();
    const menuMap = {};

    // Tạo danh sách các menu theo ID
    menus.forEach(menu => menuMap[menu._id] = { ...menu, children: [] });

    // Gán menu con vào danh sách cha
    const rootMenus = [];
    menus.forEach(menu => {
      if (menu.parent) {
        menuMap[menu.parent]?.children.push(menuMap[menu._id]);
      } else {
        rootMenus.push(menuMap[menu._id]);
      }
    });

    res.json(rootMenus);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Cập nhật menu
router.put('/:id', async (req, res) => {
  try {
    const { text, url, parent } = req.body;
    const menu = await Menu.findByIdAndUpdate(req.params.id, { text, url, parent }, { new: true });
    if (!menu) return res.status(404).json({ message: 'Menu not found' });
    res.json(menu);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Xóa menu
router.delete('/:id', async (req, res) => {
  try {
    await Menu.findByIdAndDelete(req.params.id);
    res.json({ message: 'Menu deleted' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
