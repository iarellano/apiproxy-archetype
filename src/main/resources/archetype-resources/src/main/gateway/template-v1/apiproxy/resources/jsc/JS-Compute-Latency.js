/* globals context */

var requestStartTime = context.getVariable('client.received.start.timestamp'); // 2000
var targetStartTime  = context.getVariable('target.sent.start.timestamp');  // 20100
var targetEndTime    = context.getVariable('target.received.end.timestamp'); // 2300
var requestEndTime   = context.getVariable('system.timestamp'); //2400
var totalRequestTime = requestEndTime-requestStartTime; // 2400 - 2000 = 400
var totalTargetTime  = targetEndTime-targetStartTime;   // 2300 - 2100 = 200
var totalClientTime  = totalRequestTime-totalTargetTime;  // 200

context.setVariable("message.statistics.clientLatency", totalClientTime);
context.setVariable("message.statistics.targetLatency", totalTargetTime);
context.setVariable("message.statistics.totalLatency", totalRequestTime);