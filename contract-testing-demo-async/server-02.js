'use strict';

var fs = require('fs');
const https = require('https');
const url = require('url');
const WebSocket = require('ws');

// Initialize WebSocket server.
var wss = new WebSocket.Server({ noServer: true });

wss = new WebSocket.Server({ port: 4002, path: "/websocket" });

console.log("Starting WebSocket server on ws://localhost:4002/websocket");

const getRandomId = () => Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);

const createMessage = (id, customerId) => ({
  id: `${id}`,
  customerId: `${customerId}`,
  status: "VALIDATED",
  productQuantities: [
    {
      quantity: 1,
      pastryName: "Tartelette Fraise"
    },
    {
      quantity: 2,
      pastryName: "Donut"
    }
  ]
})

const sendMessage = () => {
  var msg = createMessage(getRandomId(), getRandomId());
  console.log("Sending to " + wss.clients.size + " clients");
  wss.clients.forEach(function each(client) {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(msg));
    }
  });
  console.log(msg);
}

setInterval(sendMessage, 3000);