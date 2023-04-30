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
import java.util.TreeMap;
// import org.json.*;

// import org.json.JSONException;
// import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws IOException {

        // TODO year

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
            // System.out.println(response.statusCode());
            // System.out.println(output);

            String movieID = output.split("imdbID\":\"")[1].split("\"")[0];
            System.out.println(movieID);

            Webscraper ws = new Webscraper();
            // ws.getMovieCast(movieID);

            // ws.getMostPopularColleagues("/name/nm0000288/?ref_=tt_cl_t_1", "Christian Bale");

            // ws.getMostPopularColleagues("/name/nm3053338/?ref_=tt_cl_t_2", "Margot Robbie");


            // TODO doesn't work
            // ws.getMostPopularColleagues("/name/nm0000158/?ref_=nv_sr_srsg_1_tt_2_nm_5_q_tom%2520han", "Tom Hanks");

            // TODO doesn't work
            ws.getMostPopularColleagues("/name/nm0000212/?ref_=nv_sr_srsg_0_tt_0_nm_8_q_meg%2520r", "Meg Ryan");


            // ws.moviesActorsInTogether("Margot Robbie", "Christian Bale", "/name/nm3053338/?ref_=tt_cl_t_2", "/name/nm0000288/?ref_=tt_cl_t_1"); 


            // ws.moviesActorsInTogether("Scarlett Johansson", "Chris Evans", "/name/nm0424060/?ref_=nv_sr_srsg_1_tt_2_nm_5_q_scarlett", "/name/nm0262635/?ref_=nv_sr_srsg_0_tt_1_nm_7_q_chris%2520evans"); 

            // ws.getMovieCast("tt4154664");

            // ws.actorsBothActorsWorkedWith("Scarlett Johansson", "Chris Evans", "/name/nm0424060/?ref_=nv_sr_srsg_1_tt_2_nm_5_q_scarlett", "/name/nm0262635/?ref_=nv_sr_srsg_0_tt_1_nm_7_q_chris%2520evans"); 


            // DOesn't work
            // ws.actorsBothActorsWorkedWith("Leonardo Dicaprio", "Cillian Murphy", "/name/nm0000138/?ref_=nv_sr_srsg_1_tt_1_nm_6_q_leona", "/name/nm0614165/?ref_=nv_sr_srsg_0_tt_2_nm_6_q_cilli"); 


            // TODO
            // ws.getMovieCastByUrl("https://www.imdb.com/title/tt10304142/?ref_=nm_flmg_t_2_act", new TreeMap<String, Integer>());
           
            // TODO below is working
            // ws.getMovieCastByUrl("https://www.imdb.com/title/tt1800241/", new TreeMap<String, Integer>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}