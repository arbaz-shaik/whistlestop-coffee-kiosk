const db = require("../config/db")

/* ===============================
   CREATE USER
================================ */

exports.createUser = async (data) => {

  const { name, email, password } = data

  const result = await db.query(
    `INSERT INTO users
     (name,email,password)
     VALUES ($1,$2,$3)
     RETURNING id,name,email`,
    [name, email, password]
  )

  return result.rows[0]
}

/* ===============================
   GET USER BY EMAIL
================================ */

exports.getUserByEmail = async (email) => {

  const result = await db.query(
    "SELECT * FROM users WHERE email=$1",
    [email]
  )

  return result.rows[0]
}

/* ===============================
   GET USER BY ID
================================ */

exports.getUserById = async (id) => {

  const result = await db.query(
    "SELECT id,name,email FROM users WHERE id=$1",
    [id]
  )

  return result.rows[0]
}