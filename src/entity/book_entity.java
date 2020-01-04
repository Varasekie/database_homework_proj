package entity;

public class book_entity {

    private String book_num;
    private String book_name;
    private String is_borrowed;
    private String position;
    private String writer;
    private String book_img;
    private String publish;
    private String publish_date;
    private String book_category;
    private String update_time;

    public String getbook_num() {
        return book_num;
    }

    public void setbook_num(String book_num) {
        this.book_num = book_num;
    }

    public String getbook_name() {
        return book_name;
    }

    public void setbook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getis_borrowed() {
        return is_borrowed;
    }

    public void setis_borrowed(String is_borrowed) {
        this.is_borrowed = is_borrowed;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getwriter() {
        return writer;
    }

    public void setwriter(String writer) {
        this.writer = writer;
    }

    public String getbook_img() {
        return book_img;
    }

    public void setbook_img(String book_img) {
        this.book_img = book_img;
    }

    public String getpublish() {
        return publish;
    }

    public void setpublish(String publish) {
        this.publish = publish;
    }

    public String getpublish_date() {
        return publish_date;
    }

    public void setpublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getbook_category() {
        return book_category;
    }

    public void setbook_category(String book_category) {
        this.book_category = book_category;
    }

    public String getupdate_time() {
        return update_time;
    }

    public void setupdate_time(String update_time) {
        this.update_time = update_time;
    }
}
