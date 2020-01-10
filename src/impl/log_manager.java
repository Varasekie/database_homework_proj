package impl;

import db_conn.database;
import entity.table_model;
import entity.borrow_entity;
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
import java.util.ArrayList;

public class log_manager extends JFrame {

    private static final String REFRESH = "select * from borrow";

    private table_model tableModel;
    private JTable table;
    private JLabel rollBack,submit,exit;

    log_manager(){
        super("管理员登入");
        this.setSize(1920,1080);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("managerImg/日志管理图.png");//这是背景图片
        JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
        this.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));//背景标签添加
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
        Container cp=this.getContentPane();
        cp.setLayout(null);
        ((JPanel)cp).setOpaque(false); //注意这里，将内容面板设为透明

        //执行SWI_OFF语句来获得一个表格模型
        tableModel = SQLOperation(REFRESH);
        //获得一个表格对象
        table = new JTable(tableModel);
        beautifyTable(table);//美化表格
        JScrollPane scroll;
        scroll = new JScrollPane(table);
        beautifyScrollPane(scroll);
        scroll.setBounds(405, 302, 1305, 513);
        cp.add(scroll);

        rollBack = new JLabel(new ImageIcon("managerImg/撤回_白色.png"));
        rollBack.setBounds(620,850,65,54);
        rollBack.setVisible(false);
        cp.add(rollBack);

        submit = new JLabel(new ImageIcon("managerImg/提交修改.png"));
        submit.setBounds(755,845,600,60);
        cp.add(submit);

        JTextField adminUser = new JTextField("日志管理员已登录");
        adminUser.setBounds(460,90,460,30);
        adminUser.setOpaque(false);
        adminUser.setEditable(false);
        adminUser.setFont(new Font("黑体", Font.BOLD, 22));
        adminUser.setBorder(new LineBorder(null,0));
        cp.add(adminUser);

        exit = new JLabel(new ImageIcon("managerImg/返回_黑色.png"));
        exit.setBounds(60,55,85,65);
        cp.add(exit);

        submit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int index ,count;
                database dbcon = new database();
                if(table.getCellEditor()!=null){
                    table.getCellEditor().stopCellEditing();
                }

                String sql = "update borrow set reader_num =?,book_num =?," +
                        "borrow_date=?,return_data=? where reader_num=?";
                try {
                    PreparedStatement presta = dbcon.PreparedStatement(sql);
                    //获得JTable中所修改的行数
                    count = tableModel.getEditedIndex().size();
                    //获得JTable中修改的行的数据，更新数据库
                    if(count>0){
                        for (int i = 0; i <count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            presta.setString(1,table.getValueAt(index,0).toString());
                            presta.setString(2,table.getValueAt(index,1).toString());
                            presta.setString(3,table.getValueAt(index,2).toString());
                            presta.setString(4,table.getValueAt(index,3).toString());
                            presta.setString(5,table.getValueAt(index,0).toString());
                            presta.addBatch();
                        }
                    }
                    int i = presta.executeBatch().length;
                    JOptionPane.showMessageDialog(null,"修改成功！ "+i+" 行受影响");
                    /*
                     * 这三行代码是为了刷新表格的被编辑行数
                     * */
                    tableModel = SQLOperation(REFRESH);
                    table.setModel(tableModel);
                    beautifyTable(table);
                } catch (SQLException ex) {
                    String str = "java.sql.BatchUpdateException: \nDuplicate entry 'reader_num-book_num' for key 'PRIMARY'";
                    JOptionPane.showMessageDialog(null,"日志内容无法修改！！");
                    JOptionPane.showMessageDialog(null,str);
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

        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new admin_login();
                dispose();
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


        setVisible(true);
    }

    /*
     * 执行SQL语句的方法来返回一个新的表格模型
     * 复用
     * */
    private table_model SQLOperation(String SQL){

        table_model tableModel1 = new table_model();
        database dbcon;
        dbcon = new database();
        try {
            ResultSet rs = dbcon.executeQuery(SQL);
            for(int i=1;i<=4;i++) {
                tableModel1.addColumn("");
            }
            ArrayList<borrow_entity> v = new ArrayList<>();
            while(rs.next()){
                borrow_entity borrow = new borrow_entity();
                borrow.setreader_num(rs.getString("reader_num"));
                borrow.setbook_num(rs.getString("book_num"));
                borrow.setborrow_data(rs.getString("borrow_data"));
                borrow.setreturn_data(rs.getString("return_data"));
                v.add(borrow);
            }
            rs.close();
            for (borrow_entity borrow : v) {
                tableModel1.addRow(new Object[]{borrow.getreader_num(), borrow.getbook_num(),
                        borrow.getborrow_data(),borrow.getreturn_data()});
            }
            dbcon.closeConn();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableModel1;
    }

    /*
     * 设置一个表格对象,复用
     * */
    private void beautifyTable(JTable table){
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);//设置居中方式
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setVisible(false);
        table.setRowHeight(45);
        table.setShowGrid(false);//不显示网格线
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.setFont(new Font("黑体", Font.PLAIN, 25));
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
    }

    /*
     * 滚动容器美化方法
     * */
    private void beautifyScrollPane(JScrollPane scrollPane){
        scrollPane.setBorder(new LineBorder(null,0));
        scrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());//重写方法
    }

    public static void main(String[] args) {
        new log_manager();
    }
}
