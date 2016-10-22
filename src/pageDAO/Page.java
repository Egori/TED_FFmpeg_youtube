package pageDAO;

public class Page {

    private Integer id = 0;
    private String linkTedPage = "";
    private String title = "";
    private String author = "";
    private String authorLink = "";
    private String description = "";
    private String tags = "";
    private String index = "";
    private String filmed = "";
    private String mediaId = "";
    private String transcript = "";

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLinkTed(String linkTedPage) {
        this.linkTedPage = linkTedPage;
    }

    public String getLinkTed() {
        return linkTedPage;
    }

 
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }

    public String getAuthorLink() {
        return authorLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    public void setFilmed(String filmed) {
        this.filmed = filmed;
    }

    public String getFilmed() {
        return filmed;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getTranscript() {
        return transcript;
    }

}
