using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;

namespace Hw1_3 {
    public static class WebApiConfig {
        public static void Register(HttpConfiguration config) {
            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "hw1_3/{controller}/{n}",
                defaults: new { id = RouteParameter.Optional }
            );
        }
    }
}
