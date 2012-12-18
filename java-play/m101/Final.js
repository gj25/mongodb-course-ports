mongorestore -d enron dump\enron

// 1
db.messages.find({"headers.From":"andrew.fastow@enron.com", "headers.To":"john.lavorato@enron.com"})
db.messages.find({"headers.From":"andrew.fastow@enron.com", "headers.To":"jeff.skilling@enron.com"})

// 2
db.messages.aggregate([
{ $unwind : "$headers.To" },
{ $group : { _id : { From: "$headers.From", To: "$headers.To" }, "count" : { $sum : 1 }}},
{ $sort : { "count":-1}},
{ $limit : 5 } 
])

// 3
db.messages.find({"headers.Message-ID":"<8147308.1075851042335.JavaMail.evans@thyme>"}).pretty()
db.messages.update(
    {"headers.Message-ID":"<8147308.1075851042335.JavaMail.evans@thyme>"},
	{ $push : { "headers.To" : "mrpotatohead@10gen.com" }}
     )
	 
// 5
for (var i=0; i< 10000; i++){ 
   db.fubar.insert({
       'a': Math.floor(Math.random() * 20000), 
       'b': Math.floor(Math.random() * 20000), 
	   'c': Math.floor(Math.random() * 20000)}); 
}
db.fubar.ensureIndex({a:1,b:1})
db.fubar.ensureIndex({a:1,c:1})
db.fubar.ensureIndex({c:1})
db.fubar.ensureIndex({a:1,b:1,c:-1})
db.fubar.getIndexes()

db.fubar.find({'a':{'$lt':10000}, 'b':{'$gt': 5000}}, {'a':1, 'c':1}).sort({'c':-1}).hint({a:1,b:1})

// 7
mongoimport -d m101 -c images images.json
mongoimport -d m101 -c albums albums.json
