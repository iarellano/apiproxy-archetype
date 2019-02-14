/* jslint node: true */
'use strict';
var apickli = require('apickli');

module.exports = function () {
    this.Given(/^I set scenario variables from application (.*) and using prefix (.*)$/, function (developerAppName, prefix, callback) {
        var developerAppName = this.apickli.replaceVariables(developerAppName);
        var developerApp = this.apickli.scenarioVariables['developerApps'][developerAppName];
        for (var property in developerApp) {
            if (developerApp.hasOwnProperty(property)) {
                this.apickli.scenarioVariables[prefix + property] = developerApp[property];
            }
        }
        callback();
    });
}

