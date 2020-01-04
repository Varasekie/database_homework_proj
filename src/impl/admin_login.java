package impl;

import db_conn.database;
import entity.administrator;
import dao.login;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;

public class admin_login extends JFrame {
    private JLabel bookManage,readerManage,logManage;
    private JLabel exit,login,loginButton,close;
    private JTextField bigTextField;
    private JTextField admin_id;
    private JPasswordField admin_licensecode;
    private entity.administrator administrator;
    private int flag;
    public admin_login(){
        super("管理员登入");
        this.setSize(1920,1080);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("images/管理员图.jpg");//这是背景图片
        JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));//背景标签添加
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
        Container cp=this.getContentPane();
        cp.setLayout(null);
        ((JPanel)cp).setOpaque(false); //注意这里，将内容面板设为透明
        administrator = new administrator();



        admin_id = new JTextField();
        admin_id.setBounds(725,492,500,40);
        beautifyTextField(admin_id);
        admin_id.setVisible(false);
        cp.add(admin_id);

        admin_licensecode = new JPasswordField();
        admin_licensecode.setBounds(725,607,500,40);
        beautifyTextField(admin_licensecode);
        admin_licensecode.setVisible(false);
        cp.add(admin_licensecode);

        loginButton = new JLabel(new ImageIcon("managerImg/登录按钮.png"));
        loginButton.setBounds(850,700,230,40);
        loginButton.setVisible(false);
        cp.add(loginButton);

        close = new JLabel(new ImageIcon("managerImg/x.png"));
        close.setBounds(1200,330,20,20);
        close.setVisible(false);
        cp.add(close);


        /*
        * 登录窗口
        * */
        login = new JLabel(new ImageIcon("managerImg/登录.png"));
        login.setBounds(700,300,550,550);
        login.setVisible(false);
        cp.add(login);

        bigTextField = new JTextField();
        bigTextField.setBounds(0, 0, 1920, 1080);
        beautifyTextField(bigTextField);
        bigTextField.setEditable(false);
        bigTextField.setVisible(false);
        cp.add(bigTextField);
        /*
        * 上面是蒙版
        * */
        bookManage = new JLabel(new ImageIcon("images/图书管理.png"));
        bookManage.setBounds(500,150,350,350);
        cp.add(bookManage);

        readerManage = new JLabel(new ImageIcon("images/人员管理.png"));
        readerManage.setBounds(1080,150,350,350);
        cp.add(readerManage);

        logManage = new JLabel(new ImageIcon("images/日志管理.png"));
        logManage.setBounds(500,600,350,350);
        cp.add(logManage);

        exit = new JLabel(new ImageIcon("images/退出管理.png"));
        exit.setBounds(1080,600,350,350);
        cp.add(exit);

        /*
        * 设置书籍管理监听器监听器
        * */
        bookManage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bigTextField.setVisible(true);
                login.setVisible(true);
                admin_id.setVisible(true);
                loginButton.setVisible(true);
                close.setVisible(true);
                admin_licensecode.setVisible(true);
                flag = 1;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                bookManage.setIcon(new ImageIcon("images/图书管理1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bookManage.setIcon(new ImageIcon("images/图书管理.png"));
            }
        });
        /*
         * 设置人员管理监听器监听器
         * */
        readerManage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bigTextField.setVisible(true);
                login.setVisible(true);
                admin_id.setVisible(true);
                loginButton.setVisible(true);
                close.setVisible(true);
                admin_licensecode.setVisible(true);
                flag = 2;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                readerManage.setIcon(new ImageIcon("images/人员管理1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                readerManage.setIcon(new ImageIcon("images/人员管理.png"));
            }
        });
        /*
         * 设置日志管理监听器监听器
         * */
        logManage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bigTextField.setVisible(true);
                login.setVisible(true);
                admin_id.setVisible(true);
                loginButton.setVisible(true);
                close.setVisible(true);
                admin_licensecode.setVisible(true);
                flag = 3;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logManage.setIcon(new ImageIcon("images/日志管理1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logManage.setIcon(new ImageIcon("images/日志管理.png"));
            }
        });

        loginButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*
                 * 这边开始连接数据库
                 * */
                database dbcon;
                dbcon = new database();
                String SQL = "select * from administrator where admin_id = '"+admin_id.getText() +"';";
                try {
                    ResultSet rs = dbcon.executeQuery(SQL);

                    while(rs.next()){
                        administrator.setAdmin_id(rs.getString("admin_id"));
                        administrator.setadmin_licensecode(rs.getString("admin_licensecode"));
                    }
                    rs.close();
                    dbcon.closeConn();

                } catch (Exception se) {
                    se.printStackTrace();
                }
                /*
                 * 连接数据库结束，检查密码是No正确
                 * */
                String password1 = String.valueOf(admin_licensecode.getPassword());
                if(admin_id.getText().equals(administrator.getAdmin_id())&&password1.equals(administrator.getadmin_licensecode())){
                    if(flag==1){
                        new book_manager();
                        dispose();
                    }
                    else if(flag==2){
                        new reader_manager();
                        dispose();

                    }
                    else{
                        new log_manager();
                        dispose();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"     非法登入！");
                    admin_licensecode.setText("");
                    admin_id.setText("");
                    admin_id.requestFocus(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setIcon(new ImageIcon("managerImg/登录按钮1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setIcon(new ImageIcon("managerImg/登录按钮.png"));
            }
        });

        close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                bigTextField.setVisible(false);
                login.setVisible(false);
                admin_id.setText("");
                admin_id.setVisible(false);
                loginButton.setVisible(false);
                close.setVisible(false);
                admin_licensecode.setText("");
                admin_licensecode.setVisible(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setIcon(new ImageIcon("managerImg/x1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setIcon(new ImageIcon("managerImg/x.png"));
            }
        });

        /*
        * 退出管理监听器
        * */
        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new login();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exit.setIcon(new ImageIcon("images/退出管理1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit.setIcon(new ImageIcon("images/退出管理.png"));
            }
        });
        /*
        * 设置可见
        * */
        setVisible(true);
    }

    /*
     * 文本框美化方法复用
     * */
    private void beautifyTextField(JTextField jTextfield){
        jTextfield.setOpaque(false);
        jTextfield.setHorizontalAlignment(JTextField.CENTER);
        jTextfield.setFont(new Font("Arial", Font.PLAIN, 22));
        jTextfield.setBorder(new LineBorder(null,0));
    }

    public static void main(String[] args) {
        new admin_login();
    }
}
