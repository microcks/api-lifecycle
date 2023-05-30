const express = require('express')
const app = express()
const port = 3001

const pastries = [
  {
    "name": "Baba Rhum",
    "description": "Delicieux Baba au Rhum pas calorique du tout",
    "size": "L",
    "price": "3.2"
  },
  {
    "name": "Divorces",
    "description": "Delicieux Divorces pas calorique du tout",
    "size": "M",
    "price": "2.8"
  },
  {
    "name": "Tartelette Fraise",
    "description": "Delicieuse Tartelette aux Fraises fraiches",
    "size": "S",
    "price": "2"
  },
  {
    "name": "Eclair Cafe",
    "description": "Delicieux Eclair au Cafe pas calorique du tout",
    "size": "M",
    "price": "2.5"
  },
  {
    "name": "Eclair Chocolat",
    "description": "Delicieux Eclair au Chocolat pas calorique du tout",
    "size": "M",
    "price": "2.4"
  },
  {
    "name": "Millefeuille",
    "description": "Delicieux Millefeuille pas calorique du tout",
    "size": "L",
    "price": "4.4"
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