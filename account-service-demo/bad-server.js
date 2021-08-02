'use strict';

var fs = require('fs');
const https = require('https');
const url = require('url');
const WebSocket = require('ws');

var commandArgs = process.argv.slice(2);

const wsCert = commandArgs[0] || null;
const wsKey = commandArgs[1] || null;

var wss = new WebSocket.Server({ noServer: true });

wss.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
    console.log('Received: %s', message);
  });
});

// Initialize WebSocket server with or without ssl depending on Cert presence.
if (wsCert != null && wsKey != null) {
  const server = https.createServer({
    cert: fs.readFileSync(wsCert, 'utf-8'),
    key: fs.readFileSync(wsKey, 'utf-8')
  });

  server.on('upgrade', function upgrade(request, socket, head) {
    const pathname = url.parse(request.url).pathname;
  
    if (pathname === '/websocket') {
      wss.handleUpgrade(request, socket, head, function done(ws) {
        wss.emit('connection', ws, request);
      });
    } else {
      socket.destroy();
    }
  });

  server.listen(4000);
} else {
  wss = new WebSocket.Server({ port: 4000, path: "/websocket" });
}

console.log("Starting WebSocket server on ws://localhost:4000/websocket");

const getRandomId = () => Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);

const createMessage = id => ({
  fullName: "Laurent Broudoux",
  email: "laurent@microcks.io",
  age: 42
})

const sendMessage = () => {
  var msg = createMessage(getRandomId());
  console.log("Sending to " + wss.clients.size + " clients");
  wss.clients.forEach(function each(client) {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(msg));
    }
  });
  console.log(msg);
}

setInterval(sendMessage, 3000);