package Andes2.model.java;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
    /*Clase para manejar la base de datos de 2 formas:
     * 1) Iniciar con los datos de conexion, en este caso la clase guarda 
     *    un objeto Connection y provee el metodo getTable() que retorna el resultado de un Query SQL
     *    
     * 2) Usar el metodo getDb para obtener un objeto Connection, que luego se puede utilizar
     * para operar con libertad sobre la base de datos
     */
    private Connection actualCon;
    public DbManager() {
        super();
    }
    
    public DbManager(String username,String pass,String dbName){
        try{
                Class.forName("oracle.jdbc.driver.OracleDriver");
                
        }
        catch(ClassNotFoundException e){
                System.out.println("Where is your Oracle JDBC Driver?");
                e.printStackTrace();
                actualCon = null;
        }
        
        try {
                actualCon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:"+dbName,
                                username,pass);
        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                actualCon = null;
        }       
    }
    
    public Connection getDb(String username, String pass, String dbName){
                    try{
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            
                    }
                    catch(ClassNotFoundException e){
                            System.out.println("Where is your Oracle JDBC Driver?");
                            e.printStackTrace();
                            return null;
                    }
                    Connection con=null;
                    
                    try {
                            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:"+dbName,
                                            username,pass);
                            //probar coneccion
                            
                            if (con != null) {
                                    System.out.println("You made it, take control your database now!");
                                    return con;
                            } 
                             else {
                                    System.out.println("Failed to make connection!");
                                    return null;
                            }
                            
                    } catch (SQLException e) {
                            System.out.println("Connection Failed! Check output console");
                            e.printStackTrace();
                            return null;
                    }
            }
    
    public ResultSet getTable(String sqlCode){
        /*Devuelve un resultSet de una tabla, de acuerdo a la instruccion sql pasada como parametro*/
        Statement query;
        ResultSet result = null;

        try {
            query = actualCon.createStatement();
            result = query.executeQuery(sqlCode);
        } catch (SQLException e) {
            e.printStackTrace();
            result=null;
        }
        return result;
        
    }
}