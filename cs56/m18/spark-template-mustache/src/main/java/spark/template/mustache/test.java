package spark.template.mustache;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.port;
public class test{
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    public static void main(String[] args){
        port(getHerokuAssignedPort());
	
	get("/",(rq,rs)->"<b>test</b>");
    }
}
