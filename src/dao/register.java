package dao;

import db_conn.database;
import entity.ide_code;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class register extends JFrame {
    private JTextField userId, nickName;
    private JPasswordField password, confirmPassword;
    private JTextField email, phone, ideCode;
    private JTextField bigTextField;
    private JLabel ideCodeImage, changeIdeCodeImage, regist;
    private JLabel sexRadius;
    private JLabel successfulJFram, successfulConfirm;
    private JLabel userIdMesssage1, userIdMesssage2, passwordMesssage, confirmPasswordMesssage, nickNameMesssage;
    private JLabel emailMessage1, emailMessage2, phoneMessage1, phoneMessage2, ideCodeMessage;
    private boolean isSexMale = true;

    private ide_code[] idecodes = new ide_code[10];
    /*
     * 判断每个输入框是No符合规范
     * */
    private boolean userIdFlag = false;
    private boolean passwordFlag = false;
    private boolean confirmPasswordFlag = false;
    private boolean nickNameFlag = false;
    private boolean emailFlag = false;
    private boolean phoneFlag = false;
    private boolean ideCodeFlag = false;
    private int codeId;

    private int width = 60;
    private int height = 20;
    private int codeCount = 4;
    private int x = 0;
    private int fontHeight;
    private int codeY;
    char[] codeSequence = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'm', 'n', 'q', 'r', 's', 't', 'u','v','w',
            'x','y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'M', 'N', 'Q', 'R', 'S','T','U','V', 'W', 'X', 'Y'
            , 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

    public register() throws IOException {
        super("注册页");
        ImageIcon img = new ImageIcon("images/注册界面.png");
        this.setSize(img.getIconWidth(), img.getIconHeight());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JLabel imgLabel = new JLabel(img);
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        Container cp = this.getContentPane();
        cp.setLayout(null);
        ((JPanel) cp).setOpaque(false);


        /*
         * 注册成功的窗体确定按钮
         * */
        successfulConfirm = new JLabel(new ImageIcon("images/注册_确定.png"));
        successfulConfirm.setBounds(334, 468, 60, 30);
        successfulConfirm.setVisible(false);
        cp.add(successfulConfirm);
        /*
         *注册成功的窗体按钮
         * */
        successfulJFram = new JLabel(new ImageIcon("images/注册成功.png"));
        successfulJFram.setBounds(163, 355, 400, 150);
        successfulJFram.setVisible(false);
        cp.add(successfulJFram);

        /*
         * 原创方法，将整个页面设置为JTextField（不可编辑，隐藏）
         * 这样就能触发所有组件的失去焦点事件，而不需要重新编写监听器
         * 省略了好多不必要的代码（本来要定义整个页面的点击事件）
         * */
        bigTextField = new JTextField();
        bigTextField.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        beautifyTextField(bigTextField);
        bigTextField.setEditable(false);
        bigTextField.setVisible(false);
        cp.add(bigTextField);

        /*
         * 以上为蒙版
         * */


        /*
         * 以下为错误信息提示
         * */
        userIdMesssage1 = new JLabel(new ImageIcon("message/用户名不能为空.png"));
        userIdMesssage1.setBounds(250, 158, 135, 20);
        userIdMesssage1.setVisible(false);
        cp.add(userIdMesssage1);

        userIdMesssage2 = new JLabel(new ImageIcon("message/已注册.png"));
        userIdMesssage2.setBounds(580, 125, 135, 20);
        userIdMesssage2.setVisible(false);
        cp.add(userIdMesssage2);

        passwordMesssage = new JLabel(new ImageIcon("message/密码不符合规范.png"));
        passwordMesssage.setBounds(250, 223, 135, 20);
        passwordMesssage.setVisible(false);
        cp.add(passwordMesssage);

        confirmPasswordMesssage = new JLabel(new ImageIcon("message/两次密码不一致.png"));
        confirmPasswordMesssage.setBounds(250, 290, 135, 20);
        confirmPasswordMesssage.setVisible(false);
        cp.add(confirmPasswordMesssage);

        nickNameMesssage = new JLabel(new ImageIcon("message/昵称不符合规范.png"));
        nickNameMesssage.setBounds(250, 356, 135, 20);
        nickNameMesssage.setVisible(false);
        cp.add(nickNameMesssage);

        emailMessage1 = new JLabel(new ImageIcon("message/邮箱不符合规范.png"));
        emailMessage1.setBounds(250, 488, 135, 20);
        emailMessage1.setVisible(false);
        cp.add(emailMessage1);

        emailMessage2 = new JLabel(new ImageIcon("message/已注册.png"));
        emailMessage2.setBounds(580, 455, 135, 20);
        emailMessage2.setVisible(false);
        cp.add(emailMessage2);

        phoneMessage1 = new JLabel(new ImageIcon("message/手机号不符合规范.png"));
        phoneMessage1.setBounds(250, 555, 150, 20);
        phoneMessage1.setVisible(false);
        cp.add(phoneMessage1);

        phoneMessage2 = new JLabel(new ImageIcon("message/已注册.png"));
        phoneMessage2.setBounds(572, 522, 150, 20);
        phoneMessage2.setVisible(false);
        cp.add(phoneMessage2);

        ideCodeMessage = new JLabel(new ImageIcon("message/验证码错误.png"));
        ideCodeMessage.setBounds(250, 620, 100, 20);
        ideCodeMessage.setVisible(false);
        cp.add(ideCodeMessage);


        /**
         * 验证码
         */
        Random random = new Random();
        x = width / (codeCount + 1);
        fontHeight = height - 2;
        codeY = height - 4;
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setColor(Color.BLACK);
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(8);
            int yl = random.nextInt(8);
            g.drawLine(x, y, x + xl, y + yl);
        }
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {

            String strRand = String.valueOf(codeSequence[random.nextInt(54)]);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            randomCode.append(strRand);
            idecodes[0] = new ide_code();
            idecodes[0].setFileName("ideCode/ideCode.png");
            idecodes[0].setResult(randomCode.toString());
        }

        ImageIO.write(buffImg, "png",new FileOutputStream( "/media/liyi/D/linux/compiler_project/Java/database_homework_proj-master/ideCode/ideCode.png"));

        codeId = (int) (0);
        ideCodeImage = new JLabel(new ImageIcon(idecodes[codeId].getFileName()));
        ideCodeImage.setBounds(500, 572, 115, 54);
        cp.add(ideCodeImage);

        changeIdeCodeImage = new JLabel(new ImageIcon("message/换一个.png"));
        changeIdeCodeImage.setBounds(460, 644, 175, 20);
        changeIdeCodeImage.setVisible(false);
        cp.add(changeIdeCodeImage);




        /*
         * 以下为填写信息的文本框
         * */
        userId = new JTextField();
        userId.setBounds(250, 122, 340, 28);
        beautifyTextField(userId);
        cp.add(userId);

        password = new JPasswordField();
        password.setBounds(250, 188, 340, 28);
        beautifyTextField(password);
        cp.add(password);

        confirmPassword = new JPasswordField();
        confirmPassword.setBounds(250, 254, 340, 28);
        beautifyTextField(confirmPassword);
        cp.add(confirmPassword);

        nickName = new JTextField();
        nickName.setBounds(250, 320, 340, 28);
        beautifyTextField(nickName);
        cp.add(nickName);

        email = new JTextField();
        email.setBounds(250, 452, 340, 28);
        beautifyTextField(email);
        cp.add(email);

        phone = new JTextField();
        phone.setBounds(250, 519, 340, 28);
        beautifyTextField(phone);
        cp.add(phone);

        ideCode = new JTextField();
        ideCode.setBounds(250, 585, 220, 28);
        beautifyTextField(ideCode);
        cp.add(ideCode);

        regist = new JLabel(new ImageIcon("images/立即注册.png"));
        regist.setBounds(135, 722, 450, 34);
        cp.add(regist);

        sexRadius = new JLabel(new ImageIcon("images/圆点男.png"));
        sexRadius.setBounds(333, 389, 179, 28);
        cp.add(sexRadius);


        /*
         *用户名监听器
         * */
        userId.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                userIdMesssage1.setVisible(false);
                userIdMesssage2.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (userId.getText().length() == 0) {
                    userIdMesssage1.setVisible(true);
                } else {
                    database dbcon = new database();
                    String sql = "select reader_num from reader where reader_num = '"
                            + userId.getText() + "';";
                    try {
                        ResultSet rs = dbcon.executeQuery(sql);
                        /*防止结果集为空时报错*/
                        if (rs.next()) {
                            rs.last();
                            userIdMesssage2.setVisible(true);
                        } else {
                            userIdFlag = true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        /*
         *密码监听器
         * */
        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordMesssage.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                String password1 = String.valueOf(password.getPassword());
                int strLen = password1.length();
                int num = 0, character = 0;
                if (strLen < 6) {
                    passwordMesssage.setVisible(true);
                } else {
                    for (int i = 0; i < strLen; i++) {
                        if (password1.charAt(i) >= '0' && password1.charAt(i) <= '9') {
                            num += 1;
                        } else {
                            character += 1;
                        }
                    }
                    if (num == strLen || character == strLen) {
                        passwordMesssage.setVisible(true);
                    } else {//all正确
                        passwordFlag = true;
                    }
                }
                //判断密码是No一致
                passwordJudgement();
            }
        });

        /*
         *密码确认监听器
         * */
        confirmPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                confirmPasswordMesssage.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                //判断密码是No一致
                passwordJudgement();
            }
        });

        /*
         *昵称监听器
         * */
        nickName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                nickNameMesssage.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nickName.getText().length() == 0) {
                    nickNameMesssage.setVisible(true);
                }
                else {
                    nickNameFlag = true;
                }
            }
        });

        /*
         *邮箱监听器
         * */
        email.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                emailMessage1.setVisible(false);
                emailMessage2.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                String str = email.getText();
                int prefix = 0, middle = 0, after = 0, suffix = 0;
                if (email.getText().length() >= 6) {
                    for (int i = 0; i < email.getText().length() - 3; i++) {
                        //当输入不合法字符时直接退出循环
                        if (str.charAt(i) < '.' || str.charAt(i) == '/' || (str.charAt(i) > '9' && str.charAt(i) < '@') || (str.charAt(i) > 'Z' && str.charAt(i) < 'a') || str.charAt(i) > 'z') {
                            break;
                        }
                        prefix++;
                        if (str.charAt(i) == '@') {
                            middle++;
                        }
                        try {
                            if (str.charAt(i - 1) == '@' && (str.charAt(i) != '.')) {
                                after++;
                            }
                        } catch (Exception ae) {
                            System.out.print(" ");
                        }
                        if (str.substring(i, i + 4).equals(".com")) {
                            suffix++;
                            break;
                        }
                    }
                }
                /*
                 * 填写符合邮箱规范的
                 * */
                if (prefix > 3 && middle == 1 && after > 0 && suffix == 1) {
                    database dbcon = new database();
                    String sql = "select Email from reader where Email = '"
                            + email.getText() + "';";
                    try {
                        ResultSet rs = dbcon.executeQuery(sql);
                        if (rs.next()) {
                            rs.last();
                            emailMessage2.setVisible(true);
                            emailFlag = false;
                        } else {
                            emailFlag = true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //No则
                else {
                    emailMessage1.setVisible(true);
                    emailFlag = false;
                }
            }
        });

        /*
         *手机号监听器
         * */
        phone.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                phoneMessage1.setVisible(false);
                phoneMessage2.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                String str = phone.getText();
                if (str.length() < 11) {
                    phoneMessage1.setVisible(true);
                } else {
                    //如果手机号码位数是是11位,并且开头数字是1
                    if (str.length() == 11 && str.charAt(0) == '1' || str.substring(0, 2).equals("86") && str.length() == 13 && str.charAt(2) == '1') {
                        //将带86的13位手机号变成11位
                        if (str.substring(0, 2).equals("86")) {
                            str = str.substring(2);
                        }

                        int num = 0;//统计字符串中数字数量
                        while (num < str.length() && str.charAt(num) >= '0' && str.charAt(num) <= '9') {
                            num++;
                        }
                        //如果all是数字
                        if (num == 11) {
                            database dbcon = new database();
                            String sql = "select Phone from reader where Phone = '" + phone.getText() + "';";
                            try {
                                ResultSet rs = dbcon.executeQuery(sql);
                                /*防止结果集为空时报错*/
                                if (rs.next()) {
                                    rs.last();
                                    phoneMessage2.setVisible(true);
                                    phoneFlag = false;
                                } else {
                                    phoneFlag = true;//完全正确
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            phoneMessage1.setVisible(true);
                            phoneFlag = false;
                        }


                    } else {
                        phoneMessage1.setVisible(true);
                        phoneFlag = false;
                    }

                }
            }
        });

        /*
         *验证码监听器
         * */
        ideCode.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ideCodeMessage.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                /*
                 * 如果相等
                 * */
                if (ideCode.getText().equals(idecodes[0].getResult())) {
                    ideCodeMessage.setVisible(true);
                } else {
                    ideCodeFlag = true;
                }
            }
        });

        ideCodeImage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                codeId = (int) (0);
                ideCodeImage.setIcon(new ImageIcon(idecodes[codeId].getFileName()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                changeIdeCodeImage.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeIdeCodeImage.setVisible(false);
            }
        });

        /*
         * 圆点按钮事件
         * */
        sexRadius.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isSexMale) {
                    sexRadius.setIcon(new ImageIcon("images/圆点女.png"));
                    isSexMale = false;
                } else {
                    sexRadius.setIcon(new ImageIcon("images/圆点男.png"));
                    isSexMale = true;
                }
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

        regist.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                regist.setIcon(new ImageIcon("images/立即注册.png"));
                bigTextField.setVisible(true);
                bigTextField.requestFocus(true);//获得焦点防止用户非法操作
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                /*
                 * 假如判断条件都成立
                 * */

                if (userIdFlag && passwordFlag && confirmPasswordFlag && nickNameFlag && emailFlag && phoneFlag && ideCodeFlag) {
                    database dbcon = new database();
                    database dbcon_ = new database();
                    DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sql = "insert into reader(reader_num," +
                            "reader_name,reader_pwd,registe_time," +
                            "gender,email,phone,Note)values(?,?,?,?,?,?,?,?);";
                    String sql_ = "insert into reader_reader_category" +
                            "(reader_num,category_num)values(?,?);";
                    PreparedStatement prestate;
                    PreparedStatement prestate_;
                    try {
                        prestate = dbcon.PreparedStatement(sql);
                        prestate_ = dbcon_.PreparedStatement(sql);
                        prestate.setString(1, userId.getText());
                        prestate.setString(2, nickName.getText());
                        prestate.setString(3, String.valueOf(password.getPassword()));
                        prestate_.setString(2, "1");//默认为读者权限，只有超级管理员才能授予读者更高权限
                        prestate_.setString(1, userId.getText());//默认为读者权限，只有超级管理员才能授予读者更高权限
                        prestate.setString(4, dFormat.format(new Date()));//注册时间
                        prestate.setString(5, isSexMale ? "men" : "women");
                        prestate.setString(6, email.getText());
                        prestate.setString(7, phone.getText());
                        prestate.setString(8, "None");
                        prestate.addBatch();
                        prestate.executeBatch();
                        prestate_.addBatch();
                        prestate_.executeBatch();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    /*
                     * 这边编写插入成功之后的事情
                     * */
                    successfulConfirm.setVisible(true);
                    successfulJFram.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "信息填写错误", "提示", JOptionPane.WARNING_MESSAGE);
                    bigTextField.setVisible(false);
//                    bigTextField.requestFocus(false);//获得焦点防止用户非法操作
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                regist.setIcon(new ImageIcon("images/立即注册1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                regist.setIcon(new ImageIcon("images/立即注册.png"));
            }
        });

        successfulConfirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                successfulConfirm.setVisible(false);
                successfulJFram.setVisible(false);
                bigTextField.setVisible(false);
                dispose();//关闭窗体
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                successfulConfirm.setIcon(new ImageIcon("images/注册_确定1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                successfulConfirm.setIcon(new ImageIcon("images/注册_确定.png"));
            }
        });

    }

    /*
     * 文本框美化方法复用
     * */
    private void beautifyTextField(JTextField jTextfield) {
        jTextfield.setOpaque(false);
        jTextfield.setFont(new Font("等线", Font.PLAIN, 22));
        jTextfield.setBorder(new LineBorder(null, 0));
    }

    private void passwordJudgement() {
        String password1 = String.valueOf(confirmPassword.getPassword());
        String password2 = String.valueOf(password.getPassword());
        if (password1.length() > 0) {
            if (!password1.equals(password2)) {
                confirmPasswordMesssage.setVisible(true);
            } else {
                confirmPasswordMesssage.setVisible(false);
                confirmPasswordFlag = true;
            }
        } else {
            confirmPasswordMesssage.setVisible(true);
        }
    }


    public static void main(String[] args) throws IOException {
        register w = new register();
        w.setVisible(true);
    }
}
