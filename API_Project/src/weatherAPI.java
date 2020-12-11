import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody; 

public class weatherAPI {
	
	private static final String api_url = "http://api.openweathermap.org/data/2.5/";
	private static final Moshi moshi = new Moshi.Builder().build();
	private static final JsonAdapter<JsonResponse> WEATHER_JSON_ADAPTER = moshi.adapter(JsonResponse.class);
	
	static class JsonResponse {
		List<Weather> weather;
		Main main; 
		String name; 
		
		
	
	}
	
	static class Weather {
		
		String main;
		String description;
		
		
	}
	
	static class Main {
		float temp;
		float temp_min;
		float temp_max;
	}
	
	public static String getWeather(String area) {
		
		String url = api_url;
		boolean zip; 
		try {
			Integer.parseInt(area);
			zip = true;
		} catch(Exception e) {
			zip = false;
		}
		if(zip) {
			url += "weather?zip=";
		}
		else {
			url += "weather?q=";
		}
		url += area + "&appid=6c69d8f01e341bfe25e9acb1ccdbbfeb";
	    String returnStatement = ""; 
	    String weatherDescription = "";
	    
	    try {
	    	
	    	OkHttpClient okHttpClient = new OkHttpClient(); 
	    	Request request = new Request.Builder()
	    			.url(url)
	    			.get()
	    			.addHeader("Cache-Control", "no-cache")
	    			.build();
	    	Response response = okHttpClient.newCall(request).execute();
	    	System.out.println(request);
	    	ResponseBody body = response.body();
	    	
	    	JsonResponse result = WEATHER_JSON_ADAPTER.fromJson(body.source());
	    	
	    	try {
	    		for(Weather weather : result.weather) {
	    			weatherDescription = weather.description;
	    		}
	    		returnStatement = "The weather in " + result.name + " is going to be " + weatherDescription + 
	    				" with a temperature of " + result.main.temp + ". Today will have a high of " + result.main.temp_max + " and a low of " + result.main.temp_min;
	    		
	    		
	    	}
	    	catch (Exception e) {
	    		returnStatement = "null";
	    	}
	    	
	    }catch (Exception e){
	    	e.printStackTrace();
	    	System.out.println(e);
	    	
	    }
	    return returnStatement; 

	}
	
	
	
	
	

}
