const { fail } = require('assert');
var express = require('express');
var router = express.Router();
let categorySchema = require('../models/categories');
let BuildQueies = require('../Utils/BuildQuery');

// Lấy danh sách danh mục (KHÔNG lấy danh mục đã bị xóa)
router.get('/', async function(req, res, next) {
  let queries = req.query;
  let categories = await categorySchema.find({
    ...BuildQueies.QueryProduct(queries),
    isDeleted: false  // Chỉ lấy danh mục chưa bị xóa
  });
  res.send(categories);
});

// Lấy danh mục theo ID
router.get('/:id', async function(req, res, next) {
  try {
    let category = await categorySchema.findById(req.params.id);
    if (!category || category.isDeleted) {
      return res.status(404).send({ success: false, message: "Danh mục không tồn tại" });
    }
    res.status(200).send({ success: true, data: category });
  } catch (error) {
    res.status(404).send({ success: false, message: error.message });
  }
});

// Thêm danh mục mới
router.post('/', async function(req, res, next) {
  let body = req.body;
  let newCategory = new categorySchema({
    categoryName: body.categoryName,
    description: body.description
  });
  await newCategory.save();
  res.send(newCategory);
});

// Cập nhật danh mục
router.put('/:id', async function(req, res, next) {
  try {
    let body = req.body;
    let category = await categorySchema.findByIdAndUpdate(req.params.id, body, { new: true });
    if (!category || category.isDeleted) {
      return res.status(404).send({ success: false, message: "Danh mục không tồn tại" });
    }
    res.status(200).send({ success: true, data: category });
  } catch (error) {
    res.status(404).send({ success: false, message: error.message });
  }
});

// Xóa danh mục (xóa mềm)
router.delete('/:id', async function(req, res, next) {
  try {
    let category = await categorySchema.findByIdAndUpdate(req.params.id, { isDeleted: true }, { new: true });
    if (!category) {
      return res.status(404).send({ success: false, message: "Danh mục không tồn tại" });
    }
    res.status(200).send({ success: true, message: "Danh mục đã được xóa mềm", data: category });
  } catch (error) {
    res.status(500).send({ success: false, message: error.message });
  }
});

module.exports = router;
