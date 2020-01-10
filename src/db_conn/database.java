package db_conn;
import java.sql.*;
public class database {
    protected Connection dbConn = null;
    private Statement stateMent;
    public database(){
        String driverName = "com.mysql.jdbc.Driver";
        String connstr = "jdbc:mysql://localhost:3306/library?"+
                "useUnicode=true&characterEncoding=utf-8&useSSL=false";;
        String usn = "root";
        String pwd = "0523";
        try {
            Class.forName(driverName);
            dbConn=DriverManager.getConnection(connstr,usn,pwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public int executeUpdate(String sql) throws SQLException{
        stateMent = dbConn.createStatement();
        return stateMent.executeUpdate(sql);
    }
    public ResultSet executeQuery(String sql) throws SQLException{
        stateMent = dbConn.createStatement();
        return stateMent.executeQuery(sql);
    }
    public void closeConn() throws SQLException {
        stateMent.close();
        dbConn.close();
    }
    public PreparedStatement PreparedStatement(String sql) throws SQLException{
        return dbConn.prepareStatement(sql);
    }
}
