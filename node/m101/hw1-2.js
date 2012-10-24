var mongodb = require('mongodb');
var _ = require('underscore');

var db = mongodb.Db('m101', new mongodb.Server('localhost', 27017), {safe: true});

db.open(function(err, conn) {
	if(err) {
		console.log('Error opening connection' + err);
	}
	conn.collection('funnynumbers', function(err, collection) {
		var magic = 0;
		if (err) {
			console.log(err);
		}
		collection.find(function(err, iterator) {
			if (err) {
				console.log(err);
			}
			iterator.toArray(function(err, docs) {
				if (err) {
					console.log(err);
				}
				_.each(docs, function(iter) {
					if (iter.value % 3 === 0) {
						magic += iter.value;
					}
				});
				console.log('The answer to Homework One, Problem 2 is ' + magic);
				conn.close();
			});
		});
	});
});