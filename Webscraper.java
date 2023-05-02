import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Webscraper {

    private HashMap<String, String> castMap;
    private String baseUrl;
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

    // gets up to the first 20 keywords of a given movie and outputs movies with more than 2 shared keywords
    public String getKeywordRecommendations(String movieID, String movieTitle) {
        HashMap<String, Integer> movieCounts = new HashMap<>();
        // URL for the IMDb page of the keywords for the given movie
        String keywordURL = baseUrl + "/title/" + movieID + "/keywords";
        String result = "";
        try {
            // accesses the IMDb page of the keywords for the given movie
            Document listKeywordsDoc = Jsoup.connect(keywordURL).get();
            Elements docDivs = listKeywordsDoc.select("div#keywords_content");
            for (Element div : docDivs) {
                // finds the table of keywords
                Elements keywords = div.selectFirst("table").select("td");
                Document keywordsDoc;
                int count = 0;
                for (Element keyword: keywords) {
                    // gets at most the first 20 keywords of the keywords list (to minimize run time)
                    if (count < 20) {
                        String urlExt = keyword.selectFirst("a").attr("href");
                        try {
                            urlExt = urlExt.split("&")[0];
                            // ensures that the top recommendations of a specific keyword are feature films
                            urlExt += "&ref_=kw_ref_typ&sort=moviemeter,asc&mode=detail&page=1&title_type=movie";
                            // accesses the page of top recommendations for a specific keyword
                            keywordsDoc = Jsoup.connect(baseUrl + urlExt).get();
                            Elements keywordDocDivs = keywordsDoc.select("div.lister-item.mode-detail");
                            for (Element keywordDocDiv : keywordDocDivs) {
                                String title = keywordDocDiv.selectFirst("h3").select("a").text();
                                // adds the movies to a HashMap while counting the times a movie appears
                                if (movieCounts.containsKey(title)) {
                                    movieCounts.put(title, movieCounts.get(title) + 1);
                                } else {
                                    movieCounts.put(title, 1);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("exception: URL - " + baseUrl + urlExt);
                        }
                    }
                    count++;
                }
            }
        } catch(IOException e) {
            System.out.println("exception: URL - " + keywordURL);
        }
        // turns the HashMap into a TreeSet in order to get ordered counts
        TreeSet<String> countsToMovies = new TreeSet<>();
        for (String key : movieCounts.keySet()) {
            if (!key.equals(movieTitle)) {
                countsToMovies.add(movieCounts.get(key) + ": " + key);
            }
        }
        // gets top 20 movie recommendations
        int count = 0;
        for (String countKey: countsToMovies.descendingSet()) {
            if (count < 20) {
                result += "["+ countKey + "], ";
                count++;
            }
        }
        return result.substring(0, result.length() -2);
    }

    // gets the genres of a given movie and outputs movies with at least 2 shared genres of the top movies per genre
    public String getGenreRecommendations(String movieID, String movieTitle) {
        HashMap<String, Integer> movieCounts = new HashMap<>();
        String movieURL = baseUrl + "/title/" + movieID;
        String result = "";
        try {
            // accesses the IMDb page for the given movie
            Document moviePage = Jsoup.connect(movieURL).get();
            Elements docDivs = moviePage.select("div.ipc-chip-list__scroller");
            for (Element div : docDivs) {
                // finds the movie genres
                Elements genres = div.select("a");
                Document genresDoc;
                for (Element genre: genres) {
                    String urlExt = genre.attr("href");
                    try {
                        urlExt = urlExt.split("&")[0];
                        // ensures that all genre recommendations are movies
                        urlExt += "&title_type=movie&ref_=adv_explore_rhs";
                        // accesses the page of top movies for each genre
                        genresDoc = Jsoup.connect(baseUrl + urlExt).get();
                        Elements genreDocDivs = genresDoc.select("div.lister-item.mode-advanced");
                        for (Element genreDocDiv : genreDocDivs) {
                            String title = genreDocDiv.selectFirst("h3").select("a").text();
                            // adds the movies to a HashMap while counting the times a movie appears
                            if (movieCounts.containsKey(title)) {
                                movieCounts.put(title, movieCounts.get(title) + 1);
                            } else {
                                movieCounts.put(title, 1);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("exception: URL - " + baseUrl + urlExt);
                    }
                }
            }
        } catch(IOException e) {
            System.out.println("exception: URL - " + movieURL);
        }
        // turns the HashMap into a TreeSet in order to get ordered counts
        TreeSet<String> countsToMovies = new TreeSet<>();
        for (String key : movieCounts.keySet()) {
            if (!key.equals(movieTitle)) {
                countsToMovies.add(movieCounts.get(key) + ": " + key);
            }
        }
        // gets top 20 movie recommendations
        int count = 0;
        for (String countKey: countsToMovies.descendingSet()) {
            if (count < 20) {
                result += "["+ countKey + "], ";
                count++;
            }
        }
        return result.substring(0, result.length() -2);
    }

    // gets the average ratings of all movies directors of a given movie and outputs their highest rated movie
    public String getDirectorRecommendations(String movieID, String movieTitle) {
        HashMap<String, Integer> directorCounts = new HashMap<>();
        HashMap<String, Double> directorSums = new HashMap<>();
        HashMap<String, String> bestMovies = new HashMap<>();
        String creditsURL = baseUrl + "/title/" + movieID + "/fullcredits";
        String result = "";
        try {
            // accesses the IMDb page for the full credits of the given movie
            Document moviePage = Jsoup.connect(creditsURL).get();
            Elements docDivs = moviePage.select("div#fullcredits_content");
            for (Element div : docDivs) {
                // finds the directors
                Elements directors = div.selectFirst("table").selectFirst("tbody").select("a");
                for (Element director: directors) {
                    String urlExt = director.attr("href");
                    String directorName = director.text();
                    directorSums.put(directorName, 0.0);
                    directorCounts.put(directorName, 0);
                    String bestMovie = "";
                    double highestRating = 0.0;
                    try {
                        // accesses the page of each director
                        Document directorDoc = Jsoup.connect(baseUrl + urlExt).get();
                        Elements directedList = directorDoc.select("div");
                        Document dirMovieDoc;
                        for (int i = 1; i < directedList.size(); i++) {
                            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title " +
                                    "ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-director")) {
                                Elements previousMoviesList = directedList.get(i).select
                                        ("div.ipc-accordion__item__content_inner.accordion-content");
                                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                                        .select("div.ipc-metadata-list-summary-item__tc");
                                for (Element movie : previousMovies) {
                                    String movieName = movie.select("a").text();
                                    String dType = movie.text().split(movieName)[1];
                                    boolean isMovie = true;
                                    if (dType.contains("Short") || dType.contains("Video") || dType.contains("TV")) {
                                        isMovie = false;
                                    }
                                    if (isMovie) {
                                        urlExt = movie.select("a").attr("href");
                                        urlExt = "/title/" + urlExt.split("/")[2] + "/ratings";
                                        try {
                                            dirMovieDoc = Jsoup.connect(baseUrl + urlExt).get();
                                            // ensures that the User Ratings page is accessed rather than Ratings
                                            while (!dirMovieDoc.text().split("IMDb")[0].contains("User ratings")) {
                                                dirMovieDoc = Jsoup.connect(baseUrl + urlExt).get();
                                            }
                                            double trueRating = Double.parseDouble(dirMovieDoc
                                                    .selectFirst(".ipl-rating-star__rating").text());
                                            // adds the movie rating to HashMaps of counts and sums
                                            directorCounts.put(directorName, directorCounts.get(directorName) + 1);
                                            directorSums.put(directorName, directorSums.get(directorName) + trueRating);
                                            if (trueRating > highestRating) {
                                                highestRating = trueRating;
                                                bestMovie = movieName;
                                            }
                                        } catch (IOException e) {
                                            System.out.println("exception: URL - " + baseUrl + urlExt);
                                        }
                                    }
                                }
                            }
                            bestMovies.put(directorName, bestMovie + "(" + highestRating + ")");
                        }
                    } catch (IOException e) {
                        System.out.println("exception: URL - " + baseUrl + urlExt);
                    }
                }
            }
        } catch(IOException e){
            System.out.println("exception: URL - " + creditsURL);
        }
        for (String key : directorCounts.keySet()) {
            result += key + " (" + (directorSums.get(key) / directorCounts.get(key)) + ") [" + bestMovies.get(key) + "], ";
        }
        return result.substring(0, result.length() -2);
    }

    // gets the rating of a movie for a given age and gender and comparison to the average movie rating
    public String getDemographicRating(String movieID, String gender, int age) {
        String ratingsURL = baseUrl + "/title/" + movieID + "/ratings";
        try {
            // accesses the IMDb page of the ratings for a given movie
            Document ratingsPage = Jsoup.connect(ratingsURL).get();
            while (!ratingsPage.text().split("IMDb")[0].contains("User ratings")) {
                ratingsPage = Jsoup.connect(ratingsURL).get();
            }
            double trueRating = Double.parseDouble(ratingsPage.selectFirst(".ipl-rating-star__rating").text());
            Elements ratingRows = ratingsPage.select("table").get(1).select("tr");
            // sets the row variable to match the given gender
            int genderRow;
            if (gender.equals("Male")) {
                genderRow = 2;
            } else if (gender.equals("Female")) {
                genderRow = 3;
            } else {
                // if the gender is not Male or Female it returns the average of both genders
                genderRow = 1;
            }
            // gets the cells in the row of the right gender
            Elements ratingCols = ratingRows.get(genderRow).select("td");
            int ageColumn;
            // sets the column variable to match the given age
            if (age < 18) {
                ageColumn = 2;
            } else if (age < 30) {
                ageColumn = 3;
            } else if (age < 45) {
                ageColumn = 4;
            } else {
                ageColumn = 5;
            }
            // get the correct cell using age ranges and the given age
            Element ratingCell = ratingCols.get(ageColumn).selectFirst("div");
            // get the demographic rating as a double
            double demographicRating = Double.parseDouble(ratingCell.text());
            // returns the demographic rating and IMDb rating
            return "Demographic Rating for a(n) " + age + " year-old " + gender + ": " + demographicRating
                    + " (IMDb rating: " + trueRating + ")";
        } catch(IOException e) {
            System.out.println("exception: URL - " + ratingsURL);
        }
        return "";
    }

}
