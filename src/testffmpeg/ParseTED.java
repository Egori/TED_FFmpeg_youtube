/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testffmpeg;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pageDAO.Page;
import pageDAO.PageDAO;
import sun.util.locale.LanguageTag;

/**
 *
 * @author mumu
 */
public class ParseTED {

    private static String language = "en";
    private static String URL = "http://www.ted.com/talks";
    private static ArrayList pageLinks = new ArrayList();
    static Connection con;
    static boolean nextFlag;

    static pageDAO.PageDAO dao = new PageDAO(getConnection());
    private static String lastPage = dao.getLastPage();

    private static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ted", "root", "root");
            } catch (SQLException ex) {
                Logger.getLogger(ParseTED.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         return con;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static void Run() {

        Integer i = 0;
        getPageLinks(URL);
        for(Object page : pageLinks){
        
            System.out.println(i++);
            parse(page.toString());
            
            
        }
        

    }
        
    private static void getPageLinks(String URL) {

        String linkVid = "";
        String nextPage = "";
        String linkPage;
        
        Document doc = null;
        try {
            doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.101 Chrome/45.0.2454.101 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException ex) {
            Logger.getLogger(ParseTED.class.getName()).log(Level.SEVERE, null, ex);
        }
        Elements linksPage = doc.getElementsByClass("media__message").select("a");

        for (Element pageEl : linksPage) {
            linkVid = pageEl.attr("href");
            linkPage = "http://www.ted.com" + linkVid;
            if (linkPage.equals(lastPage) || lastPage == null) {
                nextFlag = true;
            }
            if (nextFlag) {
                pageLinks.add(linkPage);
            }
        }

        //переход на следующую страницу
        nextPage = doc.getElementsByClass("pagination__next").attr("href");
        String linkNextPage = "http://www.ted.com" + nextPage;

        if (!nextPage.equals("")) {
            System.out.println("получаем ссылки страницы: " + linkNextPage);
            getPageLinks(linkNextPage);
            
        }
        
        System.out.println("получено ссылок на видео: " + pageLinks.size());
        
    }
    
    private static boolean isParsed(String URL){
        return true;
    }
  
    private static void parse(String pageURL) {

       String langURL =  pageURL + "?language=" + language;
       String subtLangURL = pageURL + "/transcript?language=" + language;
        
        String html;
        String title;
        String description;
        String authorLink = "";
        String author = "";
        String tags = "";
        String mediaId = "";
        String index = "";
        String filmed = "";
        String transcript = "";
        String transcriptText = "";        

        Document doc = null;
        try {
            doc = Jsoup.connect(langURL)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.101 Chrome/45.0.2454.101 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException ex) {
            Logger.getLogger(ParseTED.class.getName()).log(Level.SEVERE, null, ex);
           try {
               Thread.sleep(3000);
           } catch (InterruptedException ex1) {
               Logger.getLogger(ParseTED.class.getName()).log(Level.SEVERE, null, ex1);
           }
            parse(pageURL);
        }
         
        // Получение значений сираницы
        html = doc.html();
        title = doc.getElementsByClass("player-hero__title__content").text();
        //String author = doc.getElementsByClass("player-hero__speaker__content").text().replaceAll(":$", "");
        description = doc.getElementsByClass("talk-description").text();
        Elements authorLinksEL = doc.getElementsByClass("talk-speaker__name").select("a");
        for (Element currAuthorLinkEl : authorLinksEL) {
            authorLink += "http://www.ted.com" + currAuthorLinkEl.attr("href") + ", ";
            author += currAuthorLinkEl.text() + ", ";
        }
        authorLink = authorLink.replaceAll(", $", "");
        author = author.replaceAll(", $", "");      
        Elements tagsEl = doc.getElementsByClass("talk-topics__list").select("a");
        for(Element currTagEl : tagsEl){
            tags += currTagEl.text() + ",";
        }        
        tags = tags.replaceAll(",$", "");
        
        
        Pattern patternMediaId = Pattern.compile("\"file\":\"https://download.ted.com/talks/(.*?)-");
        Matcher matcherMediaId = patternMediaId.matcher(html);
        if(matcherMediaId.find()){
            mediaId = matcherMediaId.group();
            mediaId = mediaId.replaceAll("(-$)", "");
            mediaId = mediaId.replaceAll("\"file\":\"https://download.ted.com/talks/", "");
        }
        
Pattern patternIndex = Pattern.compile("\"id\":(\\d*)");
        Matcher matcherIndex = patternIndex.matcher(html);
        if(matcherIndex.find()){
            index = matcherIndex.group();
            index = index.replaceAll("\"id\":", "");
        }        
        
        Pattern patternFilmed = Pattern.compile("\"filmed\":(\\d*)");
        Matcher matcherFilmed = patternFilmed.matcher(html);
        if(matcherFilmed.find()){
            filmed = matcherFilmed.group();
            filmed = filmed.replaceAll("\"filmed\":", "");
        }      
        
        Document docTranscript = null;
        try {
            docTranscript = Jsoup.connect(subtLangURL)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.101 Chrome/45.0.2454.101 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException ex) {
            Logger.getLogger(ParseTED.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(docTranscript != null)
        transcript = docTranscript.getElementsByClass("col-lg-7").html();
        transcriptText = docTranscript.getElementsByClass("col-lg-7").text();
        
        Page page = new Page();
        page.setAuthor(author);
        page.setAuthorLink(authorLink);
        page.setDescription(description);
        page.setFilmed(filmed);
        page.setIndex(index);
        page.setLinkTed(pageURL);
        page.setMediaId(mediaId);
        page.setTags(tags);
        page.setTitle(title);
        page.setTranscript(transcript);
        page.setTranscriptText(transcriptText);
               
        dao.updatePage(page);
        
        System.out.println("next page " + pageURL);

    }

}
