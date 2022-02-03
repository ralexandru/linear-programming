
package simplexfx;
import com.mysql.cj.jdbc.*;
import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection conn;
    private Statement statement;
    
    public Connection openConnection() throws SQLException{
        if(conn==null){
            String url="jdbc:mysql://localhost/";
            String dbName = "simplexalg";
            String driver = "com.mysql.cj.jdbc.Driver";
            String userName = "root";
            String password = "root";
            try{
                Class.forName(driver);
                this.conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
                System.out.println("Conexiune realizata cu succes!");
            }catch(ClassNotFoundException | SQLException sqle){
                System.out.println("Eroare de conexiune" + sqle);
            }
        }
        return conn;
    }
    public boolean verificaCont(String user, String parola) throws SQLException{
        boolean conectare = false;
        int contor = 0;
        String query = "SELECT * FROM users WHERE user='"+user+"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            String salt = rs.getString(4);
            String key  = rs.getString(3);
            System.out.println("Salt is : " + salt);
            if(EncryptPassword.verifyPassword(parola, key, salt)){
                System.out.println("Parolele coincid");
                principal.uId = Integer.parseInt(rs.getString(1));
                contor++;             
            }
        }
        /*
        String query = "SELECT * FROM users WHERE user='"+user+"' AND password='"+parola+"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            contor++;
            principal.uId = Integer.parseInt(rs.getString(1));
            System.out.println(principal.uId);
        }*/
        if(contor == 1)
            conectare = true;
        System.out.println("Contor: " + contor);
        return conectare;    
    }
    
    public boolean CreeazaCont(String user, String parola, String nume, String email) throws SQLException{
        boolean conectare = true;
        int contor = 0;
        String query = "SELECT * FROM users WHERE user='"+user+"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            contor++;
        }
        if(contor == 1){
            conectare = false;
        }
        if(conectare == true){
            EncryptPassword pw = new EncryptPassword();
            String salt = EncryptPassword.generateSalt(512).get();
            String key = EncryptPassword.hashPassword(parola, salt).get();
            System.out.println("Key is " + key);
            String query2 = "INSERT INTO users(user,password,salt,name,email) VALUES('"+user+"','"+key+"','"+salt+"','"+nume+"','"+email+"');";
            Statement st2 = conn.createStatement();
            st2.executeUpdate(query2);
            System.out.println("Cont creeat cu succes!");
        }
        return conectare;
    }
    
    public void ExecutaQuery(String query) throws SQLException{
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        System.out.println("Query realizat cu succes");
    }
    
    public List<String> istoric(int userId) throws SQLException{
        List<String> istoricS = new ArrayList<>();
        String query = "SELECT * FROM istoricppl WHERE userId='"+userId+"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            istoricS.add("Nr restrictii: " + rs.getString(3) + " | Nr. variabile: " + rs.getString(4) + "| f. obiectiv: " + rs.getString(5) + " | Baza finala: " + rs.getString(9) + " | Solutie optima: " + rs.getString(10));
        }
        return istoricS;
    }
}
