# Author: Marcelo Lipienski

require 'mongo'
require 'sinatra'

get '/hw1/:skip_entry' do
  # connect to the db on standard port
  connection = Mongo::Connection.new('localhost', 27017)

  db = connection.db("m101")                  # attach to db
  collection = db.collection("funnynumbers")  #specify the collection
  
  skip_count = 0
  response = nil
  
  collection.find().sort(:value).each do |entry|
 
    if skip_count < params[:skip_entry].to_i
      skip_count = skip_count + 1
    else
      response = entry['value'].to_i
      break
    end
    
  end
  
  "#{response}" # outputs response to the browser
  
end
