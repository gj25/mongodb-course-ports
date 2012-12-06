// HW 4.3
db.posts.ensureIndex({permalink:1})
db.posts.ensureIndex({tags:1, date:-1})
db.posts.ensureIndex({date:-1})

// HW 4.4
db.profile.find({"ns":"school2.students"}).sort({millis:-1}).limit(2).pretty()

// HW 5.1
db.posts.aggregate([
{ $project : { "comments.author" : 1 }},
{ $unwind : "$comments" },
{ $group : { _id : "$comments.author", "count" : {"$sum":1 }}},
{ $sort : { count:-1}},
{ $limit : 3 } 
])

// HW 5.2
db.zips.aggregate([
{ $match : { "state" : { $in : ["NY", "CA"] } }},
{ $group : { _id : { state: "$state", city: "$city" }, "city_pop" : { $sum : "$pop" }}},
{ $match : { "city_pop" : { $gt : 25000}}},
{ $group : { _id : "", avg_pop : { $avg : "$city_pop"}}}
])

//HW 5.3
db.grades.aggregate([
{ $unwind : "$scores" },
{ $match : { "scores.type" : { $in : ["homework", "exam"] } }},
{ $group : { _id : { student: "$student_id", "class_id": "$class_id" }, "avg_std" : { $avg : "$scores.score" }}},
{ $group : { _id : "$_id.class_id", "avg_cls" : { $avg : "$avg_std" }}}, 
{ $sort : { "avg_cls":1}}
])

//HW 5.4
db.zips.aggregate([
{ $project : { city:1, pop:1, first_char: {$substr : ["$city",0,1]}}},
{ $match : { "first_char" : { $in : [ "0","1","2","3","4","5","6","7","8","9" ] } } },
{ $group : { _id : "", total : { $sum : "$pop"}}}
])	 