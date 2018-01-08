package spirit.fitness.scanner.restful;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class JacksonRestApi {

	public static void getData() 
	{
		HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    String json = null;
	    try {
	      URL resetEndpoint = new URL("http://localhost:8080/v1/books");
	      connection = (HttpURLConnection) resetEndpoint.openConnection();
	      // Set request method to GET as required from the API
	      connection.setRequestMethod("GET");
	 
	      // Read the response
	      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      StringBuilder jsonSb = new StringBuilder();
	      String line = null;
	      while ((line = reader.readLine()) != null) {
	        jsonSb.append(line);
	      }
	      json = jsonSb.toString();
	 
	      // Converts JSON string to Java object
	      //ObjectMapper mapper = new ObjectMapper();
	      // Converts to an array of Book
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
