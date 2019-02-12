var argv = require('minimist')(process.argv.slice(2));
var fs = require('fs-extra');

var json = fs.readJsonSync(argv.f)
var counter = argv.c;
var errors = [];
for (var i = 0; i < json.length; i++) {
    var entry = json[i];
    if (entry[counter] > 0) {
        errors.push(entry);
    }
}

if (errors.length > 0) {
    console.error("apigeelint errors found");
    console.error(JSON.stringify(errors, null, 4));
    process.exit(1);
}