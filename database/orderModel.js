const db = require("../config/db")

/* ===============================
   CREATE ORDER
================================ */

exports.createOrder = async (data) => {

  const client = await db.connect()

  try {

    await client.query("BEGIN")

    const {
      customer_name,
      email,
      phone,
      pickup_time,
      total_price,
      items
    } = data

    const orderResult = await client.query(
      `INSERT INTO orders
       (customer_name, email, phone, pickup_time, total_price)
       VALUES ($1,$2,$3,$4,$5)
       RETURNING *`,
      [customer_name, email, phone, pickup_time, total_price]
    )

    const order = orderResult.rows[0]

    /* Insert order items */

    for (const item of items) {

      await client.query(
        `INSERT INTO order_items
         (order_id, menu_item_id, quantity, size, price)
         VALUES ($1,$2,$3,$4,$5)`,
        [
          order.id,
          item.id,
          item.quantity,
          item.size,
          item.price
        ]
      )

    }

    await client.query("COMMIT")

    order.items = items

    return order

  } catch (error) {

    await client.query("ROLLBACK")
    throw error

  } finally {

    client.release()

  }

}

/* ===============================
   GET ORDER BY ID
================================ */

exports.getOrderById = async (id) => {

  const orderResult = await db.query(
    "SELECT * FROM orders WHERE id = $1",
    [id]
  )

  const order = orderResult.rows[0]

  if (!order) return null

  const itemsResult = await db.query(
    `SELECT 
        oi.quantity,
        oi.size,
        oi.price,
        m.name
     FROM order_items oi
     JOIN menu_items m 
       ON oi.menu_item_id = m.id
     WHERE oi.order_id = $1`,
    [id]
  )

  order.items = itemsResult.rows

  return order

}

/* ===============================
   GET ALL ORDERS
================================ */

exports.getAllOrders = async () => {

  const result = await db.query(
    `SELECT * 
     FROM orders 
     ORDER BY created_at DESC`
  )

  return result.rows

}

/* ===============================
   GET ORDERS BY CUSTOMER EMAIL
================================ */

exports.getOrdersByEmail = async (email) => {

  const result = await db.query(
    `SELECT id, customer_name, email, phone, pickup_time, total_price, status, created_at
     FROM orders
     WHERE email = $1
     ORDER BY created_at DESC`,
    [email]
  )

  return result.rows

}

/* ===============================
   UPDATE ORDER STATUS
================================ */

exports.updateOrderStatus = async (id, status) => {

  const result = await db.query(
    `UPDATE orders
     SET status = $1
     WHERE id = $2
     RETURNING *`,
    [status, id]
  )

  return result.rows[0]

}

/* ===============================
   CANCEL ORDER
================================ */

exports.cancelOrder = async (id) => {

  const result = await db.query(
    `UPDATE orders
     SET status = 'cancelled'
     WHERE id = $1
     RETURNING *`,
    [id]
  )

  return result.rows[0]

}

/* ===============================
   ORDER ANALYTICS
================================ */

exports.getOrderAnalytics = async () => {

  const totalOrders = await db.query(
    "SELECT COUNT(*) FROM orders"
  )

  const revenue = await db.query(
    "SELECT COALESCE(SUM(total_price),0) AS total_revenue FROM orders"
  )

  return {
    total_orders: Number(totalOrders.rows[0].count),
    total_revenue: Number(revenue.rows[0].total_revenue)
  }

}
