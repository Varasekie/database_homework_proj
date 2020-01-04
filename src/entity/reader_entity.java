package entity;

public class reader_entity {
    private String reader_num;//用户名
    private String reader_name;//昵称
    private String reader_pwd;//密码
    private String gender;//性别
    private String reader_category;//定义读者种类，权限
    private String registe_time;//注册的时间
    private String email;//邮箱
    private String phone;//电话
    private String Note;//备注

    public String getreader_num() {
        return reader_num;
    }

    public void setreader_num(String reader_num) {
        this.reader_num = reader_num;
    }

    public String getreader_name() {
        return reader_name;
    }

    public void setreader_name(String reader_name) {
        this.reader_name = reader_name;
    }

    public String getreader_pwd() {
        return reader_pwd;
    }

    public void setreader_pwd(String reader_pwd) {
        this.reader_pwd = reader_pwd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getreader_category() {
        return reader_category;
    }

    public void setreader_category(String reader_category) {
        this.reader_category = reader_category;
    }

    public String getregiste_time() {
        return registe_time;
    }

    public void setregiste_time(String registe_time) {
        this.registe_time = registe_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }
}
