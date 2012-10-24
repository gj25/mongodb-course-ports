require '../lib/mongo_course'

config = {:server => '127.0.0.1', :db => 'm101', :collection => 'funnynumbers'}
MongoCourse.connect(config)

magic = 0

begin
  MongoCourse.find.each do |item|
    if (item['value'] % 3) == 0
      magic = magic + item['value']
    end
  end
rescue Exception => message
  puts message
end

puts "The answer to Homework One, Problem 2 is #{magic.to_i}"
