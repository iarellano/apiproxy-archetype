/* jshint node:true */
'use strict';

var apickli    = require('apickli');
var config     = require('../../config.json');
var devAppKeys = require('../../../../devAppKeys.json');
var devApps    = require('../../../../edge/org/developerApps.json');

for (var developer in devApps) {
    console.log("Developer: " + developer);
    if (devApps.hasOwnProperty(developer)) {
        console.log("It has own property: " + developer);
        var developerApps = devApps[developer];
        for (var i = 0; i < developerApps.length; i++) {
            var developerApp = developerApps[i];
            for (var j = 0; j < devAppKeys.length; j++) {
                if (devAppKeys[j].name === developerApp.name) {
                    config.parameters[developerApp.name].clientId = devAppKeys[j].credentials[0].consumerKey;
                    config.parameters[developerApp.name].clientSecret = devAppKeys[j].credentials[0].consumerSecret;
                    // We do not break the loop so we can always get the latests keys
                }
            }
        }
    }
}

var domain   = config.parameters.domain;
var basepath = config.parameters.basepath;


console.log('api parameters: [' + domain + ', ' + basepath + ']');

module.exports = function() {
    // cleanup before every scenario
    this.Before(function(scenario, callback) {
        console.log('Before scenario hook');
        this.apickli = new apickli.Apickli('${northbound.protocol}', domain + ":${northbound.port}" + basepath);
        this.apickli.storeValueInScenarioScope("domain", domain);
        this.apickli.storeValueInScenarioScope("developerApps", config.parameters);
        callback();
    });
};
