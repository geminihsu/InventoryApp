package spirit.fitness.scanner.restful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import spirit.fitness.scanner.common.Constrant;


/**
 * A simple Java REST GET example using the Apache HTTP library.
 * This executes a call against the Yahoo Weather API service, which is
 * actually an RSS service (http://developer.yahoo.com/weather/).
 * 
 * Try this Twitter API URL for another example (it returns JSON results):
 * http://search.twitter.com/search.json?q=%40apple
 * (see this url for more twitter info: https://dev.twitter.com/docs/using-search)
 * 
 * Apache HttpClient: http://hc.apache.org/httpclient-3.x/
 *
 */
public class HttpRestApi {

  
  //GET
  public static void getData() 
  {
	  HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    String retVal = null;
	    try {
	      URL resetEndpoint = new URL(Constrant.webUrl);
	      connection = (HttpURLConnection) resetEndpoint.openConnection();
	      //Set request method to GET as required from the API
	      connection.setRequestMethod("GET");
	 
	      // Read the response
	      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      StringBuilder jsonSb = new StringBuilder();
	      String line = null;
	      while ((line = reader.readLine()) != null) {
	        jsonSb.append(line);
	      }
	      retVal = jsonSb.toString();
	 
	      // print out the json response
	      System.out.println(retVal);
	 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      // Clean up
	      if (reader != null) {
	        try {
	          reader.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	      if (connection != null) {
	        connection.disconnect();
	      }
}
  }
  
  //POST
  public static void postData(String items, int index) 
  {
	  try {
	      URL url = new URL(Constrant.webUrl+ String.valueOf(index));
	      HttpURLConnection con = (HttpURLConnection) url.openConnection();
	      //Set the request method to POST as required from the API
	      con.setRequestMethod("POST");
	 
	      // Set the Content-Type to "application/json" as required from the API
	      con.setRequestProperty("Content-Type", "application/json");
	      con.setDoOutput(true);
	 
	      OutputStream os = con.getOutputStream();
	      //The book we want to create in JSON format
	      //String book = "{\"Seq\":"+91+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
		   
	      os.write(items.getBytes());
	      os.flush();
	      os.close();
	 
	      int responseCode = con.getResponseCode();
	 
	      System.out.println("Response Code :" + responseCode);
	 
	      if (responseCode == HttpURLConnection.HTTP_CREATED) {
	        System.out.println("Created book successfully.");
	      } else {
	        System.out.println("Created book failed.");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
  }
}
