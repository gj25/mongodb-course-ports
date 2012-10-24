require 'rubygems'
require 'mongo'

module MongoCourse
  class << self

		def connect(config)
			@db = Mongo::Connection.new(config[:server],config[:port] || 27017).db(config[:db])
			set_collection config[:collection]
		end
		
		def set_collection(collection)
		  @collection = collection
		end
		
		def find(*options)
			return @db.collection(@collection).find(*options)
		end
		
  end
end
