var expect = require('chai').expect;
var sinon = require('sinon');

var moduleLoader = require('./common/moduleLoader.js');
var mockFactory = require('./common/mockFactory.js');
var json = require('./common/jsonComparer.js');

var js = '${project.build.directory}/apiproxy/resources/jsc/JS-Compute-Latency.js';

describe('feature: Compute latency time', function () {

    it('should be multiplied by 1000', function (done) {
        var mock = mockFactory.getMock();

        mock.contextGetVariableMethod.withArgs('client.received.start.timestamp').returns(2000);
        mock.contextGetVariableMethod.withArgs('target.sent.start.timestamp').returns(2100);
        mock.contextGetVariableMethod.withArgs('target.received.end.timestamp').returns(2300);
        mock.contextGetVariableMethod.withArgs('system.timestamp').returns(2400);

        moduleLoader.load(js, function (err) {
            expect(err).to.be.undefined;

            expect(mock.contextSetVariableMethod.calledWith('message.statistics.clientLatency', 200)).to.be.true;
            expect(mock.contextSetVariableMethod.calledWith('message.statistics.targetLatency', 200)).to.be.true;
            expect(mock.contextSetVariableMethod.calledWith('message.statistics.totalLatency', 400)).to.be.true;

            done();

        });
    });

});