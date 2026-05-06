const { Pool } = require("pg")
require("dotenv").config()

/* ================================
   POSTGRESQL CONNECTION POOL
================================ */

const pool = new Pool({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  port: process.env.DB_PORT || 5432,
  max: 20,                // maximum connections
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000
})

/* ================================
   TEST DATABASE CONNECTION
================================ */

pool.connect((err, client, release) => {
  if (err) {
    console.error("❌ Error connecting to PostgreSQL:", err.stack)
  } else {
    console.log("✅ PostgreSQL connected successfully")
    release()
  }
})

module.exports = pool