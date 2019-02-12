/* jslint node: true */
'use strict';
var apickli = require('apickli');

// require('chromedriver');
// var seleniumWebdriver = require('selenium-webdriver');

var jwt = require('jsonwebtoken');
var CryptoJS = require("crypto-js");
var hmacUtil = new (require("../support/hmac-util.js"))();
const jsonPath = require('JSONPath');

var fs = require('fs-extra');
var cert = fs.readFileSync(process.cwd() + '/target/test/tpp.key');
var utils = new (require("../support/utils.js"))();

var config = require("../../config.json");

module.exports = function () {

    this.Given(/^I set jwt alg to$/, function (table, callback) {
        var header = utils.convertTable2Object(table, this.apickli);
        this.apickli.storeValueInScenarioScope('jwtHeader', header);
        callback();
    });

    this.Given(/^I generate a nonce and store it as (.*)$/, function(varName, callback) {
        var name = this.apickli.replaceVariables(varName);
        var nonce = utils.genNonce(12);
        this.apickli.storeValueInScenarioScope(name, nonce);
        callback();
    });

    this.Given(/^I set jwt payload using app (.*) with values$/, function (developerAppName, table, callback) {
        var developerApp = this.apickli.scenarioVariables['developerApps'][developerAppName];
        for (var property in developerApp) {
            if (developerApp.hasOwnProperty(property)) {
                this.apickli.scenarioVariables[property] = developerApp[property];
            }
        }
        var payload = utils.convertTable2Object(table, this.apickli);
        payload.exp = (payload.exp !== 'gen()' ? parseInt(payload.exp, 10) : utils.genCurTime());
        payload.nonce = (payload.nonce !== 'gen()' ? payload.nonce : utils.genNonce(12));
        this.apickli.storeValueInScenarioScope('jwtPayload', payload);
        callback();
    });

    this.Given(/^I generate a clientAssertion jws$/, function (callback) {
        var header = this.apickli.scenarioVariables['jwtHeader'];
        var payload = this.apickli.scenarioVariables['jwtPayload'];
        var jws = jwt.sign(payload, cert, {algorithm: header.alg});
        this.apickli.storeValueInScenarioScope('clientAssertion', jws);
        callback();
    });

    this.Given(/^I have a client credentials access_token using app (.*)$/, function (appParameter, callback) {
        var developerApp = this.apickli.replaceVariables(appParameter);
        var payload = {
            iss: this.apickli.scenarioVariables['developerApps'][developerApp].clientId,
            sub: this.apickli.scenarioVariables['developerApps'][developerApp].clientId,
            aud: this.apickli.scenarioVariables['audience'],
            scope: this.apickli.scenarioVariables['developerApps'][developerApp].tokenScope,
            nonce: utils.genNonce(12),
            exp: utils.genCurTime(),
            response_type: 'token'
        };

        var jws = jwt.sign(payload, cert, {algorithm: "RS256"});

        var domain = this.apickli.scenarioVariables["tokenDomain"];
        var basepath = this.apickli.scenarioVariables["tokenBasepath"];

        var callout = new apickli.Apickli('https', domain + basepath);
        callout.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        callout.setRequestBody("grant_type=client_credentials&client_assertion="+jws);

        var self = this;

        callout.post("/token", function (error, response) {
            if (error) {
                callback(new Error(error));
            }
            var token = JSON.parse(response.body).access_token;
            self.apickli.setAccessToken(token);
            self.apickli.setBearerToken();
            self.apickli.scenarioVariables["access_token"] = token;
            self.apickli.setAccessToken(token);
            //console.log("Access Token: " + token);
            callback();
        });
    });

    this.Given(/^I update jws (.*) payload path (.*) to (.*)$/, function (jwtVarName, path, value, callback) {
        var jws = this.apickli.replaceVariables(jwtVarName);
        path = this.apickli.replaceVariables(path);
        value = this.apickli.replaceVariables(value);
        var jwtParts = this.apickli.scenarioVariables[jws].split(".");
        var payload = JSON.parse(utils.base64Decode(jwtParts[1]));
        var nodes = jsonPath({json: payload, path: path, resultType: 'path', autostart:true});
        if (nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                var nodePath = JSON.parse(nodes[i]
                    .substring(1)
                    .replace(/'/g,'"')
                    .replace(/]\[/g, ',')).join('.');
                eval ('payload.' + nodePath + '=value');
                jwtParts[1] = utils.base64Encode(JSON.stringify(payload), 'base64');
                this.apickli.storeValueInScenarioScope(jws, jwtParts.join('.'));
                if (jws === "access_token") {
                    this.apickli.setAccessToken(this.apickli.scenarioVariables[jws]);
                    this.apickli.setBearerToken();
                }
            }
        } else {
            throw 'Could not assing property with path ' + path;
        }
        callback();
    });

    this.Given(/^I update jws (.*) header path (.*) to (.*)$/, function (jwtVarName, path, value, callback) {
        var jws = this.apickli.replaceVariables(jwtVarName);
        path = this.apickli.replaceVariables(path);
        value = this.apickli.replaceVariables(value);
        var jwtParts = this.apickli.scenarioVariables[jws].split(".");
        var jose = JSON.parse(utils.base64Decode(jwtParts[0]));
        var nodes = jsonPath({json: jose, path: path, resultType: 'path', autostart:true});
        if (nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                var nodePath = JSON.parse(nodes[i]
                    .substring(1)
                    .replace(/'/g,'"')
                    .replace(/]\[/g, ',')).join('.');
                eval ('jose.' + nodePath + '=value');
                jwtParts[0] = utils.base64Encode(JSON.stringify(jose));
                this.apickli.storeValueInScenarioScope(jws, jwtParts.join('.'));
                if (jws === "access_token") {
                    this.apickli.setAccessToken(this.apickli.scenarioVariables[jws]);
                    this.apickli.setBearerToken();
                }
            }
        } else {
            throw 'Could not assing property with path ' + path;
        }
        callback();
    });

    this.Given(/^I update (.*) value to jws (.*) to (.*)$/, function (part, jwsVarName, value, callback) {

        var part = this.apickli.replaceVariables(part);
        var jws = this.apickli.replaceVariables(jwsVarName);
        var value = this.apickli.replaceVariables(value);
        var jwtParts = this.apickli.scenarioVariables[jws].split(".");
        switch (part) {
            case 'jose':
                jwtParts[0] = utils.base64Encode(value);
                this.apickli.storeValueInScenarioScope(jws, jwtParts.join('.'));
                break;
            case 'payload':
                jwtParts[1] = utils.base64Encode(value);
                this.apickli.storeValueInScenarioScope(jws, jwtParts.join('.'));
                break;
            case 'signature':
                jwtParts[2] = utils.base64Encode(value);
                this.apickli.storeValueInScenarioScope(jws, jwtParts.join('.'));
                break;
            default:
                throw 'Unknows jws part: ' + part;
        }
        this.apickli.storeValueInScenarioScope(jws, jwtParts.join('.'));
        if (jws === "access_token") {
            this.apickli.setAccessToken(this.apickli.scenarioVariables[jws]);
            this.apickli.setBearerToken();
        }
        callback();
    });
};
