package dao;
// 990204
// 505358678@qq.com
// 13666895038
import db_conn.database;
import entity.book_entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class book_shelf extends JFrame {
    private JLabel [] books = new JLabel[6];
    private JLabel back;
    book_shelf(){
        super("图书管理系统");
        this.setSize(1920,1080);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("book/书架背景.png");//这是背景图片
        JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));//背景标签添加
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
        Container cp=this.getContentPane();
        cp.setLayout(null);
        ((JPanel)cp).setOpaque(false); //注意这里，将内容面板设为透明


        for (int i = 0; i <3; i++) {
            books[i] = new JLabel();
            books[i].setBounds(450+i*400,170,400,400);
            cp.add(books[i]);
        }

        for (int i = 3; i <6; i++) {
            books[i] = new JLabel();
            books[i].setBounds(450+(i-3)*400,641,400,400);
            cp.add(books[i]);
        }
        back = new JLabel(new ImageIcon("managerImg/返回_白色.png"));
        back.setBounds(100,100,100,100);
        cp.add(back);

        /*
        * 返回按钮监听器
        * */
        back.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                back.setIcon(new ImageIcon("managerImg/返回_黑色.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                back.setIcon(new ImageIcon("managerImg/返回_白色.png"));
            }
        });

        setbook_imgs();
        setVisible(true);
    }

    private void setbook_imgs(){
        ArrayList<book_entity> borrowedBooks = new ArrayList<>();
        if(login.formalReader!=null){
            database dbcon;
            dbcon = new database();
            String SQL = "select book_name,book.book_num,book_img " +
                    "from borrow,book " +
                    "where borrow.book_num=book.book_num and reader_num = '" +
                    login.formalReader.getreader_num()+"' and return_data  is null;";
            try {
                ResultSet rs = dbcon.executeQuery(SQL);
                while(rs.next()){
                    book_entity book = new book_entity();
                    book.setbook_name(rs.getString("book_name"));
                    book.setbook_num(rs.getString("book_num"));
                    book.setbook_img(rs.getString("book_img"));
                    borrowedBooks.add(book);
                }
                rs.close();
                /*
                 * 这边用来写存放书本
                 * */

                for (int i = 0; i < borrowedBooks.size(); i++) {
                    books[i].setIcon(new ImageIcon(borrowedBooks.get(i).getbook_img()));
                }
                dbcon.closeConn();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new book_shelf();
    }
}
