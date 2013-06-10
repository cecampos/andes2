package Andes2.model.java;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public class CheckLastUpload {
    
    Connection con;
    public CheckLastUpload() {
        super();
        //CAMBIAR SID si ocurren errores de null pointer
        con = new DbManager().getDb("dev", "dev", "xe");
    }
    
    public Date check(String fileName){
        Statement st;
        String name = fileName.substring(0, fileName.indexOf('_'));
        String SQL = "select * from archivos_subidos " + 
                     "where arch_nombre like '"+name+"%'" + 
                     "order by arch_fecha_subida desc";
        ResultSet result;
        try {
            st = con.createStatement();
            result = st.executeQuery(SQL);
            if(result!=null){
                if(result.next())
                    return new Date(result.getDate(3).getTime());
                }
        } catch (SQLException e) {
            result = null;
        }
        return null;
        }

}
