package entity;

public class borrow_entity {
    private String reader_num;
    private String book_num;
    private String borrow_data;
    private String return_data;

    public String getreader_num() {
        return reader_num;
    }

    public void setreader_num(String reader_num) {
        this.reader_num = reader_num;
    }

    public String getbook_num() {
        return book_num;
    }

    public void setbook_num(String book_num) {
        this.book_num = book_num;
    }

    public String getborrow_data() {
        return borrow_data;
    }

    public void setborrow_data(String borrow_data) {
        borrow_data = borrow_data;
    }

    public String getreturn_data() {
        return return_data;
    }

    public void setreturn_data(String return_data) {
        return_data = return_data;
    }
}
