TODO change parameters

TODO method that gets a method's id based on the title and year, move it to webscreaper




Only look at previous movies for an actor, not upcoming movies

We expect the names of the movies and actors to be spelled exactly, and the first letter of every word to be capitalized. 
We also expect no extra spaces before or after the names of movies or actors. 

talk about different types of cllosure

Takes the top 15 previous movies or less for an actor so the number of times actors have worked together might not be accurate. 
For instace, Amy Adams and Christian Bale have worked more than 3 times together. Because of the accordion list, and needing to press the button 

Scarlett Johansson and Chris Evans
The movies in common with both of their top 15 movies are Avengers: Endgame, Avengers: Infinity War, and Captain Marvel. 
However, our program only outputs the first two. This is because Captain Marvel's top cast featured on its IMDb page 
do not include Scarlett Johansson or Chris Evans. 

Because our program iterates over the top 15 previous movies for a given actor, say actor #1, and checks if the second actor, actor #2,
appears in the cast of any of those movies, and vice versa for actor #2, our program does not account for these rare cases.




TODOODODDOO


We treat the 15 top previous movies that show up as the movies that an actor has acted in, even if they've acted in more. This is 
because 


Leonardo Dicaprio doesn't work??? 

producer first

15 most recent movies

when giving actor and 
have to give it a mvoie where


actress not just actor


if statement for 



delete print statements. check over them all







import java.util.HashMap;
import java.util.HashSet;

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
    
    // TODO: Tested
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

    // TODO did not test
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

        // TODO debuggin
        // System.out.println("~~" + url);
        // TODO
        for (String actorName : castMap.keySet()) {
            // TODO this or without this?
            colleaguesFreqMap.put(actorName, colleaguesFreqMap.getOrDefault(actorName, 0) + 1);
            // System.out.println(actorName);
            // this.colleaguesFreqMap.put(actorName, this.colleaguesFreqMap.getOrDefault(actorName, 0) + 1);
        }
    }

    // TODO tested
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
            System.out.println("movieeee: " + movieTitle);
            String href = this.baseUrl + moviesOfColleaguesMap.get(movieTitle);
            getMovieCastByUrl(href, colleaguesFreqMap);
        }

        // TODO
        // colleaguesFreqMap.entrySet().stream()
        //         .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
        //         .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));
        colleaguesFreqMap.remove(actorName);


        ArrayList<String> frequentColleagues = new ArrayList<String>();
        for (String colleague : colleaguesFreqMap.keySet()) {
            if (colleaguesFreqMap.get(colleague) >= 2) {
                frequentColleagues.add(colleague);
            }
        }

        // colleaguesFreqMap.remove(actorName);

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
    public String getActorUrl(String actor, String movieId) {
        String url = this.movieUrl + movieId;
        Document movieDoc = null;

        // TODO
        // System.out.println("Movie url: " + url);

        try {
            movieDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("The URL " + url + " could not be accessed.");
            return "";
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

                // System.out.println("faewrfwar: " + actorName);
                if (actorName.equals(actor)) {
                    // System.out.println("acctttor url: " + actorUrl);
                    return actorUrl;
                }
            }
        }

        return "";
    }

    // TODO check if actor urls are ever empty

    // TODO add movie year




    // TODO tested
    /*** 
    Given two actors, find all the actors they've both worked with before. 
    If there are no actors they've both worked with, return no actors. 
    ***/
    public void actorsBothActorsWorkedWith2(String actor1Name, String actor2Name, String movie1Id, String movie2Id) {
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







    // TODO all the actors?
    /*** 
    Given two actors, find all the actors they've both worked with before. 
    If there are no actors they've both worked with, return no actors. 
    ***/
    // public void actorsBothActorsWorkedWith(String actor1Name, String actor2Name, String movie1Id, String movie2Id) {
    //     String actor1Url = null;
    //     String actor2Url = null;

    //     actor1Url = getActorUrl(actor1Name, movie1Id);
    //     actor2Url = getActorUrl(actor2Name, movie2Id);

    //     System.out.println("dof");
    //     System.out.println(actor1Url);
    //     System.out.println(actor2Url);



    //     // TODO comment
    //     if (actor1Url.equals("") || actor2Url.equals("")) {
    //         System.out.println("URLs for given actor(s) were unable to be found.");
    //         return;
    //     }

    //     // Find actors that have worked with actor #1
    //     HashMap<String, String> moviesOfActor1Map = new HashMap<String, String>();
    //     TreeMap<String, Integer> colleaguesOfActor1Map = new TreeMap<String, Integer>();

    //     String url1 = this.baseUrl + actor1Url;

    //     Document actor1Doc = null;
    //     try {
    //         actor1Doc = Jsoup.connect(url1).get();
    //     } catch (IOException e) {
    //         System.out.println("The URL " + url1 + " could not be accessed.");
    //         return;
    //     }

    //     Elements divElements = actor1Doc.select("div");
    //     Element filmographySection = null;
        
    //     for (Element divElement : divElements) {
    //         if (divElement.attr("data-testid").contains("Filmography")) {
    //             filmographySection = divElement;
    //             break;
    //         }
    //     }

    //     if (filmographySection == null) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     Elements sectionElements = filmographySection.select("section");
    //     if (sectionElements.size() < 2) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     Element creditsSection = sectionElements.get(1);

    //     Elements divChildren = creditsSection.children();
    //     if (divChildren.size() < 5) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     Element divChild = divChildren.get(4);

    //     divChildren = divChild.children();

    //     if (divChildren.size() < 2) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(1);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 1) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(0);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 4) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(3);

    //     Element ulElement = divChild.selectFirst("ul");
    //     if (ulElement == null) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     Elements liElements = ulElement.select("li");
    //     for (Element liElement : liElements) {
    //         Elements aElements = liElement.select("a");
    //         if (aElements.size() >= 2) {
    //             // TODO: A link is found for this movie
    //             Element aElement = aElements.get(1);
    //             String href = aElement.attr("href");
    //             String movieTitle = aElement.text();
    //             moviesOfActor1Map.put(movieTitle, href);
    //         }
    //     }

    //     for (String movieTitle : moviesOfActor1Map.keySet()) {
    //         String href = this.baseUrl + moviesOfActor1Map.get(movieTitle);
    //         getMovieCastByUrl(href, colleaguesOfActor1Map);
    //     }

    //     // Find actors that have worked with actor #2
    //     HashMap<String, String> moviesOfActor2Map = new HashMap<String, String>();
    //     TreeMap<String, Integer> colleaguesOfActor2Map = new TreeMap<String, Integer>();

    //     String url2 = this.baseUrl + actor1Url;

    //     Document actor2Doc = null;
    //     try {
    //         actor2Doc = Jsoup.connect(url2).get();
    //     } catch (IOException e) {
    //         System.out.println("The URL " + url2 + " could not be accessed.");
    //         return;
    //     }

    //     divElements = actor2Doc.select("div");
    //     filmographySection = null;
        
    //     for (Element divElement : divElements) {
    //         if (divElement.attr("data-testid").contains("Filmography")) {
    //             filmographySection = divElement;
    //             break;
    //         }
    //     }

    //     if (filmographySection == null) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     sectionElements = filmographySection.select("section");
    //     if (sectionElements.size() < 2) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     creditsSection = sectionElements.get(1);

    //     divChildren = creditsSection.children();
    //     if (divChildren.size() < 5) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     divChild = divChildren.get(4);

    //     divChildren = divChild.children();

    //     if (divChildren.size() < 2) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(1);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 1) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(0);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 4) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(3);

    //     ulElement = divChild.selectFirst("ul");
    //     if (ulElement == null) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     liElements = ulElement.select("li");
    //     for (Element liElement : liElements) {
    //         Elements aElements = liElement.select("a");
    //         if (aElements.size() >= 2) {
    //             // TODO: A link is found for this movie
    //             Element aElement = aElements.get(1);
    //             String href = aElement.attr("href");
    //             String movieTitle = aElement.text();
    //             moviesOfActor2Map.put(movieTitle, href);
    //         }
    //     }

    //     for (String movieTitle : moviesOfActor2Map.keySet()) {
    //         String href = this.baseUrl + moviesOfActor2Map.get(movieTitle);
    //         getMovieCastByUrl(href, colleaguesOfActor2Map);
    //     }

    //     HashSet<String> colleaguesOfBoth = new HashSet<String>();

    //     for (String colleague : colleaguesOfActor1Map.keySet()) {
    //         if (colleaguesOfActor2Map.keySet().contains(colleague)) {
    //             colleaguesOfBoth.add(colleague);
    //         }
    //     }

    //     for (String colleague : colleaguesOfActor2Map.keySet()) {
    //         if (colleaguesOfActor1Map.keySet().contains(colleague)) {
    //             colleaguesOfBoth.add(colleague);
    //         }
    //     }

    //     // TODO comment
    //     colleaguesOfBoth.remove(actor1Name);
    //     colleaguesOfBoth.remove(actor2Name);

    //     if (colleaguesOfBoth.size() >= 1) {
    //         System.out.println(actor1Name + " and " + actor2Name + " have both acted with the following actor/actresses together:");
    //         for (String colleague : colleaguesOfBoth) {
    //             System.out.println(colleague);
    //         }
    //     } else {
    //         System.out.println(actor1Name + " and " + actor2Name + " have never acted with the same actors/actresses together.");
    //     }
    // }

    // /*** 
    // Given two actors, find movies they've worked in together before. 
    // If they haven't worked in movies before, return no movies. 
    // ***/
    // public void moviesActorsInTogether(String actor1Name, String actor2Name, String actor1Url, String actor2Url) {
    //     HashSet<String> moviesTogether = new HashSet<String>();

    //     // Find movies that actor #1 has acted in
    //     HashMap<String, String> actor1Movies = new HashMap<String, String>();

    //     // TODO
    //     Document actor1Doc = null;
    //     try {
    //         actor1Doc = Jsoup.connect(this.baseUrl + actor1Url).get();
    //     } catch (IOException e) {
    //         System.out.println("The URL " + this.baseUrl + actor1Url + " could not be accessed.");
    //         return;
    //     }

    //     Elements divElements = actor1Doc.select("div");
    //     Element filmographySection = null;
        
    //     // TODO
    //     for (Element divElement : divElements) {
    //         if (divElement.attr("data-testid").contains("Filmography")) {
    //             filmographySection = divElement;
    //             break;
    //         }
    //     }

    //     if (filmographySection == null) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     Elements sectionElements = filmographySection.select("section");
    //     if (sectionElements.size() < 2) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     Element creditsSection = sectionElements.get(1);

    //     Elements divChildren = creditsSection.children();
    //     if (divChildren.size() < 5) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     Element divChild = divChildren.get(4);

    //     divChildren = divChild.children();

    //     if (divChildren.size() < 2) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(1);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 1) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(0);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 4) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(3);

    //     Element ulElement = divChild.selectFirst("ul");
    //     if (ulElement == null) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     Elements liElements = ulElement.select("li");
    //     for (Element liElement : liElements) {
    //         Elements aElements = liElement.select("a");
    //         if (aElements.size() >= 2) {
    //             // TODO: A link is found for this movie
    //             Element aElement = aElements.get(1);
    //             String href = aElement.attr("href");
    //             String movieTitle = aElement.text();
    //             actor1Movies.put(movieTitle, href);
    //         }
    //     }

    //     for (String movieTitle : actor1Movies.keySet()) {
    //         String href = this.baseUrl + actor1Movies.get(movieTitle);

    //         // TODO new 
    //         if (isActorInMovie(href, actor2Name)) {
    //             moviesTogether.add(movieTitle);
    //         }
    //     }

    //     // TODO debugging purposes
    //     for (String movie: moviesTogether) {
    //         System.out.println("Movie together 1: " + movie);
    //     }

    //     // TODO
    //     // Find movies that actor #2 has acted in
    //     HashMap<String, String> actor2Movies = new HashMap<String, String>();

    //     Document actor2Doc = null;
    //     try {
    //         actor2Doc = Jsoup.connect(this.baseUrl + actor2Url).get();
    //     } catch (IOException e) {
    //         System.out.println("The URL " + this.baseUrl + actor2Url + " could not be accessed.");
    //         return;
    //     }

    //     divElements = actor2Doc.select("div");
    //     filmographySection = null;
        
    //     // TODO
    //     for (Element divElement : divElements) {
    //         if (divElement.attr("data-testid").contains("Filmography")) {
    //             filmographySection = divElement;
    //             break;
    //         }
    //     }

    //     if (filmographySection == null) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     sectionElements = filmographySection.select("section");
    //     if (sectionElements.size() < 2) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     creditsSection = sectionElements.get(1);

    //     divChildren = creditsSection.children();
    //     if (divChildren.size() < 5) {
    //         System.out.println("No filmography section found.");
    //         return;
    //     }

    //     divChild = divChildren.get(4);

    //     divChildren = divChild.children();

    //     if (divChildren.size() < 2) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(1);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 1) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(0);

    //     divChildren = divChild.children();
    //     if (divChildren.size() < 4) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     divChild = divChildren.get(3);

    //     ulElement = divChild.selectFirst("ul");
    //     if (ulElement == null) {
    //         System.out.println("No previous movies found.");
    //         return;
    //     }

    //     liElements = ulElement.select("li");
    //     for (Element liElement : liElements) {
    //         Elements aElements = liElement.select("a");
    //         if (aElements.size() >= 2) {
    //             // TODO: A link is found for this movie
    //             Element aElement = aElements.get(1);
    //             String href = aElement.attr("href");
    //             String movieTitle = aElement.text();
    //             actor1Movies.put(movieTitle, href);
    //         }
    //     }

    //     for (String movieTitle : actor2Movies.keySet()) {
    //         String href = this.baseUrl + actor2Movies.get(movieTitle);

    //         // TODO new 
    //         if (isActorInMovie(href, actor1Name)) {
    //             moviesTogether.add(movieTitle);
    //         }
    //     }

    //     if (moviesTogether.size() >= 1) {
    //         System.out.println(actor1Name + " and " + actor2Name + " have been in the following movies together:");
    //         for (String movie : moviesTogether) {
    //             System.out.println(movie);
    //         }
    //     } else {
    //         System.out.println(actor1Name + " and " + actor2Name + " have been in no movies together.");
    //     }
    // }









    // TODO fixed
    // TODO revised version
    public void getMostPopularColleagues2(String actorUrl, String actorName) {
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
                    // System.out.println("DOra: " + movieTitle);
                }
                
            
                // System.out.println("hellllooo");
                // for (Element movie : previousMovies) {
                //     String href = movie.attr("href");
                //     String movieTitle = movie.text();
                //     moviesOfColleaguesMap.put(movieTitle, href);
                //     System.out.println("DOra: " + movieTitle);
                // }
            }
        }

        for (String movieTitle : moviesOfColleaguesMap.keySet()) {
            // System.out.println("rw3ra3rawr: " + movieTitle);
            String href = this.baseUrl + moviesOfColleaguesMap.get(movieTitle);
            getMovieCastByUrl(href, colleaguesFreqMap);
        }

        // System.out.println("rw3raffffffffff3rawr: ");

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

    // TODO revised version
    /*** 
    Given two actors, find movies they've worked in before. 
    If they haven't worked in movies before, return no movies. 
    ***/
    public void moviesActorsInTogether2(String actor1Name, String actor2Name, String movie1Id, String movie2Id) {
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


    // TODO doesn't make sense
    // TODO revised methods using dora's
    // public void getMovieCast2(String movieId) {
    //     String url = this.movieUrl + movieId;
    //     Document movieDoc = null;

    //     try {
    //         movieDoc = Jsoup.connect(url).get();
    //     } catch (IOException e) {
    //         System.out.println("The URL " + url + " could not be accessed.");
    //         return;
    //     }

    //     // TODO change variable names
    //     Elements directedList = movieDoc.select("div");
    //     Document directedMovieDoc;
    //     for (int i = 1; i < directedList.size(); i++) {
    //         if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title " +
    //                 "ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-director")) {
    //             Elements previousMoviesList = directedList.get(i).select
    //                     ("div.ipc-accordion__item__content_inner.accordion-content");
    //             Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
    //                     .select("a");
    //             String title;
    //             for (Element movie : previousMovies) {
    //                 String urlExt = movie.attr("href");
    //                 String movieTitle = movie.text();
    //                 System.out.println(movieTitle);
    //             }
    //         }
    //     }


    // }





    // TODO Dora's method
        // gets the average ratings of all movies directors of a given movie and outputs their highest rated movie
        // public String getDirectorRecommendations(String movieID, String movieTitle) {
        //     HashMap<String, Integer> directorCounts = new HashMap<>();
        //     HashMap<String, Double> directorSums = new HashMap<>();
        //     String baseURL = this.baseUrl;
        //     String creditsURL = baseURL + "title/" + movieID + "/fullcredits";
        //     String result = "";
        //     try {
        //         // accesses the IMDb page for the full credits of the given movie
        //         Document moviePage = Jsoup.connect(creditsURL).get();
        //         Elements docDivs = moviePage.select("div#fullcredits_content");
        //         for (Element div : docDivs) {
        //             // finds the directors
        //             Elements directors = div.selectFirst("table").selectFirst("tbody").select("a");
        //             for (Element director: directors) {
        //                 String urlExt = director.attr("href");
        //                 String directorName = director.text();
        //                 try {
        //                     // accesses the page of each director
        //                     Document directorDoc = Jsoup.connect(baseURL + urlExt).get();
        //                     Elements directedList = directorDoc.select("div");
        //                     Document directedMovieDoc;
        //                     for (int i = 1; i < directedList.size(); i++) {
        //                         if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title " +
        //                                 "ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-director")) {
        //                             Elements previousMoviesList = directedList.get(i).select
        //                                     ("div.ipc-accordion__item__content_inner.accordion-content");
        //                             Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
        //                                     .select("a");
        //                             String title;
        //                             for (Element movie : previousMovies) {
        //                                 urlExt = movie.attr("href");
        //                                 try {
        //                                     directorSums.put(directorName, 0.0);
        //                                     directorCounts.put(directorName, 0);
        //                                     directedMovieDoc = Jsoup.connect(baseURL + urlExt +
        //                                             "/ratings?ref_=tt_ql_4").get();
        //                                     System.out.println(directedMovieDoc.text());
        //                                     double trueRating = Double.parseDouble(directedMovieDoc
        //                                             .selectFirst(".ipl-rating-star__rating").text());
        //                                     // adds the movies to a HashMap while counting the times a movie appears
        //                                     directorCounts.put(directorName, directorCounts.get(directorName) + 1);
        //                                     directorSums.put(directorName, directorSums.get(directorName) + trueRating);
        //                                 } catch (IOException e) {
        //                                     System.out.println("exception: URL - " + baseURL + urlExt);
        //                                 }
        //                             }
        //                         }
        //                     }
        //                 } catch (IOException e) {
        //                     System.out.println("exception: URL - " + baseURL + urlExt);
        //                 }
        //             }
        //         }
        //     } catch(IOException e){
        //         System.out.println("exception: URL - " + creditsURL);
        //     }
        //     System.out.println(directorCounts);
        //     for (String key : directorCounts.keySet()) {
        //         result += key + " (" + (directorSums.get(key) / directorCounts.get(key)) + "), ";
        //     }
        //     return result.substring(0, result.length() -2);
        // }
}