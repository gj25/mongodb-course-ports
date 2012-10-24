# Author: Marcelo Lipienski

require 'mongo'
require 'sinatra'

get '/hw1/:skip_entry' do
  # connect to the db on standard port
  connection = Mongo::Connection.new('localhost', 27017)

  db = connection.db("m101")                  # attach to db
  collection = db.collection("funnynumbers")  #specify the collection

  doc = collection.find({}, sort: 'value', skip: params[:skip_entry].to_i).next
  response = doc['value'].to_i unless doc.nil?
  
  "#{response}" # outputs response to the browser
  
end
