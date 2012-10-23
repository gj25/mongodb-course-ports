# Author: Marcelo Lipienski

require 'mongo'

# connect to the db on standard port
connection = Mongo::Connection.new('localhost', 27017)

db = connection.db("m101")                  # attach to db
collection = db.collection("funnynumbers")  #specify the collection

magic = 0

begin
  collection.find.each do |item|
    if (item['value'] % 3) == 0
      magic = magic + item['value']
    end
  end
rescue Exception => message
  puts message
end

puts "The answer to Homework One, Problem 2 is #{magic.to_i}"
