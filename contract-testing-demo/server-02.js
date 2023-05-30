const express = require('express')
const app = express()
// Set default server port to 3002
var port = process.env.PORT || 3002;

const pastries = [
  {
    "name": "Baba Rhum",
    "description": "Delicieux Baba au Rhum pas calorique du tout",
    "size": "L",
    "price": 3.2,
    "status": "available"
  },
  {
    "name": "Divorces",
    "description": "Delicieux Divorces pas calorique du tout",
    "size": "M",
    "price": 2.8,
    "status": "available"
  },
  {
    "name": "Tartelette Fraise",
    "description": "Delicieuse Tartelette aux Fraises fraiches",
    "size": "S",
    "price": 2,
    "status": "available"
  },
  {
    "name": "Eclair Cafe",
    "description": "Delicieux Eclair au Cafe pas calorique du tout",
    "size": "M",
    "price": 2.5,
    "status": "available"
  },
  {
    "name": "Eclair Chocolat",
    "description": "Delicieux Eclair au Chocolat pas calorique du tout",
    "size": "M",
    "price": 2.4,
    "status": "unknown"
  },
  {
    "name": "Millefeuille",
    "description": "Delicieux Millefeuille pas calorique du tout",
    "size": "L",
    "price": 4.4,
    "status": "available"
  }
]

app.get('/pastries', (req, res) => {
  res.send(pastries);
})

app.get('/pastries/:name', (req, res) => {
  res.send(pastries[Math.floor(Math.random() * pastries.length)]);
})

app.patch('/pastries/:name', (req, res) => {
  res.send(pastries[Math.floor(Math.random() * pastries.length)]);
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})