var expect = require('chai').expect;
var sinon = require('sinon');

var moduleLoader = require('./common/moduleLoader.js');
var mockFactory = require('./common/mockFactory.js');
var json = require('./common/jsonComparer.js');

var js = '../../../apiproxy/resources/jsc/JS-SetTimeData.js';

describe('feature: Compute latency time', function () {

    it('should be multiplied by 1000', function (done) {
        var mock = mockFactory.getMock();

        mock.contextGetVariableMethod.withArgs('client.received.start.timestamp').returns(42);
        mock.contextGetVariableMethod.withArgs('target.sent.start.timestamp').returns(4242);
        mock.contextGetVariableMethod.withArgs('target.received.end.timestamp').returns(4242);
        mock.contextGetVariableMethod.withArgs('system.timestamp').returns(4242);

        moduleLoader.load(js, function (err) {
            expect(err).to.be.undefined;

            expect(mock.contextSetVariableMethod.calledWith('message.statistics.clientLatency', '42000')).to.be.true;
            expect(mock.contextSetVariableMethod.calledWith('message.statistics.targetLatency', '4242000')).to.be.true;
            expect(mock.contextSetVariableMethod.calledWith('message.statistics.totalLatency', '4242000')).to.be.true;

            done();

        });
    });

});

// var total_request_time = request_end_time-request_start_time;
// var total_target_time  = target_end_time-target_start_time;
// var total_client_time  = total_request_time-total_target_time;

