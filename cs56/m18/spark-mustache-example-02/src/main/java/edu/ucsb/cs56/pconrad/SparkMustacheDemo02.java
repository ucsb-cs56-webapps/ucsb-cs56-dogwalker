package edu.ucsb.cs56.pconrad;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spark.template.mustache.MustacheTemplateEngine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.net.URL;

import static spark.Spark.get;
import static spark.Spark.port;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;


/**
 * Simple example of using Mustache Templates
 *
 */

public class SparkMustacheDemo02 {

	public static final String CLASSNAME="SparkMustacheDemo02";
	
	public static final Logger log = Logger.getLogger(CLASSNAME);


private static String streamToString(InputStream inputStream) {
    String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
    return text;
  }

 static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)                         
    }

    
     
  public static String jsonGetRequest(String urlQueryString) {
    String json = null;
    try {
      URL url = new URL(urlQueryString);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects(false);
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("charset", "utf-8");
      connection.connect();
      InputStream inStream = connection.getInputStream();
      json = streamToString(inStream); // input stream to string                                                
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return json;
  }

  public static String jsonParseConverter(String jsonString){
        String s = "";
         
        String [] jString = jsonString.split(",");
        //for(int x = 0; x < jString.length; x++){                                                              
        s+=jString[0].split(":")[1];
        s+= "\n";
        s+=jString[2].split(":")[1]+"\n";
        s+=jString[3].split(":")[1]+"\n";
        s+=jString[4].split(":")[1]+"\n";
        s+=jString[5].split(":")[1]+"\n";
        s+=jString[6].split(":")[1]+"\n";
        s+=jString[7].split(":")[1]+"\n";

        //}                                                                                                     
        return s;
  }

	public static void main(String[] args) {

        port(getHerokuAssignedPort());

		 String homeInfo=jsonGetRequest("https://raw.githubusercontent.com/andrewdoanutz/ucsb-cs56-dogwalker/master/home.json");
 
  
        Map map2=new HashMap();
        map2.put("listing",homeInfo);


        String userProf = jsonParseConverter(jsonGetRequest("https://raw.githubusercontent.com/andrewdoanutz/ucsb-cs56-dogwalker/master/profile.json"));
     
  

	
	
   
		Map map = new HashMap();
        map.put("name", "Sam");

		        get("/home",(rq,rs)-> new ModelAndView(map,"home.mustache"), new MustacheTemplateEngine());
           get("/profile",(rq,rs)->new ModelAndView(map,"profile.mustache"), new MustacheTemplateEngine());
        // hello.mustache file is in resources/templates directory
        get("/", (rq, rs) -> new ModelAndView(map, "Login.mustache"), new MustacheTemplateEngine());

		get("/signup", (rq, rs) -> new ModelAndView(map, "Signup.mustache"), new MustacheTemplateEngine());
    }

		         


}
