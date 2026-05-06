const menuModel = require("../models/menuModel")

/* ===============================
   GET ALL MENU ITEMS
================================ */

exports.getMenuItems = async (req, res, next) => {
  try {
    const items = await menuModel.getAllMenuItems()
    res.json(items)
  } catch (error) {
    next(error)
  }
}

/* ===============================
   GET MENU ITEM BY ID
================================ */

exports.getMenuItemById = async (req, res, next) => {
  try {
    const item = await menuModel.getMenuItemById(req.params.id)

    if (!item) {
      return res.status(404).json({ message: "Menu item not found" })
    }

    res.json(item)
  } catch (error) {
    next(error)
  }
}

/* ===============================
   CREATE MENU ITEM
================================ */

exports.createMenuItem = async (req, res, next) => {
  try {
    const item = await menuModel.createMenuItem(req.body)
    res.status(201).json(item)
  } catch (error) {
    next(error)
  }
}

/* ===============================
   UPDATE MENU ITEM
================================ */

exports.updateMenuItem = async (req, res, next) => {
  try {
    const item = await menuModel.updateMenuItem(req.params.id, req.body)
    res.json(item)
  } catch (error) {
    next(error)
  }
}

/* ===============================
   DELETE MENU ITEM
================================ */

exports.deleteMenuItem = async (req, res, next) => {
  try {
    await menuModel.deleteMenuItem(req.params.id)
    res.json({ message: "Menu item deleted" })
  } catch (error) {
    next(error)
  }
}