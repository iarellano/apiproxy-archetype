/* jslint node: true */
'use strict';

// Create a new singleton
var hmacUtil = new (require("../support/hmac-util.js"))();
var utils = new (require("../support/utils.js"))();

module.exports = function () {

    var setupCommon = function(apickli, verb, developerApp, pathSuffix) {

        var issued = (Math.floor(new Date().getTime() / 1000)) + '';
        var nonce = hmacUtil.nonce(12);

        var clientSecret = apickli.scenarioVariables['developerApps'][developerApp].clientSecret;

        var data = apickli.requestBody != undefined && apickli.requestBody != null && apickli.requestBody.length > 0
            ? apickli.requestBody
            : pathSuffix;

        var hmac = hmacUtil.hmac(data, clientSecret, nonce, issued);

        apickli.addRequestHeader("X-Banorte-Hmac-Token", hmac);
        apickli.addRequestHeader("X-Banorte-Hmac-Nonce", nonce);
        apickli.addRequestHeader("X-Banorte-Hmac-Issued", issued);

        // console.log( "HMAC: " + hmac);
        // console.log( "Nonce: " + nonce);
        // console.log( "Issued: " + issued);
        // console.log( "Data: " + data);
    };

    this.Given(/^I generate an hmac nonce and store it as (.*)$/, function(varName, callback) {
        var varName = this.apickli.replaceVariables(varName);
        var nonce = hmacUtil.nonce(12);
        this.apickli.storeValueInScenarioScope(varName, nonce);
        callback();
    });

    this.Given(/^I setup HMAC using app (.*) with values$/, function (developerAppName, table, callback) {
        var developerAppName = this.apickli.replaceVariables(developerAppName);
        var developerApp = this.apickli.scenarioVariables['developerApps'][developerAppName];
        for (var property in developerApp) {
            if (developerApp.hasOwnProperty(property)) {
                this.apickli.scenarioVariables[property] = developerApp[property];
            }
        }
        var params = utils.convertTable2Object(table, this.apickli);


        var issued = params.issued === 'gen()' ? (Math.floor(new Date().getTime() / 1000)) + '' : params.issued;
        var nonce = params.nonce === 'gen()' ? hmacUtil.nonce(12) : params.nonce;

        var clientSecret = developerApp.clientSecret;

        var data = this.apickli.requestBody != undefined && this.apickli.requestBody != null && this.apickli.requestBody.length > 0
            ? this.apickli.requestBody
            : pathSuffix;

        var hmac = hmacUtil.hmac(data, clientSecret, nonce, issued);

        this.apickli.addRequestHeader("X-Banorte-Hmac-Token", hmac);
        this.apickli.addRequestHeader("X-Banorte-Hmac-Nonce", nonce);
        this.apickli.addRequestHeader("X-Banorte-Hmac-Issued", issued);
        callback();
    });

    this.Given(/^I remove header (.*)$/, function(header, callback) {
        var header = this.apickli.replaceVariables(header);
        this.apickli.removeRequestHeader(header);
        callback();
    });

    this.When(/^I use HMAC for application (.*) and GET (.*)$/, function (appParameter, pathSuffix, callback) {
        setupCommon(this.apickli, "GET", this.apickli.replaceVariables(appParameter), pathSuffix);
        this.apickli.get(pathSuffix, function(err, response) {
            callback(err);
        });
    });

    this.When(/^I use HMAC for application (.*) and PUT to (.*)$/, function (appParameter, pathSuffix, callback) {

        setupCommon(this.apickli, "PUT", this.apickli.replaceVariables(appParameter), pathSuffix);
        this.apickli.put(pathSuffix, function(err, response) {
            callback(err);
        });
    });

    this.When(/^I use HMAC for application (.*) and POST to (.*)$/, function (appParameter, pathSuffix, callback) {
        setupCommon(this.apickli, "POST", this.apickli.replaceVariables(appParameter), pathSuffix);
        this.apickli.post(pathSuffix, function(err, response) {
            callback(err);
        });
    });

    this.When(/^I use HMAC for application (.*) and DELETE (.*)$/, function (appParameter, pathSuffix, callback) {
        setupCommon(this.apickli, "DELETE", this.apickli.replaceVariables(appParameter), pathSuffix);
        this.apickli.delete(pathSuffix, function(err, response) {
            callback(err);
        });
    });

};