const orderModel = require("../models/orderModel")

/* ===============================
   STAFF DASHBOARD ORDERS
================================ */

exports.getDashboardOrders = async (req, res, next) => {

  try {

    const orders = await orderModel.getAllOrders()

    res.json(orders)

  } catch (error) {
    next(error)
  }

}

/* ===============================
   UPDATE ORDER STATUS (STAFF)
================================ */

exports.updateOrderStatusByStaff = async (req, res, next) => {

  try {

    const { status } = req.body

    const order = await orderModel.updateOrderStatus(
      req.params.id,
      status
    )

    res.json(order)

  } catch (error) {
    next(error)
  }

}

/* ===============================
   ORDER ANALYTICS
================================ */

exports.getOrderAnalytics = async (req, res, next) => {

  try {

    const analytics = await orderModel.getOrderAnalytics()

    res.json(analytics)

  } catch (error) {
    next(error)
  }

}