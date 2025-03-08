const { fail } = require('assert');
var express = require('express');
var router = express.Router();
let productSchema = require('../models/products');
let BuildQueies = require('../Utils/BuildQuery');

// Lấy danh sách sản phẩm (KHÔNG lấy sản phẩm đã xóa)
router.get('/', async function(req, res, next) {
  let queries = req.query;
  let products = await productSchema.find({
    ...BuildQueies.QueryProduct(queries),
    isDeleted: false  // Chỉ lấy sản phẩm chưa bị xóa
  });
  res.send(products);
});

// Lấy sản phẩm theo ID
router.get('/:id', async function(req, res, next) {
  try {
    let product = await productSchema.findById(req.params.id);
    if (!product || product.isDeleted) {
      return res.status(404).send({ success: false, message: "Sản phẩm không tồn tại" });
    }
    res.status(200).send({ success: true, data: product });
  } catch (error) {
    res.status(404).send({ success: false, message: error.message });
  }
});

// Thêm sản phẩm mới
router.post('/', async function(req, res, next) {
  let body = req.body;
  let newProduct = new productSchema({
    productName: body.productName,
    price: body.price,
    quantity: body.quantity,
    categoryID: body.category
  });
  await newProduct.save();
  res.send(newProduct);
});

// Cập nhật sản phẩm
router.put('/:id', async function(req, res, next) {
  try {
    let body = req.body;
    let product = await productSchema.findByIdAndUpdate(req.params.id, body, { new: true });
    if (!product || product.isDeleted) {
      return res.status(404).send({ success: false, message: "Sản phẩm không tồn tại" });
    }
    res.status(200).send({ success: true, data: product });
  } catch (error) {
    res.status(404).send({ success: false, message: error.message });
  }
});

// Xóa sản phẩm (xóa mềm)
router.delete('/:id', async function(req, res, next) {
  try {
    let product = await productSchema.findByIdAndUpdate(req.params.id, { isDeleted: true }, { new: true });
    if (!product) {
      return res.status(404).send({ success: false, message: "Sản phẩm không tồn tại" });
    }
    res.status(200).send({ success: true, message: "Sản phẩm đã được xóa mềm", data: product });
  } catch (error) {
    res.status(500).send({ success: false, message: error.message });
  }
});

module.exports = router;
