var mongodb = require('mongodb');
var express = require('express');

var db = mongodb.Db('m101', new mongodb.Server('localhost', 27017), {safe: true});


var app = express.createServer()
app.use(express.methodOverride());

app.get('/hw1/:n', function(req, res) {
	db.open(function(err, db) {
		var collection = new mongodb.Collection(db, 'funnynumbers');
		collection.find({}, {limit: 1, skip: req.params.n, sort: 'value'}).toArray(function(err, results) {
			if (err) {
				console.log(err);
			}
			db.close();
			res.send(results[0].value.toString());
		});
	});
});

console.log('App started on port 8080');
app.listen(8080);
