package models;

/**
 * Created by Emin on 12/11/2015.
 */
public class Blog {
    private String title;
    private String author;

    public Blog(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}
