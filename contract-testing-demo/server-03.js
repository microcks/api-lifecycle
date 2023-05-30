const express = require('express')
const app = express()
const port = 3003

app.use(express.json()) // for parsing application/json

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
  res.send(pastries.filter(
    pastry => pastry.size === req.query.size
  ));
})

app.get('/pastries/:name', (req, res) => {
  const pastry = pastries.find(pastry => pastry.name === req.params.name);
  res.send(pastry);
})

app.patch('/pastries/:name', (req, res) => {
  const pastry = pastries.find(pastry => pastry.name === req.params.name);
  Object.keys(req.body).map(
    (key, index) => pastry[key] = req.body[key]
  )
  res.send(pastry);
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})