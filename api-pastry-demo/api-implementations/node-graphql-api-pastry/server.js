const fs = require('fs');
const path = require('path');
var express = require("express")
var { graphqlHTTP } = require("express-graphql")
var { buildSchema } = require("graphql")

const pastries = [
  {
    "name": "Baba Rhum",
    "description": "Delicieux Baba au Rhum pas calorique du tout",
    "size": "L",
    "price": 3.2,
    "status": "available",
    "rating": 3.1,
    "reviews": 185
  },
  {
    "name": "Divorces",
    "description": "Delicieux Divorces pas calorique du tout",
    "size": "M",
    "price": 2.8,
    "status": "available",
    "rating": 3.2,
    "reviews": 36
  },
  {
    "name": "Tartelette Fraise",
    "description": "Delicieuse Tartelette aux Fraises fraiches",
    "size": "S",
    "price": 2,
    "status": "available",
    "rating": 3.5,
    "reviews": 57
  },
  {
    "name": "Eclair Cafe",
    "description": "Delicieux Eclair au Cafe pas calorique du tout",
    "size": "M",
    "price": 2.5,
    "status": "available",
    "rating": 4.2,
    "reviews": 405
  },
  {
    "name": "Eclair Chocolat",
    "description": "Delicieux Eclair au Chocolat pas calorique du tout",
    "size": "M",
    "price": 2.4,
    "status": "unknown",
    "rating": 4.4,
    "reviews": 302
  },
  {
    "name": "Millefeuille",
    "description": "Delicieux Millefeuille pas calorique du tout",
    "size": "L",
    "price": 4.4,
    "status": "available",
    "rating": 4.5,
    "reviews": 253
  }
]

// Construct a schema, using GraphQL schema language
const schemaFilePath = path.join(__dirname, 'api-pastry.graphql');
var schema = buildSchema(fs.readFileSync(schemaFilePath).toString());

// The root provides a resolver function for each API endpoint
var root = {
  pastry: (req) => {
    console.log("In pastry handler with: " + JSON.stringify(req));
    let pastry = pastries.find(pastry => pastry.name === req.name);
    return pastry;
  },
  pastries: (req) => {
    console.log("In pastries handler with: " + JSON.stringify(req));
    let selectedPastries = pastries.filter(
      pastry => pastry.size === req.size
    );
    return {
      count: selectedPastries.length,
      pastries: selectedPastries
    }
  },
  allPastries: (req) => {
    console.log("In allPastries handler");
    return {
      count: pastries.length,
      pastries: pastries
    }
  },
  addReview: (req) => {
    console.log("In addReview handler with: " + JSON.stringify(req));
    let pastry = pastries.find(pastry => pastry.name === req.pastryName);
    let newTotal = (pastry.rating * pastry.reviews) + req.review.rating
    pastry.reviews++
    pastry.rating = (newTotal / pastry.reviews).toFixed(2);
    return pastry;
  }
}

var app = express()
app.use(
  "/graphql",
  graphqlHTTP({
    schema: schema,
    rootValue: root,
    graphiql: true,
  })
)
app.listen(4000)
console.log("Running a GraphQL API server at http://localhost:4000/graphql")