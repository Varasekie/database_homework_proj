package impl;

import db_conn.database;
import entity.book_entity;
import entity.table_model;
import exterior.DemoScrollBarUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class book_manager extends JFrame {

    private static final String REFRESH = "select * from book";
    private static final String REFRESH_ = "select book_book_category.book_num,book_category.category_name from book_book_category left join book_category on book_book_category.category_num = book_category.category_num;";

    private table_model tableModel;
    private JTable table;
    private JLabel rollBack, submit, exit;
    private JLabel addBook, removeBook;
    private JLabel addBookFrame, confirm, close;
    private JTextField book_num, book_name, position, writer, publish, book_img, publish_date, type;
    private JTextField bigTextField;
    private DateFormat dFormat;
    private boolean addBookFrameIsopened = false;

    book_manager() {
        super("管理员登入");
        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("managerImg/图书管理图.png");//这是背景图片
        JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));//背景标签添加
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
        Container cp = this.getContentPane();
        cp.setLayout(null);
        ((JPanel) cp).setOpaque(false); //注意这里，将内容面板设为透明

        /*
         * 日期格式的预处理方便复用
         * */
        dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        book_name = new JTextField();
        book_name.setBounds(900, 260, 340, 40);
        beautifyTextField(book_name);
        book_name.setVisible(false);
        cp.add(book_name);

        book_num = new JTextField();
        book_num.setBounds(900, 327, 340, 40);
        beautifyTextField(book_num);
        book_num.setVisible(false);
        cp.add(book_num);

        position = new JTextField();
        position.setBounds(900, 394, 340, 40);
        beautifyTextField(position);
        position.setVisible(false);
        cp.add(position);

        writer = new JTextField();
        writer.setBounds(900, 461, 340, 40);
        beautifyTextField(writer);
        writer.setVisible(false);
        cp.add(writer);

        publish = new JTextField();
        publish.setBounds(900, 528, 340, 40);
        beautifyTextField(publish);
        publish.setVisible(false);
        cp.add(publish);

        book_img = new JTextField();
        book_img.setBounds(900, 595, 340, 40);
        beautifyTextField(book_img);
        book_img.setVisible(false);
        cp.add(book_img);

        publish_date = new JTextField();
        publish_date.setBounds(900, 662, 340, 40);
        beautifyTextField(publish_date);
        publish_date.setVisible(false);
        cp.add(publish_date);

        type = new JTextField();
        type.setBounds(900, 729, 340, 40);
        beautifyTextField(type);
        type.setVisible(false);
        cp.add(type);


        confirm = new JLabel(new ImageIcon("managerImg/添加成功.png"));
        confirm.setBounds(900, 800, 250, 40);
        confirm.setVisible(false);
        cp.add(confirm);

        addBookFrame = new JLabel(new ImageIcon("managerImg/图书登记.png"));
        addBookFrame.setBounds(760, 200, 550, 680);
        addBookFrame.setVisible(false);
        cp.add(addBookFrame);

        close = new JLabel(new ImageIcon("managerImg/X.png"));
        close.setBounds(1260, 222, 20, 20);
        close.setVisible(false);
        cp.add(close);
        /*
         * 原创方法，将整个页面设置为JTextField（不可编辑，隐藏）
         * 这样点击边缘就能触发所有组件的失去焦点事件，而不需要重新编写监听器
         * 并且可以在基本组件和弹出组件之间添加一层蒙版，防止用户非法操作
         * 省略了好多不必要的代码（本来要定义整个页面的点击事件）
         * */
        bigTextField = new JTextField();
        bigTextField.setBounds(0, 0, 1920, 1080);
        beautifyTextField(bigTextField);
        bigTextField.setEditable(false);
        bigTextField.setVisible(false);
        cp.add(bigTextField);


        //执行SWI_OFF语句来获得一个表格模型
        tableModel = SQLOperation(REFRESH,REFRESH_);
        //获得一个表格对象
        table = new JTable(tableModel);
        beautifyTable(table);//美化表格
        JScrollPane scroll;
        scroll = new JScrollPane(table);
        beautifyScrollPane(scroll);
        scroll.setBounds(405, 302, 1305, 513);
        cp.add(scroll);

        rollBack = new JLabel(new ImageIcon("managerImg/撤回_白色.png"));
        rollBack.setBounds(620, 850, 65, 54);
        rollBack.setVisible(false);
        cp.add(rollBack);

        submit = new JLabel(new ImageIcon("managerImg/提交修改.png"));
        submit.setBounds(755, 845, 600, 60);
        cp.add(submit);

        JTextField adminUser = new JTextField("图书管理员已登录");
        adminUser.setBounds(460, 90, 460, 30);
        adminUser.setOpaque(false);
        adminUser.setEditable(false);
        adminUser.setFont(new Font("黑体", Font.BOLD, 22));
        adminUser.setBorder(new LineBorder(null, 0));
        cp.add(adminUser);

        exit = new JLabel(new ImageIcon("managerImg/返回_黑色.png"));
        exit.setBounds(60, 55, 85, 65);
        cp.add(exit);

        addBook = new JLabel(new ImageIcon("managerImg/添加图书.png"));
        addBook.setBounds(260, 400, 130, 140);
        cp.add(addBook);

        removeBook = new JLabel(new ImageIcon("managerImg/删除图书.png"));
        removeBook.setBounds(260, 600, 130, 140);
        cp.add(removeBook);


        close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                addBookSwitch();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setIcon(new ImageIcon("managerImg/X1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setIcon(new ImageIcon("managerImg/X.png"));
            }
        });

        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                /*
                 * 这边写将图书添加到数据库
                 * */
                database dbcon = new database();
                database dbcon_ = new database();
                String sql = "insert into book(book_name,book_num,is_borrowed,position,writer," +
                        "book_img,publish,publish_date,update_time)values(?,?,?,?,?,?,?,?,?);";
                String sql_ = "insert into book_book_category(book_num,category_num)values(?,?);";
                PreparedStatement prestate;
                PreparedStatement prestate_;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);//不是严格检查时间格式
                if (book_name.getText().length() != 0 && checkbook_num() && checkPosition() && writer.getText().length() != 0
                        && book_img.getText().length() != 0 && checkpublish() && publish_date.getText().length() != 0 && type.getText().length() != 0) {
                    try {
                        format.parse(publish_date.getText());
                        prestate = dbcon.PreparedStatement(sql);
                        prestate_ = dbcon_.PreparedStatement(sql_);
                        prestate.setString(1, book_name.getText());
                        prestate.setString(2, book_num.getText());
                        prestate.setString(3, "No");
                        prestate.setString(4, position.getText());
                        prestate.setString(5, writer.getText());
                        prestate.setString(6, book_img.getText());
                        prestate.setString(7, publish.getText());
                        prestate.setString(8, publish_date.getText());
                        prestate.setString(9, dFormat.format(new Date()));
                        prestate_.setString(1, book_num.getText());
                        prestate_.setString(2, type.getText());
                        prestate.addBatch();
                        prestate.executeBatch();
                        prestate_.addBatch();
                        prestate_.executeBatch();
                        JOptionPane.showMessageDialog(null, "    恭喜，图书插入成功！");
                        addBookSwitch();
                        tableModel = SQLOperation(REFRESH,REFRESH_);
                        //获得一个表格对象
                        table.setModel(tableModel);
                        beautifyTable(table);
                    } catch (SQLException se) {
                        JOptionPane.showMessageDialog(null, "     数据库链接错误");
                        book_num.setText("");
                        System.out.println(se);
                    } catch (ParseException pex) {
                        JOptionPane.showMessageDialog(null, "     时间格式错误");
                        publish_date.setText("");
                    }

                } else {
                    if (!checkbook_num()) {
                        JOptionPane.showMessageDialog(null, "     书籍编号不符合规范\n请参照表格");
                    } else if (!checkPosition()) {
                        JOptionPane.showMessageDialog(null, "     馆藏位置不符合规范\n请参照表格");
                    } else if (!checkpublish()) {
                        JOptionPane.showMessageDialog(null, "     出版信息不符合规范\n请参照表格");
                    } else {
                        JOptionPane.showMessageDialog(null, "     书籍信息不能为空");
                    }
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                confirm.setIcon(new ImageIcon("managerImg/添加成功1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                confirm.setIcon(new ImageIcon("managerImg/添加成功.png"));
            }
        });
        submit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int index, count;
                database dbcon = new database();
                database dbcon_ = new database();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                String sql = "update book set book_name=?,book_num=?,position=?,writer=?,publish=?,publish_date=?,update_time=? where book_num=?;";
                String sql_ = "update book_book_category set category_num=? where book_num=?";

                try {
                    PreparedStatement presta = dbcon.PreparedStatement(sql);
                    PreparedStatement presta_ = dbcon_.PreparedStatement(sql_);
                    //获得JTable中所修改的行数
                    count = tableModel.getEditedIndex().size();
                    //获得JTable中修改的行的数据，更新数据库
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            presta.setString(1, table.getValueAt(index, 1).toString());
                            presta.setString(2, table.getValueAt(index, 0).toString());
                            presta.setString(3, table.getValueAt(index, 2).toString());
                            presta.setString(4, table.getValueAt(index, 3).toString());
                            presta.setString(5, table.getValueAt(index, 4).toString());
                            presta.setString(6, table.getValueAt(index, 5).toString());
                            presta.setString(7, dFormat.format(new Date()));
                            presta.setString(8, table.getValueAt(index, 1).toString());
                            presta_.setString(1, table.getValueAt(index, 6).toString());
                            presta_.setString(2, table.getValueAt(index, 1).toString());
                            presta.addBatch();
                            presta_.addBatch();
                        }
                    }
                    presta.executeBatch();
                    presta_.executeBatch();
                    JOptionPane.showMessageDialog(null, "修改成功！ ");
                    /*
                     * 这三行代码是为了刷新表格的被编辑行数
                     * */
                    tableModel = SQLOperation(REFRESH,REFRESH_);
                    table.setModel(tableModel);
                    beautifyTable(table);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                submit.setIcon(new ImageIcon("managerImg/提交修改1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                submit.setIcon(new ImageIcon("managerImg/提交修改.png"));
            }
        });

        rollBack.addMouseListener(new MouseListener() {
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
                rollBack.setIcon(new ImageIcon("managerImg/撤回_黑色.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rollBack.setIcon(new ImageIcon("managerImg/撤回_白色.png"));
            }
        });

        addBook.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                addBookSwitch();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                addBook.setIcon(new ImageIcon("managerImg/添加图书1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addBook.setIcon(new ImageIcon("managerImg/添加图书.png"));
            }
        });

        removeBook.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                database dbcon = new database();
                if (table.getSelectedRows().length > 0) {
                    //获得JTable中行的序列
                    int[] selRowIndex = table.getSelectedRows();
                    PreparedStatement presta;
                    try {
                        presta = dbcon.PreparedStatement("delete from book where book_num=?");
                        for (int rowIndex : selRowIndex) {
                            presta.setString(1, table.getValueAt(rowIndex, 1).toString());
                            presta.addBatch();
                        }
                        //删除数据库中相应的记录
                        presta.executeBatch();
                        JOptionPane.showMessageDialog(null, "        删除成功！");
                        //重新加载到JTable
                        tableModel = SQLOperation(REFRESH,REFRESH_);
                        table.setModel(tableModel);
                        beautifyTable(table);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "        删除失败！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "        未选中任何图书！");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                removeBook.setIcon(new ImageIcon("managerImg/删除图书1.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeBook.setIcon(new ImageIcon("managerImg/删除图书.png"));
            }
        });

        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new admin_login();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exit.setIcon(new ImageIcon("managerImg/返回_白色.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit.setIcon(new ImageIcon("managerImg/返回_黑色.png"));
            }
        });


        setVisible(true);
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
            ResultSet rs_ = dbcon_.executeQuery(SQL_);
//            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= 7; i++) {
                tableModel1.addColumn("");//去掉表头
            }
            /*
             * 这句话非常重要，不写这句话，那么每次更新，原来的数据都不会消除
             * */
            ArrayList<book_entity> v = new ArrayList<>();
            while (rs.next()) {
                rs_.next();
                book_entity book = new book_entity();
                book.setbook_name(rs.getString("book_name"));
                book.setbook_num(rs.getString("book_num"));
                book.setPosition(rs.getString("Position"));
                book.setwriter(rs.getString("writer"));
                book.setpublish(rs.getString("publish"));
                book.setpublish_date(rs.getString("publish_date"));
                book.setbook_category(rs_.getString("category_name"));
                v.add(book);
            }
            rs.close();
            rs_.close();
            for (book_entity book_entity : v) {
                tableModel1.addRow(new Object[]{book_entity.getbook_name(), book_entity.getbook_num(),
                        book_entity.getPosition(), book_entity.getwriter(), book_entity.getpublish(),
                        book_entity.getpublish_date(), book_entity.getbook_category()});
            }
            dbcon.closeConn();
            dbcon_.closeConn();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableModel1;
    }

    /*
     * 文本框美化方法复用
     * */
    private void beautifyTextField(JTextField jTextfield) {
        jTextfield.setOpaque(false);
        jTextfield.setHorizontalAlignment(JTextField.CENTER);
        jTextfield.setFont(new Font("等线", Font.PLAIN, 22));
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
        table.setRowHeight(30);
        table.setShowGrid(false);//不显示网格线
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.setFont(new Font("黑体", Font.PLAIN, 18));
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
    }

    /*
     * 图书添加窗口是No开启开关
     * */
    private void addBookSwitch() {
        if (addBookFrameIsopened) {
            book_num.setVisible(false);
            book_num.setText("");
            book_name.setVisible(false);
            book_name.setText("");
            position.setVisible(false);
            position.setText("");
            writer.setVisible(false);
            writer.setText("");
            publish.setVisible(false);
            publish.setText("");
            book_img.setVisible(false);
            book_img.setText("");
            publish_date.setVisible(false);
            publish_date.setText("");
            type.setVisible(false);
            type.setText("");
            close.setVisible(false);
            addBookFrame.setVisible(false);
            confirm.setVisible(false);
            bigTextField.setVisible(false);
            addBookFrameIsopened = false;
        } else {
            book_num.setVisible(true);
            book_name.setVisible(true);
            position.setVisible(true);
            writer.setVisible(true);
            publish.setVisible(true);
            book_img.setVisible(true);
            publish_date.setVisible(true);
            type.setVisible(true);
            close.setVisible(true);
            addBookFrame.setVisible(true);
            confirm.setVisible(true);
            bigTextField.setVisible(true);
            addBookFrameIsopened = true;
            book_name.requestFocus(true);
        }

    }

    /*
     * 检查出版社的规范性
     * */
    private boolean checkpublish() {
        String str = publish.getText();
        if (str.length() < 4) {
            return false;
        }
        for (int i = 0; i < str.length() - 2; i++) {
            if (str.substring(i).equals("出版社")) {
                return true;
            }
        }
        return false;
    }

    /*
     * 检查馆藏位置的规范性
     * */
    private boolean checkPosition() {
        String str = position.getText();
        if (str.length() <= 3) {
            return false;
        }
        if (!(str.substring(0, 3).equals("图书馆") | str.substring(0, 3).equals("图书馆"))) {
            return false;
        }
        return true;
    }

    /*
     * 检查图书编号的规范性
     * */
    private boolean checkbook_num() {
        String str = book_num.getText();
        if (!(str.length() > 0)) {
            return false;
        }
        return true;
    }

    /*
     * 滚动容器美化方法
     * */
    private void beautifyScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(null, 0));
        scrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());//重写方法
    }

    public static void main(String[] args) {
        new book_manager();
    }
}
