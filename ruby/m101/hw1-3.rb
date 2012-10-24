require '../lib/mongo_course'
require 'sinatra'

before do
  config = {:server => '127.0.0.1', :db => 'm101', :collection => 'funnynumbers'}
  MongoCourse.connect(config)
end

get '/hw1/:skip' do
  doc = MongoCourse.find({}, sort: 'value', skip: params[:skip].to_i).next
  response = doc['value'].to_i unless doc.nil?
  
  "#{response}" # outputs response to the browser
end
