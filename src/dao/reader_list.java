package dao;

import db_conn.database;
import entity.book_entity;
import entity.table_model;
import exterior.DemoScrollBarUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class reader_list extends JFrame {
    /*
     * SQL语句常量(Mysql数据库)
     * */
    private static final String SWI_OFF = "select * from book";
    private static final String SWI_OFF_ = "select book_book_category.book_num,book_category.category_name from book_book_category left join book_category on book_book_category.category_num = book_category.category_num";
    private static final String SWI_ON = "select * from book where is_borrowed = 'No' ";
    private static final String SWI_ON_ = "select * from book where is_borrowed = 'No' ";
    private static final String SEARCH_BY_KEY_VALUE = "select * from book where book_name like";
    private static final String SEARCH_BY_TYPE = "select * from book where book_category = ";
    private static final String SEARCH_BY_publish = "select * from book where publish = ";
    private static final String SEARCH_BY_PUBLIC_DATE = "select * from book where publish_date like ";
    private static final String SEARCH_ALL_NUMBER = "select count(*) '库存量' from book where book_name = ";
    private static final String SEARCH_BORROW_NUMBER = "select count(*) '借出量' from book where is_borrowed = 'yes' and book_name = ";
    private static final String SEARCH_LEFT_NUMBER = "select count(*) '剩余量' from book where is_borrowed = 'No' and book_name = ";
    private static final String BORROWED_BOOK_NUMBER = "select count(*) '借书量' from borrow where reader_num = ";

    private boolean swi_on = false;
    private ArrayList<book_entity> v = new ArrayList<>();
    private String book_imgURL;
    private String storedNum, borrowedNum, leftNum;
    private DateFormat dFormat;
    private int borrowedNumber = 0;

    private table_model tableModel;
    private JTable table, typeTable, publishTable, pulicDateTable;
    private JScrollPane scroll1, scroll2, scroll3;
    private JLabel exit, search, borrow, swi, select1, select2, select3;//定义标签（作用为按钮）
    private JLabel myBookshelf;
    private JLabel keyQueryLabel, typeQueryLabel, publishQueryLabel, publish_dateQueryLabel;//定义四种不同的查找方式的背景字体
    private JTextField keyQuery, typeQuery, publishQuery, publish_dateQuery;//定义四种不同的查找方式
    private JTextField book_name, position, writer, publishName, publicTime;//定义 书本基本信息
    private JTextField storedNumText, borrowedNumText, leftNumText;    //定义 库存量，借出量，剩余量
    private JTextField bigTextField;
    private JLabel book_img;//用来存放书本图片


    reader_list() {
        super("图书管理系统");
        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel imgLabel = new JLabel(new ImageIcon("images/图书管理系统.png"));//将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));//背景标签
        imgLabel.setBounds(0, 0, 1920, 1080);//设置背景标签的位置
        Container cp = this.getContentPane();
        cp.setLayout(null);
        ((JPanel) cp).setOpaque(false); //注意这里，将内容面板设为透明
        /*
         * 日期格式的预处理方便复用
         * */
        dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*
         *静态的选择框，之后会更改
         * */
        String[][] cellData = {{"全部"}, {"计算机"}, {"名著"}};
        String[] columnNames = {""};
        typeTable = new JTable(cellData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }//表格不允许被编辑
        };
        beautifyTable(typeTable);
        scroll1 = new JScrollPane(typeTable);
        beautifyScrollPane(scroll1);
        scroll1.setBounds(596, 200, 200, 120);
        scroll1.setVisible(false);
        cp.add(scroll1);
        String[][] cellData1 = {{"全部"}, {"A416出版社"}, {"清华大学出版社"}};
        publishTable = new JTable(cellData1, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }//表格不允许被编辑
        };
        beautifyTable(publishTable);
        scroll2 = new JScrollPane(publishTable);
        beautifyScrollPane(scroll2);
        scroll2.setBounds(860, 200, 200, 120);
        scroll2.setVisible(false);
        cp.add(scroll2);
        String[][] cellData2 = {{"全部"}, {"2019-11"},{"2019-12"}};
        pulicDateTable = new JTable(cellData2, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }//表格不允许被编辑
        };
        beautifyTable(pulicDateTable);
        scroll3 = new JScrollPane(pulicDateTable);
        beautifyScrollPane(scroll3);
        scroll3.setBounds(1115, 200, 200, 120);
        scroll3.setVisible(false);
        cp.add(scroll3);
        /*
         *以上为静态的选择框，之后会更改
         * */
        //执行SWI_OFF语句来获得一个表格模型
        tableModel = SQLOperation(SWI_OFF,SWI_OFF_);
        //获得一个表格对象
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }//表格不允许被编辑
        };
        beautifyTable(table);//美化表格
        JScrollPane scroll;
        scroll = new JScrollPane(table);
        beautifyScrollPane(scroll);
        scroll.setBounds(590, 300, 730, 513);
        cp.add(scroll);
        JLabel welcome = new JLabel(new ImageIcon("images/欢迎.png"));
        welcome.setBounds(1550, 15, 40, 40);
        cp.add(welcome);
        if (login.formalReader == null) {
            exit = new JLabel(new ImageIcon("images/借书请登录.png"));
        }
        else {
            exit = new JLabel(new ImageIcon("images/退出登录.png"));
        }
        exit.setBounds(1672, 15, 100, 40);
        cp.add(exit);

        search = new JLabel(new ImageIcon("images/搜索图标.png"));
        search.setBounds(1132, 68, 183, 100);
        cp.add(search);

        swi = new JLabel(new ImageIcon("images/off.png"));
        swi.setBounds(1120, 796, 183, 100);
        cp.add(swi);

        borrow = new JLabel(new ImageIcon("images/借阅.png"));
        borrow.setBounds(1458, 870, 400, 100);
        cp.add(borrow);


        keyQuery = new JTextField();
        keyQuery.setBounds(650, 98, 400, 40);
        beautifyTextField(keyQuery);
        cp.add(keyQuery);

        typeQuery = new JTextField();
        typeQuery.setBounds(620, 169, 150, 40);
        typeQuery.setEditable(false);
        beautifyTextField(typeQuery);
        cp.add(typeQuery);

        publishQuery = new JTextField();
        publishQuery.setBounds(885, 169, 150, 40);
        publishQuery.setEditable(false);
        beautifyTextField(publishQuery);
        cp.add(publishQuery);

        publish_dateQuery = new JTextField();
        publish_dateQuery.setBounds(1141, 169, 150, 40);
        publish_dateQuery.setEditable(false);
        beautifyTextField(publish_dateQuery);
        cp.add(publish_dateQuery);

        myBookshelf = new JLabel(new ImageIcon("book/空书架.png"));
        myBookshelf.setBounds(45, 490, 349, 252);
        cp.add(myBookshelf);
        /*
         * 原创方法，将整个页面设置为JTextField（不可编辑，隐藏）
         * 这样就能触发所有组件的失去焦点事件，而不需要重新编写监听器
         * 省略了好多不必要的代码（本来要定义整个页面的点击事件）
         * */
        bigTextField = new JTextField();
        bigTextField.setBounds(0, 0, 1920, 1080);
        beautifyTextField(bigTextField);
        bigTextField.setEditable(false);
        cp.add(bigTextField);


        book_imgURL = "book/tjxxff.png";
        book_img = new JLabel(new ImageIcon(book_imgURL));
        book_img.setBounds(1420, 228, 400, 400);
        cp.add(book_img);

        writer = new JTextField(v.get(0).getwriter());
        beautifyTextField(writer);
        writer.setBounds(1550, 680, 300, 40);
        writer.setEditable(false);
        cp.add(writer);

        publishName = new JTextField(v.get(0).getpublish());
        beautifyTextField(publishName);
        publishName.setBounds(1550, 735, 300, 40);
        publishName.setEditable(false);
        cp.add(publishName);

        publicTime = new JTextField(v.get(0).getpublish_date());
        beautifyTextField(publicTime);
        publicTime.setBounds(1550, 795, 300, 40);
        publicTime.setEditable(false);
        cp.add(publicTime);

        book_name = new JTextField(v.get(0).getbook_name());
        book_name.setBounds(1470, 169, 300, 40);
        beautifyTextField(book_name);
        book_name.setEditable(false);
        cp.add(book_name);

        position = new JTextField(v.get(0).getPosition());
        position.setBounds(1550, 115, 300, 40);
        beautifyTextField(position);
        position.setEditable(false);
        cp.add(position);

        JTextField user;
        if (login.formalReader == null || login.formalReader.getreader_name() == null) {
            user = new JTextField("tourist");
        }
        else {
            user = new JTextField(login.formalReader.getreader_name());
        }
        user.setBounds(1450, 15, 100, 40);
        beautifyTextField(user);
        user.setFont(new Font("黑体", Font.PLAIN, 19));
        user.setForeground(Color.white);
        user.setEditable(false);
        cp.add(user);

        select1 = new JLabel(new ImageIcon("images/三角左.png"));
        select1.setBounds(770, 179, 20, 20);
        cp.add(select1);

        select2 = new JLabel(new ImageIcon("images/三角左.png"));
        select2.setBounds(1034, 179, 20, 20);
        cp.add(select2);

        select3 = new JLabel(new ImageIcon("images/三角左.png"));
        select3.setBounds(1290, 179, 20, 20);
        cp.add(select3);

        keyQueryLabel = new JLabel(new ImageIcon("images/关键字查找.png"));
        keyQueryLabel.setBounds(805, 99, 100, 40);
        cp.add(keyQueryLabel);

        typeQueryLabel = new JLabel(new ImageIcon("images/类别查找.png"));
        typeQueryLabel.setBounds(643, 169, 100, 40);
        cp.add(typeQueryLabel);

        publishQueryLabel = new JLabel(new ImageIcon("images/出版社查找.png"));
        publishQueryLabel.setBounds(911, 169, 100, 40);
        cp.add(publishQueryLabel);

        publish_dateQueryLabel = new JLabel(new ImageIcon("images/出版时间查找.png"));
        publish_dateQueryLabel.setBounds(1159, 169, 120, 40);
        cp.add(publish_dateQueryLabel);

        storedNumText = new JTextField("3");//初始化为第一本书的信息
        beautifyTextField(storedNumText);
        storedNumText.setBounds(710, 937, 40, 40);
        storedNumText.setEditable(false);
        cp.add(storedNumText);

        borrowedNumText = new JTextField("2");//初始化为第一本书的信息
        beautifyTextField(borrowedNumText);
        borrowedNumText.setBounds(1025, 937, 40, 40);
        borrowedNumText.setEditable(false);
        cp.add(borrowedNumText);

        leftNumText = new JTextField("1");//初始化为第一本书的信息
        beautifyTextField(leftNumText);
        leftNumText.setBounds(1285, 937, 40, 40);
        leftNumText.setEditable(false);
        cp.add(leftNumText);

        /*
         * 初始化书架
         * */
        if (login.formalReader != null) {
            SQLSetNum(BORROWED_BOOK_NUMBER + "'" + login.formalReader.getreader_num() +
                    "' and return_data is null;");
            upDateBookshelfImage();
        }
        /*
         * "借阅"按钮鼠标点击事件
         * */
        borrow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                // 设置按钮显示效果
                UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("黑体", Font.BOLD, 15)));
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("黑体", Font.BOLD, 15)));
                if (login.formalReader.getreader_name() == null) {
                    JOptionPane.showMessageDialog(null, "  借/还书请先登录！");
                }
                else {
                    if (table.getValueAt(table.getSelectedRow(), 0) != null) {
                        String str_book_num = table.getValueAt(table.getSelectedRow(), 1).toString();
                        if (table.getValueAt(table.getSelectedRow(), 2).toString().equals("No")) {
                            /*
                             * 下面修改数据库的内容
                             * */
                            database dbcon = new database();
                            String sql1 = "update book set is_borrowed= 'yes' where book_num = '" + str_book_num + "';";
                            String sql2 = "insert into borrow(reader_num,book_num,borrow_data,return_data) values('" +
                                    login.formalReader.getreader_num() + "','" + str_book_num + "','" +
                                    dFormat.format(new Date()) + "',null);";
                            try {
                                if (dbcon.executeUpdate(sql1) != 0) {
                                    dbcon.executeUpdate(sql2);
                                    /*
                                     * 这里修改列表页的内容
                                     * */
                                    table.setValueAt("yes", table.getSelectedRow(), 2);
                                    borrow.setIcon(new ImageIcon("images/归还.png"));
                                    SQLSetNum(BORROWED_BOOK_NUMBER + "'" + login.formalReader.getreader_num() +
                                            "' and return_data is null;");
                                    upDateBookshelfImage();
                                    JOptionPane.showMessageDialog(null, "     借阅成功！");
                                } else {
                                    JOptionPane.showMessageDialog(null, "     借阅失败！");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            /*
                             *上面修改数据库的内容
                             * */
                        } else {
                            /*
                             * 下面修改数据库的内容
                             * */
                            database dbcon = new database();
                            database dbcon_ = new database();
                            database dbcon__ = new database();
                            String sql1 = "update borrow set return_data = '" + dFormat.format(new Date()) + "' where book_num= '" +
                                    str_book_num + "' and reader_num = '" + login.formalReader.getreader_num() + "';";
                            String sql2 = "update book set is_borrowed= 'No' where book_num = '" + str_book_num + "';";
                            String sql3 = "select * from borrow where book_num =" + str_book_num;
                            try {
                                /*
                                 * if语句判断的同时就会执行sql1的语句
                                 * 所以不需要写第二遍executeUpdate(sql1)
                                 * */
                                if (dbcon.executeUpdate(sql1) != 0) {
                                    /*
                                     * 当执行归还书本的sql语句成功时，即影响的行数非零时
                                     * 执行将图书表中的是No借阅改成No
                                     * */
                                    dbcon.executeUpdate(sql2);
                                    ResultSet rs = dbcon_.executeQuery(sql3);
                                    rs.next();
                                    SimpleDateFormat dataf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat dataf_=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date bdata  = dataf.parse(rs.getString("borrow_data"));
                                    Date rdata = dataf_.parse(rs.getString("return_data"));
                                    long rto = rdata.getTime() -bdata.getTime();
                                    if(rto > 10000){
                                        JOptionPane.showMessageDialog(null, "     归还超时！");
                                        PreparedStatement prestate;
                                        String sql4 = "insert into return_out_time(reader_num,book_num,out_time,write_data)values(?,?,?,?);";
                                        prestate = dbcon.PreparedStatement(sql4);
                                        prestate.setString(1, rs.getString("reader_num"));
                                        prestate.setString(2, rs.getString("book_num"));
                                        prestate.setString(3, String.valueOf(rto));
                                        prestate.setString(4, rs.getString("return_data"));//默认为读者权限，只有超级管理员才能授予读者更高权限
                                        prestate.addBatch();
                                        prestate.executeBatch();
                                    }
                                    /*
                                     * 这里修改列表页的内容
                                     * */
                                    table.setValueAt("No", table.getSelectedRow(), 2);
                                    borrow.setIcon(new ImageIcon("images/借阅.png"));
                                    SQLSetNum(BORROWED_BOOK_NUMBER + "'" + login.formalReader.getreader_num() +
                                            "' and return_data is null;");
                                    upDateBookshelfImage();
                                    JOptionPane.showMessageDialog(null, "     归还成功！");
                                } else {
                                    JOptionPane.showMessageDialog(null, "亲，您未借阅该书~\n不需要归还~");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            /*
                             *上面修改数据库的内容
                             * */
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "未选中任何图书");
                    }
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
        /*
         * "开关"鼠标点击事件
         * */
        swi.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!swi_on) {
                    swi.setIcon(new ImageIcon("images/on.png"));
                    swi_on = true;
                    if (keyQuery.getText().length() != 0) {

                        tableModel = SQLOperation(SWI_ON,SWI_ON_);
                    } else {
                        tableModel = SQLOperation(SWI_ON,SWI_ON_);
                    }

                    table.setModel(tableModel);
                    beautifyTable(table);//每一次都要美化表格，No则要出错

                } else {
                    swi.setIcon(new ImageIcon("images/off.png"));
                    swi_on = false;
                    tableModel = SQLOperation(SWI_OFF,SWI_OFF_);
                    table.setModel(tableModel);
                    beautifyTable(table);//每一次都要美化表格，No则要出错
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
        /*
         * "退出登录"点击事件
         * */
        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*
                 * 将书籍列表销毁，No则将会出现用户信息错乱的情况
                 * */
                dispose();
                login login = new login();
                login.setVisible(true);
                //下面两句用来清除之前的账号信息
                dao.login.formalReader = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (login.formalReader == null) {
                    exit.setIcon(new ImageIcon("images/借书请登录1.png"));
                } else {
                    exit.setIcon(new ImageIcon("images/退出登录1.png"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (login.formalReader == null) {
                    exit.setIcon(new ImageIcon("images/借书请登录.png"));
                } else {
                    exit.setIcon(new ImageIcon("images/退出登录.png"));
                }
            }
        });
        /*
         * "查询"点击事件
         * */
        search.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                search.setIcon(new ImageIcon("images/搜索图标.png"));
                int num = keyQuery.getText().length();
                /*
                 * 让每个字都有效
                 * */
                String[] s = new String[num];
                for (int i = 0; i < num; i++) {//让每个字都有效
                    s[i] = "'%" + keyQuery.getText().substring(i, i + 1) + "%'";//两边有单引号
                }
                StringBuilder SQL = new StringBuilder(SEARCH_BY_KEY_VALUE);
                StringBuilder SQL_ = new StringBuilder("");
                for (int i = 0; i < num - 1; i++) {
                    SQL.append(s[i]).append("or book_name like ");//相当于SQL+= s[i]+"or book_name like ";
                    //防止出现数组下标越界的情况
                }
                try {
                    SQL.append(s[num - 1]);
                    tableModel = SQLOperation(SQL.toString(),SQL_.toString());
                    table.setModel(tableModel);
                    //db每一次都要美化表格，No则要出错
                    beautifyTable(table);
                } catch (ArrayIndexOutOfBoundsException ae) {
                    System.out.println("数组下标越界");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                search.setIcon(new ImageIcon("images/搜索图标1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                search.setIcon(new ImageIcon("images/搜索图标.png"));
            }
        });
        /*
         * "关键字查找"获得焦点事件
         * */
        keyQuery.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                keyQueryLabel.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (keyQuery.getText().length() == 0) {
                    keyQueryLabel.setVisible(true);
                }
            }
        });
        /*
         * "种类查找"获得焦点事件
         * */
        typeQuery.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                select1.setIcon(new ImageIcon("images/三角下.png"));
                typeQueryLabel.setVisible(false);
                /*
                 * 这里将会产生一个下拉菜单
                 * */
                scroll1.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                select1.setIcon(new ImageIcon("images/三角左.png"));
                if (typeQuery.getText().length() == 0) {
                    typeQueryLabel.setVisible(true);
                }
                int num = typeQuery.getText().length();//获得种类查找的字符串长度
                String SQL = SEARCH_BY_TYPE + "'" + typeQuery.getText() + "'";
                String SQL_ = SEARCH_BY_TYPE + "'" + typeQuery.getText() + "'";
                if (num != 0) {
                    tableModel = SQLOperation(SQL,SQL_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                } else {
                    tableModel = SQLOperation(SWI_OFF,SWI_OFF_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                }
                /*
                 * 这里下拉菜单将会消失
                 * */
                scroll1.setVisible(false);
            }
        });
        /*
         * "出版社查找"获得焦点事件
         * */
        publishQuery.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                select2.setIcon(new ImageIcon("images/三角下.png"));
                publishQueryLabel.setVisible(false);
                /*
                 * 这里将会产生一个下拉菜单
                 * */
                scroll2.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                select2.setIcon(new ImageIcon("images/三角左.png"));
                if (publishQuery.getText().length() == 0) {
                    publishQueryLabel.setVisible(true);
                }
                int num = publishQuery.getText().length();//获得出版社查找的字符串长度
                String SQL = SEARCH_BY_publish + "'" + publishQuery.getText() + "'";
                String SQL_ = "";
                if (num != 0) {
                    tableModel = SQLOperation(SQL,SQL_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                } else {
                    tableModel = SQLOperation(SWI_OFF,SWI_OFF_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                }
                /*
                 * 这里下拉菜单将会消失
                 * */
                scroll2.setVisible(false);
            }
        });
        /*
         * "出版日期查找"获得焦点事件
         * */
        publish_dateQuery.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                select3.setIcon(new ImageIcon("images/三角下.png"));
                publish_dateQueryLabel.setVisible(false);
                /*
                 * 这里将会产生一个下拉菜单
                 * */
                scroll3.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                select3.setIcon(new ImageIcon("images/三角左.png"));
                if (publish_dateQuery.getText().length() == 0) {
                    publish_dateQueryLabel.setVisible(true);
                }
                int num = publish_dateQuery.getText().length();//获得出版日期查找的字符串长度
                String SQL = SEARCH_BY_PUBLIC_DATE + "'%" + publish_dateQuery.getText() + "%'";
                String SQL_ = "";
                if (num != 0) {
                    tableModel = SQLOperation(SQL,SQL_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                } else {
                    tableModel = SQLOperation(SWI_OFF,SWI_OFF_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                }
                /*
                 * 这里下拉菜单将会消失
                 * */
                scroll3.setVisible(false);
            }
        });
        /*
         * "种类列表"鼠标点击事件
         * */
        typeTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                String content = typeTable.getValueAt(typeTable.getSelectedRow(), 0).toString();
                if (content != null) {
                    if (!content.equals("全部")) {
                        typeQuery.setText(content);
                    } else {
                        typeQuery.setText(null);
                    }
                }
                //大文本框获得焦点相当于选择框失去焦点
                bigTextField.requestFocus(true);
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
        /*
         * "出版社列表"鼠标点击事件
         * */
        publishTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                String content = publishTable.getValueAt(publishTable.getSelectedRow(), 0).toString();
                if (content != null) {
                    if (!content.equals("全部")) {
                        publishQuery.setText(content);
                    } else {
                        publishQuery.setText(null);
                    }
                }
                //大文本框获得焦点相当于选择框失去焦点
                bigTextField.requestFocus(true);
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
        /*
         * "出版时间列表"鼠标点击事件
         * */
        pulicDateTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                String content = pulicDateTable.getValueAt(pulicDateTable.getSelectedRow(), 0).toString();
                if (content != null) {
                    if (!content.equals("全部")) {
                        publish_dateQuery.setText(content);
                    } else {
                        publish_dateQuery.setText(null);
                    }
                }
                //大文本框获得焦点相当于选择框失去焦点
                bigTextField.requestFocus(true);
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
        /*
         * "总列表"鼠标点击事件
         * */
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (table.getValueAt(table.getSelectedRow(), 0) != null) {
                    book_imgURL = v.get(table.getSelectedRow()).getbook_img();
                    //更新视图
                    book_img.setIcon(new ImageIcon(book_imgURL));
                    position.setText(v.get(table.getSelectedRow()).getPosition());
                    book_name.setText(v.get(table.getSelectedRow()).getbook_name());
                    writer.setText(v.get(table.getSelectedRow()).getwriter());
                    publishName.setText(v.get(table.getSelectedRow()).getpublish());
                    publicTime.setText(v.get(table.getSelectedRow()).getpublish_date());
                    System.out.println(table.getValueAt(table.getSelectedRow(), 2).toString());
                    if (table.getValueAt(table.getSelectedRow(), 2).toString().equals("No")) {
                        borrow.setIcon(new ImageIcon("images/借阅.png"));
                    } else {
                        borrow.setIcon(new ImageIcon("images/归还.png"));
                    }
                    /*
                     * 通过SQL语句来获得书本的库存，借出量，存储量
                     * */
                    SQLSetNum(SEARCH_ALL_NUMBER + "'" + v.get(table.getSelectedRow()).getbook_name() + "'"
                            , SEARCH_BORROW_NUMBER + "'" + v.get(table.getSelectedRow()).getbook_name() + "'"
                            , SEARCH_LEFT_NUMBER + "'" + v.get(table.getSelectedRow()).getbook_name() + "'");
                    storedNumText.setText(storedNum);
                    borrowedNumText.setText(borrowedNum);
                    leftNumText.setText(leftNum);
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
        /*
         * 键盘回车事件
         * */
        keyQuery.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    int num = keyQuery.getText().length();
                    /*
                     * 让每个字都有效
                     * */
                    String[] s = new String[num];
                    for (int i = 0; i < num; i++) {//让每个字都有效
                        s[i] = "'%" + keyQuery.getText().substring(i, i + 1) + "%'";//两边有单引号
                    }
                    StringBuilder SQL = new StringBuilder(SEARCH_BY_KEY_VALUE);
                    StringBuilder SQL_ = new StringBuilder("");
                    for (int i = 0; i < num - 1; i++) {
                        SQL.append(s[i]).append("or book_name like ");
                        //防止出现数组下标越界的情况
                    }
                    try {
                        SQL.append(s[num - 1]);
                        tableModel = SQLOperation(SQL.toString(),SQL_.toString());
                        table.setModel(tableModel);
                        //每一次都要美化表格，No则要出错
                        beautifyTable(table);
                    } catch (ArrayIndexOutOfBoundsException ae) {
                        System.out.println("数组下标越界");
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

        myBookshelf.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                new book_shelf();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                myBookshelf.setIcon(new ImageIcon("book/多本书.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                upDateBookshelfImage();
            }
        });

    }

    /*
     * 依据借阅书本数量来修改图片
     * */
    private void upDateBookshelfImage() {
        if (borrowedNumber == 0) {
            myBookshelf.setIcon(new ImageIcon("book/空书架.png"));
        } else if (borrowedNumber == 1) {
            myBookshelf.setIcon(new ImageIcon("book/一本书.png"));
        } else if (borrowedNumber == 2) {
            myBookshelf.setIcon(new ImageIcon("book/两本书.png"));
        } else if (borrowedNumber == 3) {
            myBookshelf.setIcon(new ImageIcon("book/三本书.png"));
        } else if (borrowedNumber == 4) {
            myBookshelf.setIcon(new ImageIcon("book/四本书.png"));
        } else if (borrowedNumber == 5) {
            myBookshelf.setIcon(new ImageIcon("book/五本书.png"));
        } else {
            myBookshelf.setIcon(new ImageIcon("book/多本书.png"));
        }
    }

    /*
     * 滚动容器美化方法
     * */
    private void beautifyScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(null, 0));
        scrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());//重写方法
    }

    /*
     * 文本框美化方法复用
     * */
    private void beautifyTextField(JTextField jTextfield) {
        jTextfield.setOpaque(false);
        jTextfield.setHorizontalAlignment(JTextField.CENTER);
        jTextfield.setFont(new Font("黑体", Font.PLAIN, 22));
        jTextfield.setBorder(new LineBorder(null, 0));
    }

    /*
     * 设置一个表格对象,复用
     * */
    private void beautifyTable(JTable table) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);//设置居中方式
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setVisible(false);
        table.setRowHeight(40);
        table.setShowGrid(false);//不显示网格线
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.setFont(new Font("黑体", Font.PLAIN, 22));
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
    }

    /*
     * 执行SQL语句的方法来返回一个新的表格模型
     * 复用
     * */
    private table_model SQLOperation(String SQL,String SQL_) {

        table_model tableModel1 = new table_model();
        database dbcon;
        dbcon = new database();
        database dbcon_;
        dbcon_ = new database();
        try {
            ResultSet rs = dbcon.executeQuery(SQL);
            ResultSetMetaData rsmd = rs.getMetaData();
            ResultSet rs_ = dbcon_.executeQuery(SQL_);

            for (int i = 1; i <= 3; i++) {
                tableModel1.addColumn(rsmd.getColumnName(i));
            }
            /*
             * 这句话非常重要，不写这句话，那么每次更新，原来的数据都不会消除
             * */
            v = new ArrayList<>();
            while (rs.next()) {
                rs_.next();
                book_entity book = new book_entity();
                book.setbook_name(rs.getString("book_name"));
                book.setbook_num(rs.getString("book_num"));
                book.setis_borrowed(rs.getString("is_borrowed"));
                book.setPosition(rs.getString("Position"));
                book.setwriter(rs.getString("writer"));
                book.setbook_img(rs.getString("book_img"));
                book.setpublish(rs.getString("publish"));
                book.setpublish_date(rs.getString("publish_date"));
                if (!SQL_.equals("")) {
                    book.setbook_category(rs_.getString("category_name"));
                }
                v.add(book);
            }
            rs.close();
            for (book_entity book_entity : v) {
                tableModel1.addRow(new Object[]{book_entity.getbook_name(), book_entity.getbook_num(), book_entity.getis_borrowed()});
            }
            dbcon.closeConn();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableModel1;
    }

    /*
     * 执行SQL语句的方法来设置几个数据对象
     * 一次连接多次查询
     * 复用
     * */
    private void SQLSetNum(String SQL1, String SQL2, String SQL3) {
        database dbcon;
        dbcon = new database();
        try {
            ResultSet rs1 = dbcon.executeQuery(SQL1);
            ResultSet rs2 = dbcon.executeQuery(SQL2);
            ResultSet rs3 = dbcon.executeQuery(SQL3);
            rs1.last();
            rs2.last();
            rs3.last();
            this.storedNum = rs1.getString(1);//获得结果集
            this.borrowedNum = rs2.getString(1);
            this.leftNum = rs3.getString(1);
            rs1.close();
            rs2.close();
            rs3.close();
            dbcon.closeConn();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * 执行SQL语句的方法来设置一个数据对象
     * 复用
     * */
    private void SQLSetNum(String SQL) {
        database dbcon;
        dbcon = new database();
        try {
            ResultSet rs = dbcon.executeQuery(SQL);
            rs.last();
            this.borrowedNumber = rs.getInt(1);//获得结果集
            rs.close();
            dbcon.closeConn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        reader_list list = new reader_list();
        list.setVisible(true);
    }
}
