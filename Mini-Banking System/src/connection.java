
import java.sql.*;

public class connection {
    static Connection con;
    public static Object st;
    public static Connection getConnection(){
        try{
            String mysqlJDBCDriver  = "com.mysql.cj.jdbc.Driver"; //jdbc driver
            String url="jdbc:mysql://sql12.freesqldatabase.com:3306/sql12669806";
            String username="sql12669806";
            String password="D5l6TBgzFp";
            Class.forName(mysqlJDBCDriver);
            con=DriverManager.getConnection(url, username, password);
            System.out.println(
            "Connection Established successfully");
            // Statement st = con.createStatement();
            
            System.out.println("Connection established successfull");
            
        }
        catch(Exception e){
            System.out.print(e + "Connection failed!!!");
        }
        return con;
    }
}
