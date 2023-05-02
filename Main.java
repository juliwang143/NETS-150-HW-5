// package com.twilio;

// import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Webscraper w = new Webscraper();
        boolean ongoing = true;
        // answers a question given a number until no more questions are asked
        while (ongoing) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the number to be answered: " +
                    "\n1. Given a movie, recommend top movies that have similar keywords." +
                    "\n2. Given a movie, recommend top movies that have similar genres." +
                    "\n3. Given a movie, find the movie's cast." +
                    "\n4. Given an actor and a movie they've acted in, find the colleagues who the actor has acted the most with." +
                    "\n5. Given the director of a particular movie, find the average rating of all the movies they have directed and output the top movie they have directed." +
                    "\n6. Given two actors, find movies they've worked in before. If they haven't worked in movies before, return the top 3 individual movies for each actor. " +
                    "\n7. Given a user's age, gender, and a movie, find the average rating from other users in that same demographic for the given movie.");
            String number = sc.nextLine();
            if (number.equals("1")) {
                System.out.print("Enter a movie title: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                String movieID = omdbRequest(title, year);
                System.out.println(w.getKeywordRecommendations(movieID, title));
            } else if (number.equals("2")) {
                System.out.print("Enter a movie title: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                String movieID = omdbRequest(title, year);
                System.out.println(w.getGenreRecommendations(movieID, title));
            } else if (number.equals("3")) {
                System.out.print("Enter a movie title: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                String movieID = omdbRequest(title, year);
                w.getMovieCast(movieID);
            } else if (number.equals("4")) {
                System.out.print("Enter the name of one actor/actress: ");
                String actorOne = sc.nextLine();
                System.out.print("Enter the title of a movie " + actorOne + " has been in: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                String movieIDOne = omdbRequest(title, year);
                System.out.print("Enter the name of another actor/actress: ");
                String actorTwo = sc.nextLine();
                System.out.print("Enter the title of a movie " + actorTwo + " has been in: ");
                title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                year = sc.nextLine();
                String movieIDTwo = omdbRequest(title, year);
                w.actorsBothActorsWorkedWith(actorOne, actorTwo, movieIDOne, movieIDTwo);
            } else if (number.equals("5")) {
                System.out.print("Enter a movie title: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                String movieID = omdbRequest(title, year);
                System.out.println(w.getDirectorRecommendations(movieID, title));
            } else if (number.equals("6")) {
                System.out.print("Enter the name of one actor/actress: ");
                String actorOne = sc.nextLine();
                System.out.print("Enter the title of a movie " + actorOne + " has been in: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                String movieIDOne = omdbRequest(title, year);
                System.out.print("Enter the name of another actor/actress: ");
                String actorTwo = sc.nextLine();
                System.out.print("Enter the title of a movie " + actorTwo + " has been in: ");
                title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                year = sc.nextLine();
                String movieIDTwo = omdbRequest(title, year);
                w.moviesActorsInTogether(actorOne, actorTwo, movieIDOne, movieIDTwo);
            } else if (number.equals("7")) {
                System.out.print("Enter a movie title: ");
                String title = sc.nextLine();
                System.out.print("Enter the release year of " + title + ": ");
                String year = sc.nextLine();
                System.out.print("Enter your gender (Male/Female): ");
                String gender = sc.nextLine();
                System.out.print("Enter your age: ");
                int age = Integer.parseInt(sc.nextLine());
                String movieID = omdbRequest(title, year);
                System.out.println(w.getDemographicRating(movieID, gender, age));
            } else {
                System.out.println(number + " is not a valid option.");
            }
            System.out.print("Do you want to ask another question (Yes/No)? ");
            String answer = sc.nextLine();
            if (!answer.equals("Yes")) {
                ongoing = false;
            }
        }
    }

    public static String omdbRequest(String movieTitle, String movieYear) {
        String[] titleWords = movieTitle.split(" ");
        String omdbRequest = "&t=";
        for (String word : titleWords) {
            omdbRequest += word + "+";
        }
        omdbRequest = omdbRequest.substring(0, omdbRequest.length() - 1) + "&y" + movieYear;
        String url = "http://www.omdbapi.com/?apikey=56dae4f7&" + omdbRequest;
        var uri = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .GET()
                .build();
        String movieID = "";
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String output = response.body();

            movieID = output.split("imdbID\":\"")[1].split("\"")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieID;
    }

}