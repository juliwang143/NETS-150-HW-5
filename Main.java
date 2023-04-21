// package com.twilio;

// import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
// import org.json.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws IOException {

        // // Create a neat value object to hold the URL
        // URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");

        // // Open a connection(?) on the URL(?) and cast the response(??)
        // HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // // Now it's "open", we can set the request method, headers etc.
        // connection.setRequestProperty("accept", "application/json");

        // // This line makes the request
        // InputStream responseStream = connection.getInputStream();

        // // Manually converting the response body InputStream to APOD using Jackson
        // ObjectMapper mapper = new ObjectMapper();
        // APOD apod = mapper.readValue(responseStream, APOD.class);

        // // Finally we have the response
        // System.out.println(apod.title);

        Scanner sc = new Scanner(System.in);
        System.out.print("Movie title: ");
        String movieTitle = sc.nextLine();
        String[] titleWords = movieTitle.split(" ");
        String requestTitle = "&t=";
        for (String word: titleWords) {
            requestTitle += word + "+";
        }
        requestTitle = requestTitle.substring(0, requestTitle.length() -1);
        System.out.println(requestTitle);
        String url = "http://www.omdbapi.com/?apikey=56dae4f7&" + requestTitle;

        var uri = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .GET()
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String output = response.body();
            System.out.println(response.statusCode());
            System.out.println(output);

            String movieID = output.split("imdbID\":\"")[1].split("\"")[0];
            System.out.println(movieID);
            //JSONObject obj = new JSONObject(jsonString);
            JSONObject obj = new JSONObject();
            //String pageName = obj.getJSONObject("pageInfo").getString("pageName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}