using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MongoDB.Driver;

namespace Hw1_2 {
    class Program {
        static void Main(string[] args) {
            MongoServer server = MongoServer.Create("mongodb://localhost/?safe=true");
            MongoDatabase db = server.GetDatabase("m101");
            MongoCollection<FunnyNumbers> collection = db.GetCollection<FunnyNumbers>("funnynumbers");
            int magic = 0;
            try {
                MongoCursor<FunnyNumbers> iter = collection.FindAll();
                foreach (FunnyNumbers item in iter)
                    if (item.Value % 3 == 0)
                        magic += item.Value;
                Console.Write("The answer to Homework One, Problem 2 is " + magic);
            } catch (Exception ex) { Console.Write(ex.ToString()); }
            Console.ReadKey(true);
        }
    }
}
