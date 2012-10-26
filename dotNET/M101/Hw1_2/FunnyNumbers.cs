using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace Hw1_2 {
    public class FunnyNumbers {
        public ObjectId Id { get; set; }

        [BsonElement("value")]
        public int Value { get; set; }
    }
}
