const orderModel = require("../models/orderModel")
const { emitNewOrder, emitStatusUpdate } = require("../sockets/orderSocket")

/* ===============================
   CREATE ORDER
================================ */

exports.createOrder = async (req, res, next) => {

  try {

    const orderData = req.body

    if (!orderData || !orderData.items || orderData.items.length === 0) {
      return res.status(400).json({
        message: "Order must contain at least one item"
      })
    }

    const order = await orderModel.createOrder(orderData)

    /* Notify staff dashboard in real time */
    emitNewOrder(order)

    res.status(201).json(order)

  } catch (error) {

    console.error("Create order error:", error)
    next(error)

  }

}


/* ===============================
   GET ORDER BY ID
================================ */

exports.getOrderById = async (req, res, next) => {

  try {

    const id = req.params.id

    if (!id) {
      return res.status(400).json({ message: "Order ID required" })
    }

    const order = await orderModel.getOrderById(id)

    if (!order) {
      return res.status(404).json({ message: "Order not found" })
    }

    res.json(order)

  } catch (error) {

    console.error("Get order error:", error)
    next(error)

  }

}


/* ===============================
   GET ALL ORDERS
================================ */

exports.getAllOrders = async (req, res, next) => {

  try {

    const orders = await orderModel.getAllOrders()

    res.json(orders)

  } catch (error) {

    console.error("Get orders error:", error)
    next(error)

  }

}


/* ===============================
   GET ORDERS BY CUSTOMER EMAIL
================================ */

exports.getOrdersByCustomer = async (req, res, next) => {

  try {

    const email = req.params.email

    if (!email) {
      return res.status(400).json({
        message: "Customer email is required"
      })
    }

    const orders = await orderModel.getOrdersByEmail(email)

    res.json(orders)

  } catch (error) {

    console.error("Get customer orders error:", error)
    next(error)

  }

}


/* ===============================
   UPDATE ORDER STATUS
================================ */

exports.updateOrderStatus = async (req, res, next) => {

  try {

    const id = req.params.id
    const { status } = req.body

    if (!status) {
      return res.status(400).json({
        message: "Status is required"
      })
    }

    const order = await orderModel.updateOrderStatus(id, status)

    if (!order) {
      return res.status(404).json({
        message: "Order not found"
      })
    }

    /* Emit real-time update */
    emitStatusUpdate(order)

    res.json(order)

  } catch (error) {

    console.error("Update status error:", error)
    next(error)

  }

}


/* ===============================
   CANCEL ORDER
================================ */

exports.cancelOrder = async (req, res, next) => {

  try {

    const id = req.params.id

    const order = await orderModel.cancelOrder(id)

    if (!order) {
      return res.status(404).json({
        message: "Order not found"
      })
    }

    res.json(order)

  } catch (error) {

    console.error("Cancel order error:", error)
    next(error)

  }

}
