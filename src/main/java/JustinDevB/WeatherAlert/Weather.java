package JustinDevB.WeatherAlert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.extra.Interval;

public class Weather {

	public Weather() throws JSONException, IOException {
		System.out.println(getTime(getJsonObject()));
	}

	/**
	 * Get a String representation of how long the weather will last
	 * 
	 * @return
	 */
	private String getTime(String json) {

		String[] parts = json.split(":"); // Split up result by ':'

		String[] string = parts[4].split(","); // Return the value after / since it's not ':' separated

		String time = String.join(":", parts[1], parts[2], parts[3], string[0]); // Join all of the parts back into the
																					// original value. This leaves us
																					// with a String that only contains
																					// the time
		
		Interval interval = Interval.parse(time.replaceAll("\"", ""));
		
		Instant start = interval.getStart();
		Instant stop = interval.getEnd();
		
		System.out.println("start : " + start);
		System.out.println("stop  : " + stop);
		System.out.println("duration : " + interval.toDuration());
		return time.replaceAll("\"", "");
	}
	
	private void printWeather(String json) {
		
	} 

	private String getJsonObject() throws JSONException, IOException {
		JSONObject json = readJsonFromUrl("https://api.weather.gov/gridpoints/IND/71,58");
		// System.out.println(json.toString());
		// Return Properties.Weather.values
		String result = json.getJSONObject("properties").getJSONObject("weather").getJSONArray("values").get(0)
				.toString(); // result returns a date/time format like: 2021-04-19T17:00:00+00:00/P1DT1H
		
		String test = json.getJSONObject("properties").getJSONObject("weather").getJSONArray("values").get(0).toString();
		System.out.println("test : " + test);
		System.out.println("result : " + result);

		return result;
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}
