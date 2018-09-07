package spark.template.mustache;
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
import org.json.JSONException;
import org.json.JSONObject;
import static spark.Spark.get;
import static spark.Spark.port;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;

public class test{
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
private static String streamToString(InputStream inputStream) {
    String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
    return text;
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
	s+=jString[2].split(":")[1];
	s+=jString[3].split(":")[1];
	s+=jString[4].split(":")[1];
	s+=jString[5].split(":")[1];
	s+=jString[6].split(":")[1];
	s+=jString[7].split(":")[1];

	//}
	return s;
  }
    public static void main(String[] args){
        port(getHerokuAssignedPort());
        String profiles = "<h1><a href='/profile'>Profile</a></h1>\n";
      

	String homeInfo=jsonGetRequest("https://raw.githubusercontent.com/andrewdoanutz/ucsb-cs56-dogwalker/master/home.json");

	Map map=new HashMap();
	map.put("listing",homeInfo);
	
	get("/home",(rq,rs)->profiles+map);
	String userProf = jsonParseConverter(jsonGetRequest("https://raw.githubusercontent.com/andrewdoanutz/ucsb-cs56-dogwalker/master/profile.json"));
	get("/profile",(rq,rs)->"<h1><a href='/home'>Home</a></h1>\n"+userProf);
    }
}
