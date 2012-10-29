using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Mime;
using System.Text;
using System.Web.Http;
using Hw1_3.Models;
using MongoDB.Driver;
using MongoDB.Driver.Linq;

namespace Hw1_3.Controllers {
    public class Hw1Controller : ApiController {
        //This controller is accessible via:
        //http://localhost:10240/hw1_3/Hw1/50

        [HttpGet]
        public HttpResponseMessage Get(int n) {
            string responseText = string.Empty;

            MongoServer server = MongoServer.Create("mongodb://localhost/?safe=true");
            MongoDatabase db = server.GetDatabase("m101");
            MongoCollection<FunnyNumbers> collection = db.GetCollection<FunnyNumbers>("funnynumbers");
            try {
                IQueryable<FunnyNumbers> funnyNumbers = collection.AsQueryable().Skip(n).Take(1).OrderBy(funnyNumber => funnyNumber.Value);
                foreach (FunnyNumbers item in funnyNumbers)
                    responseText = item.Value + "\n";
            } catch (Exception ex) { responseText = ex.ToString(); }

            HttpResponseMessage response = new HttpResponseMessage(HttpStatusCode.OK);
            response.Content = new StringContent(responseText, Encoding.UTF8, MediaTypeNames.Text.Plain);
            return response;
        }
    }
}