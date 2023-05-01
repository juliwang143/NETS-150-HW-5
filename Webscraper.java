import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Webscraper {

    private HashMap<String, String> castMap;
    private String baseUrl;

    // TODO
    private TreeMap<String, Integer> colleaguesFreqMap;

    String movieUrl;

    // TODO my base url is different than Dora's
    public Webscraper() {
        this.castMap = new HashMap<String, String>();
        this.movieUrl = "https://www.imdb.com/title/";
        this.baseUrl = "https://www.imdb.com";

        this.colleaguesFreqMap = new TreeMap<String, Integer>();
    }
    
    public void getMovieCast(String movieId) {
        String url = this.movieUrl + movieId;
        Document movieDoc = null;

        try {
            movieDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }

        Elements sectionElements = movieDoc.select("section");
        Element topCastSection = null;
        
        for (Element sectionElement : sectionElements) {
            if (sectionElement.attr("data-testid").equals("title-cast")) {
                topCastSection = sectionElement;
                break;
            }
        }

        if (topCastSection == null) {
            System.out.println("No movie cast section found for movie #" + movieId);
            return;
        }

        Elements divElements = topCastSection.children();
        if (divElements.size() < 2) {
            System.out.println("No movie cast section found for movie #" + movieId);
            return;
        }

        Element topCastGridDiv = divElements.get(1);
        Elements aElements = topCastGridDiv.select("a");

        for (Element aElement : aElements) {
            if (aElement.attr("data-testid").equals("title-cast-item__actor")) {
                String actorName = aElement.text();
                String actorUrl = aElement.attr("href");
                this.castMap.put(actorName, actorUrl);
            }
        }

        for (String actorName : castMap.keySet()) {
            System.out.println(actorName);
        }
    }

    // Helper method for getMostPopularColleagues
    public void getMovieCastByUrl(String url, TreeMap<String, Integer> colleaguesFreqMap) {
        String movieUrl = url;
        this.castMap = new HashMap<String, String>();

        Document movieDoc = null;

        try {
            movieDoc = Jsoup.connect(movieUrl).get();
        } catch (IOException e) {
            System.out.println("The URL " + movieUrl + " could not be accessed.");
            return;
        }

        Elements sectionElements = movieDoc.select("section");
        Element topCastSection = null;
        
        for (Element sectionElement : sectionElements) {
            if (sectionElement.attr("data-testid").equals("title-cast")) {
                topCastSection = sectionElement;
                break;
            }
        }

        if (topCastSection == null) {
            System.out.println("No movie cast section found for movie url " + movieUrl);
            return;
        }

        Elements divElements = topCastSection.children();
        if (divElements.size() < 2) {
            System.out.println("No movie cast section found for movie url " + movieUrl);
            return;
        }

        Element topCastGridDiv = divElements.get(1);
        Elements aElements = topCastGridDiv.select("a");

        for (Element aElement : aElements) {
            if (aElement.attr("data-testid").equals("title-cast-item__actor")) {
                String actorName = aElement.text();
                String actorUrl = aElement.attr("href");
                this.castMap.put(actorName, actorUrl);
            }
        }

        for (String actorName : castMap.keySet()) {
            colleaguesFreqMap.put(actorName, colleaguesFreqMap.getOrDefault(actorName, 0) + 1);
        }
    }

    public boolean isActorInMovie(String url, String actorName) {
        String movieUrl = url;
        this.castMap = new HashMap<String, String>();

        Document movieDoc = null;

        try {
            movieDoc = Jsoup.connect(movieUrl).get();
        } catch (IOException e) {
            System.out.println("The URL " + movieUrl + " could not be accessed.");
            return false;
        }

        Elements sectionElements = movieDoc.select("section");
        Element topCastSection = null;
        
        for (Element sectionElement : sectionElements) {
            if (sectionElement.attr("data-testid").equals("title-cast")) {
                topCastSection = sectionElement;
                break;
            }
        }

        if (topCastSection == null) {
            System.out.println("No movie cast section found for movie url " + movieUrl);
            return false;
        }

        Elements divElements = topCastSection.children();
        if (divElements.size() < 2) {
            System.out.println("No movie cast section found for movie url " + movieUrl);
            return false;
        }

        Element topCastGridDiv = divElements.get(1);
        Elements aElements = topCastGridDiv.select("a");

        for (Element aElement : aElements) {
            if (aElement.attr("data-testid").equals("title-cast-item__actor")) {
                if (aElement.text().contains(actorName)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Find an actor's ID given a movie they are in and their name
    public String getActorUrl(String actor, String movieId) {
        String url = this.movieUrl + movieId;
        Document movieDoc = null;

        try {
            movieDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return "";
        }

        Elements sectionElements = movieDoc.select("section");
        Element topCastSection = null;
        
        for (Element sectionElement : sectionElements) {
            if (sectionElement.attr("data-testid").equals("title-cast")) {
                topCastSection = sectionElement;
                break;
            }
        }

        if (topCastSection == null) {
            System.out.println("When trying to get actor's url, no movie cast section found for movie #" + movieId);
            return "";
        }

        Elements divElements = topCastSection.children();
        if (divElements.size() < 2) {
            System.out.println("When trying to get actor's url, No movie cast section found for movie #" + movieId);
            return "";
        }

        Element topCastGridDiv = divElements.get(1);
        Elements aElements = topCastGridDiv.select("a");

        for (Element aElement : aElements) {
            if (aElement.attr("data-testid").equals("title-cast-item__actor")) {
                String actorName = aElement.text();
                String actorUrl = aElement.attr("href");

                if (actorName.equals(actor)) {
                    return actorUrl;
                }
            }
        }

        return "";
    }

    /*** 
    Given two actors, find all the actors they've both worked with before. 
    If there are no actors they've both worked with, return no actors. 
    ***/
    public void actorsBothActorsWorkedWith(String actor1Name, String actor2Name, String movie1Id, String movie2Id) {
        String actor1Url = null;
        String actor2Url = null;

        actor1Url = getActorUrl(actor1Name, movie1Id);
        actor2Url = getActorUrl(actor2Name, movie2Id);

        System.out.println("dof");
        System.out.println(actor1Url);
        System.out.println(actor2Url);

        HashMap<String, String> moviesOfActor1Map = new HashMap<String, String>();
        TreeMap<String, Integer> colleaguesOfActor1Map = new TreeMap<String, Integer>();

        String url = this.baseUrl + actor1Url;
    
        Document actorDoc = null;
        try {
            actorDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }
    
        Elements directedList = actorDoc.select("div");
        Document directedMovieDoc;
        for (int i = 1; i < directedList.size(); i++) {
            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actor") || 
            directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actress")) {
                Elements previousMoviesList = directedList.get(i).select
                        ("div.ipc-accordion__item__content_inner.accordion-content");
                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                        .select("div.ipc-metadata-list-summary-item__tc");
                for (Element movie : previousMovies) {
                    String movieTitle = movie.select("a").text();

                    String mType = movie.text().split(movieTitle)[1];
                    if (mType.contains("Short") || mType.contains("Video") || mType.contains("TV")) {
                        continue;
                    }

                    String href = movie.select("a").attr("href");
                    moviesOfActor1Map.put(movieTitle, href);
                }
            }
        }
    
        for (String movieTitle : moviesOfActor1Map.keySet()) {
            String href = this.baseUrl + moviesOfActor1Map.get(movieTitle);
            getMovieCastByUrl(href, colleaguesOfActor1Map);
        }

        // Find actors that have worked with actor #2
        HashMap<String, String> moviesOfActor2Map = new HashMap<String, String>();
        TreeMap<String, Integer> colleaguesOfActor2Map = new TreeMap<String, Integer>();

        url = this.baseUrl + actor2Url;
    
        actorDoc = null;
        try {
            actorDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }
    
        directedList = actorDoc.select("div");
        for (int i = 1; i < directedList.size(); i++) {
            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actor") || 
            directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actress")) {
                Elements previousMoviesList = directedList.get(i).select
                        ("div.ipc-accordion__item__content_inner.accordion-content");
                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                        .select("div.ipc-metadata-list-summary-item__tc");
                for (Element movie : previousMovies) {
                    String movieTitle = movie.select("a").text();

                    String mType = movie.text().split(movieTitle)[1];
                    if (mType.contains("Short") || mType.contains("Video") || mType.contains("TV")) {
                        continue;
                    }

                    String href = movie.select("a").attr("href");
                    moviesOfActor2Map.put(movieTitle, href);
                }
            }
        }
    
        for (String movieTitle : moviesOfActor2Map.keySet()) {
            String href = this.baseUrl + moviesOfActor2Map.get(movieTitle);
            getMovieCastByUrl(href, colleaguesOfActor2Map);
        }

        // TODO both logic
        TreeSet<String> colleaguesOfBoth = new TreeSet<String>();

        for (String colleague : colleaguesOfActor1Map.keySet()) {
            if (colleaguesOfActor2Map.keySet().contains(colleague)) {
                colleaguesOfBoth.add(colleague);
            }
        }

        for (String colleague : colleaguesOfActor2Map.keySet()) {
            if (colleaguesOfActor1Map.keySet().contains(colleague)) {
                colleaguesOfBoth.add(colleague);
            }
        }

        // TODO comment
        colleaguesOfBoth.remove(actor1Name);
        colleaguesOfBoth.remove(actor2Name);

        if (colleaguesOfBoth.size() >= 1) {
            System.out.println(actor1Name + " and " + actor2Name + " have both acted with the following actor/actresses together:");
            for (String colleague : colleaguesOfBoth) {
                System.out.println(colleague);
            }
        } else {
            System.out.println(actor1Name + " and " + actor2Name + " have never acted with the same actors/actresses together.");
        }
    }

    public void getMostPopularColleagues(String actorUrl, String actorName) {
        HashMap<String, String> moviesOfColleaguesMap = new HashMap<String, String>();
        colleaguesFreqMap = new TreeMap<String, Integer>();

        String url = this.baseUrl + actorUrl;

        Document actorDoc = null;
        try {
            actorDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }


        Elements directedList = actorDoc.select("div");
        Document directedMovieDoc;
        for (int i = 1; i < directedList.size(); i++) {
            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actor") || 
            directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actress")) {
                Elements previousMoviesList = directedList.get(i).select
                        ("div.ipc-accordion__item__content_inner.accordion-content");
                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                        .select("div.ipc-metadata-list-summary-item__tc");
                for (Element movie : previousMovies) {
                    String movieTitle = movie.select("a").text();
                    String href = movie.select("a").attr("href");
                    moviesOfColleaguesMap.put(movieTitle, href);
                }
            }
        }

        for (String movieTitle : moviesOfColleaguesMap.keySet()) {
            String href = this.baseUrl + moviesOfColleaguesMap.get(movieTitle);
            getMovieCastByUrl(href, colleaguesFreqMap);
        }

        ArrayList<String> frequentColleagues = new ArrayList<String>();
        for (String colleague : colleaguesFreqMap.keySet()) {
            if (colleaguesFreqMap.get(colleague) >= 2) {
                frequentColleagues.add(colleague);
            }
        }

        if (colleaguesFreqMap.size() >= 1) {
            System.out.println(actorName + " has worked with the following actors/actresses");
            for (String colleague : frequentColleagues) {
                if (colleague.equals(actorName)) {
                    continue;
                }
                System.out.println(colleague + ": " + colleaguesFreqMap.get(colleague) + " times");
            }
        } else {
            System.out.println(actorName + " has not worked with any actors/actresses 2 or more times.");
        }
        
    }

    /*** 
    Given two actors, find movies they've worked in before. 
    If they haven't worked in movies before, return no movies. 
    ***/
    public void moviesActorsInTogether(String actor1Name, String actor2Name, String movie1Id, String movie2Id) {
        String actor1Url = null;
        String actor2Url = null;

        actor1Url = getActorUrl(actor1Name, movie1Id);
        actor2Url = getActorUrl(actor2Name, movie2Id);

        HashMap<String, String> moviesOfActor1Map = new HashMap<String, String>();
        TreeMap<String, Integer> colleaguesOfActor1Map = new TreeMap<String, Integer>();

        String url = this.baseUrl + actor1Url;
    
        Document actorDoc = null;
        try {
            actorDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }
    
        Elements directedList = actorDoc.select("div");
        Document directedMovieDoc;
        for (int i = 1; i < directedList.size(); i++) {
            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actor") || 
            directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actress")) {
                Elements previousMoviesList = directedList.get(i).select
                        ("div.ipc-accordion__item__content_inner.accordion-content");
                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                        .select("div.ipc-metadata-list-summary-item__tc");
                for (Element movie : previousMovies) {
                    String movieTitle = movie.select("a").text();

                    String mType = movie.text().split(movieTitle)[1];
                    if (mType.contains("Short") || mType.contains("Video") || mType.contains("TV")) {
                        continue;
                    }

                    String href = movie.select("a").attr("href");
                    moviesOfActor1Map.put(movieTitle, href);
                }
            }
        }
    
        for (String movieTitle : moviesOfActor1Map.keySet()) {
            String href = this.baseUrl + moviesOfActor1Map.get(movieTitle);
            getMovieCastByUrl(href, colleaguesOfActor1Map);
        }

        // Find actors that have worked with actor #2
        HashMap<String, String> moviesOfActor2Map = new HashMap<String, String>();
        TreeMap<String, Integer> colleaguesOfActor2Map = new TreeMap<String, Integer>();

        url = this.baseUrl + actor2Url;
    
        actorDoc = null;
        try {
            actorDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }
    
        directedList = actorDoc.select("div");
        for (int i = 1; i < directedList.size(); i++) {
            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actor") || 
            directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-actress")) {
                Elements previousMoviesList = directedList.get(i).select
                        ("div.ipc-accordion__item__content_inner.accordion-content");
                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                        .select("div.ipc-metadata-list-summary-item__tc");
                for (Element movie : previousMovies) {
                    String movieTitle = movie.select("a").text();

                    String mType = movie.text().split(movieTitle)[1];
                    if (mType.contains("Short") || mType.contains("Video") || mType.contains("TV")) {
                        continue;
                    }

                    String href = movie.select("a").attr("href");
                    moviesOfActor2Map.put(movieTitle, href);
                }
            }
        }
    
        for (String movieTitle : moviesOfActor2Map.keySet()) {
            String href = this.baseUrl + moviesOfActor2Map.get(movieTitle);
            getMovieCastByUrl(href, colleaguesOfActor2Map);
        }

        TreeSet<String> moviesTogether = new TreeSet<String>();

        // TODO logic for both
        for (String movieTitle : moviesOfActor1Map.keySet()) {
            String href = this.baseUrl + moviesOfActor1Map.get(movieTitle);

            // TODO new 
            if (isActorInMovie(href, actor2Name)) {
                moviesTogether.add(movieTitle);
            }
        }

        // TODO
        for (String movieTitle : moviesOfActor2Map.keySet()) {
            String href = this.baseUrl + moviesOfActor2Map.get(movieTitle);

            // TODO new 
            if (isActorInMovie(href, actor1Name)) {
                moviesTogether.add(movieTitle);
            }
        }

        if (moviesTogether.size() >= 1) {
            System.out.println(actor1Name + " and " + actor2Name + " have been in the following movies together:");
            for (String movie : moviesTogether) {
                System.out.println(movie);
            }
        } else {
            System.out.println(actor1Name + " and " + actor2Name + " have been in no movies together.");
        }
    }

}
