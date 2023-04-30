import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Webscraper {

    private HashMap<String, String> castMap;
    private Document currentDoc;
    private String baseUrl;

    // TODO
    private TreeMap<String, Integer> colleaguesFreqMap;

    String movieUrl;

    public Webscraper() {
        this.castMap = new HashMap<String, String>();
        this.movieUrl = "https://www.imdb.com/title/";
        this.baseUrl = "https://www.imdb.com";

        // TODO
        this.colleaguesFreqMap = new TreeMap<String, Integer>();
    }
    
    public void getMovieCast(String movieId) {
        String url = this.movieUrl + movieId;
        Document movieDoc = null;

        // TODO
        // System.out.println("Movie url: " + url);

        try {
            movieDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }

        Elements sectionElements = movieDoc.select("section");
        Element topCastSection = null;
        
        for (Element sectionElement : sectionElements) {
            // TODO before
            // if (sectionElement.text().contains("Top cast")) {
            //     topCastSection = sectionElement;
            //     System.out.println(topCastSection);
            //     break;
            // }

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

    public void getMovieCastByUrl(String url, TreeMap<String, Integer> colleaguesFreqMap) {
        // String movieUrl = this.baseUrl + url;
        // TODO
        String movieUrl = url;
        this.castMap = new HashMap<String, String>();

        Document movieDoc = null;
        
        // TODO
        // System.out.println("Movie url: " + url);


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

        // TODO
        for (String actorName : castMap.keySet()) {
            // TODO this or without this?
            colleaguesFreqMap.put(actorName, colleaguesFreqMap.getOrDefault(actorName, 0) + 1);
            // this.colleaguesFreqMap.put(actorName, this.colleaguesFreqMap.getOrDefault(actorName, 0) + 1);
        }
    }

    public boolean isActorInMovie(String url, String actorName) {
        // String movieUrl = this.baseUrl + url;
        // TODO
        // System.out.println("ohterrrrrr: " + actorName);

        String movieUrl = url;
        this.castMap = new HashMap<String, String>();

        Document movieDoc = null;
        
        // TODO
        // System.out.println("Movie url: " + url);


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
                // System.out.println("other cast: " + aElement.text());
                if (aElement.text().contains(actorName)) {
                    // System.out.println("!!");
                    return true;
                }
            }
        }

        return false;
    }

    public void getMostPopularColleagues(String actorUrl, String actorName) {
        HashMap<String, String> moviesOfColleaguesMap = new HashMap<String, String>();
        // TreeMap<String, Integer> colleaguesFreqMap = new TreeMap<String, Integer>();
        colleaguesFreqMap = new TreeMap<String, Integer>();

        String url = this.baseUrl + actorUrl;

        Document actorDoc = null;
        try {
            actorDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return;
        }

        Elements divElements = actorDoc.select("div");
        Element filmographySection = null;
        
        // TODO
        for (Element divElement : divElements) {
            if (divElement.attr("data-testid").contains("Filmography")) {
                filmographySection = divElement;
                break;
            }
        }

        if (filmographySection == null) {
            System.out.println("No filmography section found.");
            return;
        }

        Elements sectionElements = filmographySection.select("section");
        if (sectionElements.size() < 2) {
            System.out.println("No filmography section found.");
            return;
        }

        Element creditsSection = sectionElements.get(1);

        Elements divChildren = creditsSection.children();
        if (divChildren.size() < 5) {
            System.out.println("No filmography section found.");
            return;
        }

        Element divChild = divChildren.get(4);

        divChildren = divChild.children();

        if (divChildren.size() < 2) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(1);

        divChildren = divChild.children();
        if (divChildren.size() < 1) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(0);

        divChildren = divChild.children();
        if (divChildren.size() < 4) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(3);

        Element ulElement = divChild.selectFirst("ul");
        if (ulElement == null) {
            System.out.println("No previous movies found.");
            return;
        }

        Elements liElements = ulElement.select("li");
        for (Element liElement : liElements) {
            Elements aElements = liElement.select("a");
            if (aElements.size() >= 2) {
                // TODO: A link is found for this movie
                Element aElement = aElements.get(1);
                String href = aElement.attr("href");
                String movieTitle = aElement.text();
                moviesOfColleaguesMap.put(movieTitle, href);
            }
        }

        for (String movieTitle : moviesOfColleaguesMap.keySet()) {
            String href = this.baseUrl + moviesOfColleaguesMap.get(movieTitle);
            getMovieCastByUrl(href, colleaguesFreqMap);
        }

        // TODO
        // colleaguesFreqMap.entrySet().stream()
        //         .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
        //         .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));


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
    






    // TODO find an actor's ID given a movie they are in and their name



    // TODO all the actors?
    /*** 
    Given two actors, find all the actors they've both worked with before. 
    If there are no actors they've both worked with, return no actors. 
    ***/
    public void actorsBothActorsWorkedWith(String actor1Name, String actor2Name, String actor1Url, String actor2Url) {
        // Find actors that have worked with actor #1
        HashMap<String, String> moviesOfActor1Map = new HashMap<String, String>();
        TreeMap<String, Integer> colleaguesOfActor1Map = new TreeMap<String, Integer>();

        String url1 = this.baseUrl + actor1Url;

        Document actor1Doc = null;
        try {
            actor1Doc = Jsoup.connect(url1).get();
        } catch (IOException e) {
            System.out.println("The URL " + url1 + " could not be accessed.");
            return;
        }

        Elements divElements = actor1Doc.select("div");
        Element filmographySection = null;
        
        for (Element divElement : divElements) {
            if (divElement.attr("data-testid").contains("Filmography")) {
                filmographySection = divElement;
                break;
            }
        }

        if (filmographySection == null) {
            System.out.println("No filmography section found.");
            return;
        }

        Elements sectionElements = filmographySection.select("section");
        if (sectionElements.size() < 2) {
            System.out.println("No filmography section found.");
            return;
        }

        Element creditsSection = sectionElements.get(1);

        Elements divChildren = creditsSection.children();
        if (divChildren.size() < 5) {
            System.out.println("No filmography section found.");
            return;
        }

        Element divChild = divChildren.get(4);

        divChildren = divChild.children();

        if (divChildren.size() < 2) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(1);

        divChildren = divChild.children();
        if (divChildren.size() < 1) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(0);

        divChildren = divChild.children();
        if (divChildren.size() < 4) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(3);

        Element ulElement = divChild.selectFirst("ul");
        if (ulElement == null) {
            System.out.println("No previous movies found.");
            return;
        }

        Elements liElements = ulElement.select("li");
        for (Element liElement : liElements) {
            Elements aElements = liElement.select("a");
            if (aElements.size() >= 2) {
                // TODO: A link is found for this movie
                Element aElement = aElements.get(1);
                String href = aElement.attr("href");
                String movieTitle = aElement.text();
                moviesOfActor1Map.put(movieTitle, href);
            }
        }

        for (String movieTitle : moviesOfActor1Map.keySet()) {
            String href = this.baseUrl + moviesOfActor1Map.get(movieTitle);
            getMovieCastByUrl(href, colleaguesOfActor1Map);
        }

        // Find actors that have worked with actor #2
        HashMap<String, String> moviesOfActor2Map = new HashMap<String, String>();
        TreeMap<String, Integer> colleaguesOfActor2Map = new TreeMap<String, Integer>();

        String url2 = this.baseUrl + actor1Url;

        Document actor2Doc = null;
        try {
            actor2Doc = Jsoup.connect(url2).get();
        } catch (IOException e) {
            System.out.println("The URL " + url2 + " could not be accessed.");
            return;
        }

        divElements = actor2Doc.select("div");
        filmographySection = null;
        
        for (Element divElement : divElements) {
            if (divElement.attr("data-testid").contains("Filmography")) {
                filmographySection = divElement;
                break;
            }
        }

        if (filmographySection == null) {
            System.out.println("No filmography section found.");
            return;
        }

        sectionElements = filmographySection.select("section");
        if (sectionElements.size() < 2) {
            System.out.println("No filmography section found.");
            return;
        }

        creditsSection = sectionElements.get(1);

        divChildren = creditsSection.children();
        if (divChildren.size() < 5) {
            System.out.println("No filmography section found.");
            return;
        }

        divChild = divChildren.get(4);

        divChildren = divChild.children();

        if (divChildren.size() < 2) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(1);

        divChildren = divChild.children();
        if (divChildren.size() < 1) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(0);

        divChildren = divChild.children();
        if (divChildren.size() < 4) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(3);

        ulElement = divChild.selectFirst("ul");
        if (ulElement == null) {
            System.out.println("No previous movies found.");
            return;
        }

        liElements = ulElement.select("li");
        for (Element liElement : liElements) {
            Elements aElements = liElement.select("a");
            if (aElements.size() >= 2) {
                // TODO: A link is found for this movie
                Element aElement = aElements.get(1);
                String href = aElement.attr("href");
                String movieTitle = aElement.text();
                moviesOfActor2Map.put(movieTitle, href);
            }
        }

        for (String movieTitle : moviesOfActor2Map.keySet()) {
            String href = this.baseUrl + moviesOfActor2Map.get(movieTitle);
            getMovieCastByUrl(href, colleaguesOfActor2Map);
        }

        HashSet<String> colleaguesOfBoth = new HashSet<String>();

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

    /*** 
    Given two actors, find movies they've worked in before. 
    If they haven't worked in movies before, return no movies. 
    ***/
    public void moviesActorsInTogether(String actor1Name, String actor2Name, String actor1Url, String actor2Url) {
        HashSet<String> moviesTogether = new HashSet<String>();

        // Find movies that actor #1 has acted in
        HashMap<String, String> actor1Movies = new HashMap<String, String>();

        // TODO
        Document actor1Doc = null;
        try {
            actor1Doc = Jsoup.connect(this.baseUrl + actor1Url).get();
        } catch (IOException e) {
            System.out.println("The URL " + this.baseUrl + actor1Url + " could not be accessed.");
            return;
        }

        Elements divElements = actor1Doc.select("div");
        Element filmographySection = null;
        
        // TODO
        for (Element divElement : divElements) {
            if (divElement.attr("data-testid").contains("Filmography")) {
                filmographySection = divElement;
                break;
            }
        }

        if (filmographySection == null) {
            System.out.println("No filmography section found.");
            return;
        }

        Elements sectionElements = filmographySection.select("section");
        if (sectionElements.size() < 2) {
            System.out.println("No filmography section found.");
            return;
        }

        Element creditsSection = sectionElements.get(1);

        Elements divChildren = creditsSection.children();
        if (divChildren.size() < 5) {
            System.out.println("No filmography section found.");
            return;
        }

        Element divChild = divChildren.get(4);

        divChildren = divChild.children();

        if (divChildren.size() < 2) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(1);

        divChildren = divChild.children();
        if (divChildren.size() < 1) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(0);

        divChildren = divChild.children();
        if (divChildren.size() < 4) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(3);

        Element ulElement = divChild.selectFirst("ul");
        if (ulElement == null) {
            System.out.println("No previous movies found.");
            return;
        }

        Elements liElements = ulElement.select("li");
        for (Element liElement : liElements) {
            Elements aElements = liElement.select("a");
            if (aElements.size() >= 2) {
                // TODO: A link is found for this movie
                Element aElement = aElements.get(1);
                String href = aElement.attr("href");
                String movieTitle = aElement.text();
                actor1Movies.put(movieTitle, href);
            }
        }

        for (String movieTitle : actor1Movies.keySet()) {
            String href = this.baseUrl + actor1Movies.get(movieTitle);

            // TODO new 
            if (isActorInMovie(href, actor2Name)) {
                moviesTogether.add(movieTitle);
            }
        }

        // TODO debugging purposes
        for (String movie: moviesTogether) {
            System.out.println("Movie together 1: " + movie);
        }

        // TODO
        // Find movies that actor #2 has acted in
        HashMap<String, String> actor2Movies = new HashMap<String, String>();

        Document actor2Doc = null;
        try {
            actor2Doc = Jsoup.connect(this.baseUrl + actor2Url).get();
        } catch (IOException e) {
            System.out.println("The URL " + this.baseUrl + actor2Url + " could not be accessed.");
            return;
        }

        divElements = actor2Doc.select("div");
        filmographySection = null;
        
        // TODO
        for (Element divElement : divElements) {
            if (divElement.attr("data-testid").contains("Filmography")) {
                filmographySection = divElement;
                break;
            }
        }

        if (filmographySection == null) {
            System.out.println("No filmography section found.");
            return;
        }

        sectionElements = filmographySection.select("section");
        if (sectionElements.size() < 2) {
            System.out.println("No filmography section found.");
            return;
        }

        creditsSection = sectionElements.get(1);

        divChildren = creditsSection.children();
        if (divChildren.size() < 5) {
            System.out.println("No filmography section found.");
            return;
        }

        divChild = divChildren.get(4);

        divChildren = divChild.children();

        if (divChildren.size() < 2) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(1);

        divChildren = divChild.children();
        if (divChildren.size() < 1) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(0);

        divChildren = divChild.children();
        if (divChildren.size() < 4) {
            System.out.println("No previous movies found.");
            return;
        }

        divChild = divChildren.get(3);

        ulElement = divChild.selectFirst("ul");
        if (ulElement == null) {
            System.out.println("No previous movies found.");
            return;
        }

        liElements = ulElement.select("li");
        for (Element liElement : liElements) {
            Elements aElements = liElement.select("a");
            if (aElements.size() >= 2) {
                // TODO: A link is found for this movie
                Element aElement = aElements.get(1);
                String href = aElement.attr("href");
                String movieTitle = aElement.text();
                actor1Movies.put(movieTitle, href);
            }
        }

        for (String movieTitle : actor2Movies.keySet()) {
            String href = this.baseUrl + actor2Movies.get(movieTitle);

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
