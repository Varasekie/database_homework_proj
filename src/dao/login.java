package dao;

import db_conn.database;
import impl.admin_login;
import entity.reader_entity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.ResultSet;


public class login extends JFrame {
    private JLabel messageDialog;
    private JTextField bigTextField, userName;
    private JTextField email, phone;
    private JLabel adminImage;
    private JLabel confirmButton;
    private JLabel login, searchOnly, forget, regist;
    private JLabel getBack, getBackConfirm, getBackClose;
    private JPasswordField password;
    /**
     * 读者创建
     */
    static reader_entity formalReader = new reader_entity();
    private static reader_entity temporaryReader = new reader_entity();

    public login() {
        super("图书管理系统");
        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("images/样图.png");//这是背景图片
        JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));//背景标签添加
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
        Container cp = this.getContentPane();
        cp.setLayout(null);
        ((JPanel) cp).setOpaque(false);

        formalReader = new reader_entity();

        confirmButton = new JLabel(new ImageIcon("images/确定.png"));
        confirmButton.setBounds(937, 583, 50, 50);
        confirmButton.setVisible(false);
        cp.add(confirmButton);

        email = new JTextField();
        email.setBounds(900, 400, 300, 40);
        beautifyTextField(email);
        email.setOpaque(false);
        cp.add(email);

        phone = new JTextField();
        phone.setBounds(900, 520, 300, 40);
        beautifyTextField(phone);
        cp.add(phone);

        getBackConfirm = new JLabel(new ImageIcon("images/一键找回.png"));
        getBackConfirm.setBounds(939, 642, 136, 30);
        getBackConfirm.setVisible(false);
        cp.add(getBackConfirm);

        getBackClose = new JLabel(new ImageIcon("images/X.png"));
        getBackClose.setBounds(1323, 254, 40, 40);
        getBackClose.setVisible(false);
        cp.add(getBackClose);

        messageDialog = new JLabel(new ImageIcon("images/提示框深.png"));
        messageDialog.setBounds(710, 440, 500, 200);
        messageDialog.setVisible(false);
        cp.add(messageDialog);

        getBack = new JLabel(new ImageIcon("images/找回账号和密码.png"));
        getBack.setBounds(635, 230, 750, 620);
        getBack.setVisible(false);
        cp.add(getBack);


        /**
         * 蒙版
         */
        bigTextField = new JTextField();
        bigTextField.setBounds(0, 0, 1920, 1080);
        beautifyTextField(bigTextField);
        bigTextField.setEditable(false);
        bigTextField.setVisible(false);
        cp.add(bigTextField);


        userName = new JTextField();
        password = new JPasswordField();
        userName.setBounds(1360, 385, 240, 50);
        password.setBounds(1360, 522, 240, 50);
        userName.setFont(new Font("Arial", Font.PLAIN, 26));
        password.setFont(new Font("Arial", Font.PLAIN, 26));
        userName.setBorder(new LineBorder(null, 0));
        password.setBorder(new LineBorder(null, 0));
        userName.setOpaque(false);
        password.setOpaque(false);

        login = new JLabel(new ImageIcon("images/登录.png"));
        login.setBounds(1210, 690, 395, 50);

        searchOnly = new JLabel(new ImageIcon("images/我只想查找.png"));
        searchOnly.setBounds(1655, 705, 90, 20);

        forget = new JLabel(new ImageIcon("images/忘记密码.png"));
        forget.setBounds(1178, 810, 200, 50);

        regist = new JLabel(new ImageIcon("images/注册.png"));
        regist.setBounds(1475, 810, 200, 50);

        adminImage = new JLabel(new ImageIcon("images/账号管理.png"));
        adminImage.setBounds(1670, 35, 32, 32);

        JLabel adminLogin = new JLabel(new ImageIcon("images/管理员登入.png"));
        adminLogin.setBounds(1700, 49, 100, 20);

        cp.add(userName);
        cp.add(password);
        cp.add(login);
        cp.add(forget);
        cp.add(regist);
        cp.add(searchOnly);
        cp.add(adminImage);
        cp.add(adminLogin);

        this.setVisible(true);

        confirmButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                messageDialog.setVisible(false);
                confirmButton.setVisible(false);
                bigTextField.setVisible(false);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        login.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*
                 * 这边开始连接数据库
                 * */
                formalReader = new reader_entity();
                database dbcon;
                dbcon = new database();
                database dbcon_;
                dbcon_ = new database();
                String SQL = "select * from reader where reader_num = '" + userName.getText() + "';";
                String SQL_ = "select reader_reader_category.reader_num,reader_category.category_name from reader_reader_category left join reader_category on reader_reader_category.category_num = reader_category.category_num where reader_num = '" + userName.getText() + "';";
                try {
                    ResultSet rs = dbcon.executeQuery(SQL);
                    ResultSet rs_ = dbcon_.executeQuery(SQL_);
                    while (rs.next()) {
                        rs_.next();
                        formalReader.setreader_num(rs.getString("reader_num"));
                        formalReader.setreader_name(rs.getString("reader_name"));
                        formalReader.setreader_pwd(rs.getString("reader_pwd"));
                        formalReader.setreader_category(rs_.getString("category_name"));
                    }
                    rs.close();
                    dbcon.closeConn();
                    rs_.close();
                    dbcon_.closeConn();
                } catch (Exception se) {
                    se.printStackTrace();
                }
                /*
                 * 检查密码
                 * */
                String password1 = String.valueOf(password.getPassword());
                if (userName.getText().equals(formalReader.getreader_num()) && password1.equals(formalReader.getreader_pwd())) {
                    reader_list readerlist = new reader_list();
                    readerlist.setVisible(true);
                    /*
                     * ★★★将登录页销毁，No则将会出现用户借还信息的异常★★★
                     * */
                    dispose();
                } else {
                    messageDialog.setVisible(true);
                    confirmButton.setVisible(true);
                    bigTextField.setVisible(true);
                    bigTextField.requestFocus(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                login.setIcon(new ImageIcon("images/登录.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                login.setIcon(new ImageIcon("images/登录1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                login.setIcon(new ImageIcon("images/登录.png"));
            }
        });

        /*
         * 管理员登录监听器
         * */
        adminImage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new admin_login();
                dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                adminImage.setIcon(new ImageIcon("images/账号管理2.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                adminImage.setIcon(new ImageIcon("images/账号管理1.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                adminImage.setIcon(new ImageIcon("images/账号管理1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                adminImage.setIcon(new ImageIcon("images/账号管理.png"));
            }
        });

        /*当用户只想查找的时候*/
        searchOnly.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                reader_list readerlist = new reader_list();
                readerlist.setVisible(true);
                dispose();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                searchOnly.setIcon(new ImageIcon("images/我只想查找1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchOnly.setIcon(new ImageIcon("images/我只想查找.png"));
            }
        });
        forget.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                forgetJFrameSwitch();
                email.requestFocus(true);

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forget.setIcon(new ImageIcon("images/忘记密码1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forget.setIcon(new ImageIcon("images/忘记密码.png"));
            }
        });
        regist.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                register regist1 = null;
                try {
                    regist1 = new register();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                regist1.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                regist.setIcon(new ImageIcon("images/注册1.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                regist.setIcon(new ImageIcon("images/注册1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                regist.setIcon(new ImageIcon("images/注册.png"));
            }
        });

        password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                formalReader = new reader_entity();
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    /*
                     * 这边开始连接数据库
                     * */
                    database dbcon;
                    dbcon = new database();
                    String SQL = "select * from reader where reader_num = '" + userName.getText() + "';";
                    try {
                        ResultSet rs = dbcon.executeQuery(SQL);
                        while (rs.next()) {
                            formalReader.setreader_num(rs.getString("reader_num"));
                            formalReader.setreader_name(rs.getString("reader_name"));
                            formalReader.setreader_pwd(rs.getString("reader_pwd"));
                            formalReader.setreader_category(rs.getString("reader_category"));
                        }
                        rs.close();
                        dbcon.closeConn();

                    } catch (Exception se) {
                        se.printStackTrace();
                    }
                    /*
                     * 连接数据库结束，检查密码是No正确
                     * */
                    String password1 = String.valueOf(password.getPassword());
                    if (userName.getText().equals(formalReader.getreader_num()) && password1.equals(formalReader.getreader_pwd())) {
                        reader_list readerlist = new reader_list();
                        readerlist.setVisible(true);
                        dispose();
                    } else {
                        messageDialog.setVisible(true);
                        confirmButton.setVisible(true);
                        bigTextField.setVisible(true);
                        bigTextField.requestFocus(true);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        /*
         * 大文本框的点击事件，一般要是点到了它，应该是出现了密码错误的情况
         * 所以要让用户点了确定才能消失
         * */
        bigTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                messageDialog.setIcon(new ImageIcon("images/提示框淡.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                messageDialog.setIcon(new ImageIcon("images/提示框深.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        messageDialog.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                messageDialog.setIcon(new ImageIcon("images/提示框深.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        /*
         * 一键找回按钮听器
         * */
        getBackConfirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                database database = new database();
                String SQL = "select * from reader where Email = '" + email.getText() + "' and Phone = '" + phone.getText() + "';";
                try {
                    ResultSet rs = database.executeQuery(SQL);
                    while (rs.next()) {
                        temporaryReader.setreader_num(rs.getString("reader_num"));
                        temporaryReader.setreader_name(rs.getString("reader_name"));
                        temporaryReader.setreader_pwd(rs.getString("reader_pwd"));
                        temporaryReader.setEmail(rs.getString("Email"));
                        temporaryReader.setPhone(rs.getString("Phone"));
                    }
                    rs.close();
                    database.closeConn();
                } catch (Exception se) {
                    se.printStackTrace();
                }
                if (temporaryReader.getreader_num() == null || temporaryReader.getreader_pwd() == null) {
                    JOptionPane.showMessageDialog(null, "账号和密码未找到\n请核实信息", "您好！", JOptionPane.WARNING_MESSAGE);
                } else {
                    String msg = "账号为：" + temporaryReader.getreader_num() + "\n密码为：" + temporaryReader.getreader_pwd();
                    JOptionPane.showMessageDialog(null, msg, temporaryReader.getreader_name() + " 您好！", JOptionPane.WARNING_MESSAGE);
                    forgetJFrameSwitch();
                    temporaryReader.setreader_num(null);
                    temporaryReader.setreader_pwd(null);
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
                getBackConfirm.setIcon(new ImageIcon("images/一键找回1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                getBackConfirm.setIcon(new ImageIcon("images/一键找回.png"));
            }
        });

        /*
         * 关闭按钮监听器
         * */
        getBackClose.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                forgetJFrameSwitch();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                getBackClose.setIcon(new ImageIcon("images/X1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                getBackClose.setIcon(new ImageIcon("images/X.png"));
            }
        });

    }

    /*
     * 忘记密码窗体开关，复用
     * */
    private void forgetJFrameSwitch() {
        if (getBack.isVisible()) {
            getBack.setVisible(false);
            getBackConfirm.setVisible(false);
            getBackClose.setVisible(false);
            email.setText("");
            email.setVisible(false);
            phone.setVisible(false);
            phone.setText("");
            bigTextField.setVisible(false);//蒙版也去掉
        } else {
            getBack.setVisible(true);
            getBackConfirm.setVisible(true);
            getBackClose.setVisible(true);
            email.setVisible(true);
            phone.setVisible(true);
            bigTextField.setVisible(true);//蒙版打开
        }
    }

    /*
     * 文本框美化方法复用
     * */
    private void beautifyTextField(JTextField jTextfield) {
        jTextfield.setOpaque(false);
        jTextfield.setFont(new Font("黑体", Font.PLAIN, 22));
        jTextfield.setHorizontalAlignment(JTextField.CENTER);
        jTextfield.setBorder(new LineBorder(null, 0));
        jTextfield.setVisible(false);
    }

    public static void main(String[] args) {
        new login();
    }
}
