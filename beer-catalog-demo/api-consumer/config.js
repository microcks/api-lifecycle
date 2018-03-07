/*
 * Sample config file for the Beer UI
 *
 * Usage: edit this file and save it as "config.js"
 */
define(function(require, exports, module) {
  module.exports = {
    // The base URL of the Beer API
    baseURL: "http://beer-catalog-api-beer-catalog-prod.52.174.149.59.nip.io/api",

    // If a user-key is required, you can pass it as HTTP Header:
    additionalHeaders: { "user-key": "db0a8a7d3ca99b5df5aace2f61ac4e45" },

    // Or in the Query String
    extraQueryStringSuffix: "user_key=db0a8a7d3ca99b5df5aace2f61ac4e45"
  };
});
