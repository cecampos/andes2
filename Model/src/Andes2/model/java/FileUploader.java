package Andes2.model.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.StringTokenizer;

public class FileUploader {

	/*Clase que Carga los archivos de input en la base de datos
         * Dado que cada archivo subido requiere aplicar logica de negocio disinta,
         * Se tiene 1 metodo por cada caso
         * */
    
        Connection con;
	public FileUploader(){
            DbManager dbMan = new DbManager();
            con = dbMan.getDb("dev", "dev", "xe");
            }

        
        public void uploadFileToDb(String fileCase, Object[] dataPair){
            //dataPair es el par (inputStream oinput, String mes)
        
            InputStreamReader aux = new InputStreamReader((InputStream)dataPair[0]);
            BufferedReader reader = new BufferedReader(aux);
            
            //Router, cada archivo tiene su propio metodo que lo carga en la BD
            if(fileCase.equals("empleados")){
                uploadEmpleado(reader);
                }
            else if(fileCase.equals("vacaciones"))
                uploadVacacion(reader);
            else if(fileCase.equals("capacitaciones"))
                uploadCapacitacion(reader);
            else if(fileCase.equals("skills"))
                uploadSkills(reader);
            else if(fileCase.equals("demandaSkills"))
                uploadDemandaSkills(reader,(String)dataPair[1]);
            else if(fileCase.equals("grupos"))
                uploadGrupos(reader);
                


        }
        
        private PreparedStatement prepareInsertSQL(Connection con, String[] colNames, String tableName){
            /*Prepara una consulta SQL que insertara nuevos registros en la base de datos.
             * Usar esta consulta con datos que ya existen generara un erro SQL "duplicate key"*/
                String insertSQL = String.format("insert into %s (", tableName);
                for(int i=0;i<colNames.length;i++){
                        insertSQL = insertSQL+colNames[i]+",";
                }
                insertSQL = insertSQL.substring(0, insertSQL.length()-1)+")";
                insertSQL = insertSQL.concat(" values(");

                for(int i=0;i<colNames.length;i++){
                        insertSQL = insertSQL+"?,";
                }

                insertSQL = insertSQL.substring(0, insertSQL.length()-1)+")";
                PreparedStatement st= null;
                try {
                        st = con.prepareStatement(insertSQL);
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                return st;            
            }

	private PreparedStatement prepareInsertUpdate(Connection con,String[] colNames, String tableName) {
            /*Prepara una consulta de insert (si es que existe el registro) o update (si ya existe un
             * registro con esta primary key). Se asume que la primary key de cada tabla  es la primera columna
             * (si no, redistribuir los valores para que quede la id en el primer campo de la insercion
             * */
            String insertSQL = "MERGE INTO "+tableName+" a USING (SELECT";  
	    for(int i=0;i<colNames.length;i++){
                if(i!=0)
                    insertSQL=insertSQL+",";
                insertSQL = insertSQL+" ? "+colNames[i];
	    }
            insertSQL = insertSQL+" FROM dual) incoming";
            insertSQL = insertSQL+" ON (a."+colNames[0]+" = incoming."+colNames[0]+")";
            insertSQL = insertSQL + "WHEN MATCHED THEN UPDATE SET ";
            for(int i=1;i<colNames.length;i++){
                    if(i!=1)
                        insertSQL=insertSQL+",";
                insertSQL = insertSQL + " a."+colNames[i]+" = incoming."+colNames[i];
                }
            insertSQL = insertSQL +" WHEN NOT MATCHED THEN INSERT (";
            for(int i=0;i<colNames.length;i++){
                if(i!=0)
                    insertSQL = insertSQL+",";
                insertSQL = insertSQL +" a."+colNames[i];    
                }
            insertSQL = insertSQL+") VALUES (";
            for(int i=0;i<colNames.length;i++){
                if(i!=0)
                    insertSQL = insertSQL+",";
                insertSQL = insertSQL+" incoming."+colNames[i];
                }
            insertSQL = insertSQL+")";
            
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(insertSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return st;
        }

	private void saveRecord(PreparedStatement SQL,String[] colTypes, String[] dataRow) {
		/*Tipos de entrada de inputs:
		 * String
		 * int
		 * Date
		 * 
		 * */

		for(int i=0;i<colTypes.length;i++)
			colTypes[i]=colTypes[i].toLowerCase();


		try {
			//Dependiendo del caso, indicamos que tipo de datos setear
			for(int i=0; i<colTypes.length;i++){
				if(colTypes[i].equals("int"))
					SQL.setInt(i+1, Integer.parseInt(dataRow[i]));
				else if(colTypes[i].equals("date")){
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                        java.util.Date aux=formatter.parse(dataRow[i]);
					java.sql.Date sqlDate = new java.sql.Date(aux.getTime());
					SQL.setDate(i+1, sqlDate);
				}
                                else{
			            SQL.setString(i+1, dataRow[i]);
                                }
			}
			SQL.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
                        System.out.println(e);
		}

	}



    private void uploadEmpleado(BufferedReader reader) {
        String actualLine;
        StringTokenizer separator = null;
        String tableName="empleados";
        String[] colNames ={"EMPL_RUT","EMPL_NOMBRE"};
        String[] colTypes = {"String","String"};
        
        PreparedStatement SQL = prepareInsertUpdate(con,colNames,tableName);
        
        //Datos para CONTRATO_EMPLEADOS
        String table2 = "contrato_empleados";
        String[] colNames2 = {"EMPL_RUT","RGTR_SEQ_CDG","CRGO_ID","CNTR_MES","CNTR_VERSION"};
        String[] colTypes2 = {"String","int","String","date","int"};
        PreparedStatement SQL2 = prepareInsertUpdate(con,colNames2,table2);

        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                String[] rowTokens = actualLine.split(";");
                String[] dataRow = {rowTokens[0],rowTokens[2]};
                String month = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                String[] dataRow2 = {rowTokens[0],rowTokens[3],rowTokens[1],month,"0"};
                saveRecord(SQL,colTypes,dataRow);
                saveRecord(SQL2,colTypes2,dataRow2);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("empleados");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }       
    }

    private void uploadCapacity(BufferedReader reader){
        String actualLine;
        StringTokenizer separator = null;
        String tableName="capacity_hora";
        String[] colNames ={"CAPH_FECHA","CRGO_ID","CAPH_HORA","CAPH_REQUERIMIENTO","CAPH_VERSION"};
        String[] colTypes = {"date","String","String","int","int"};
        
        PreparedStatement SQL = prepareInsertUpdate(con,colNames,tableName);

        String[] dataRow = null;
        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                separator = new StringTokenizer(actualLine,";");
                dataRow = new String[5];
                for(int i=0;i<dataRow.length;i++){
                    try{
                        dataRow[i] = separator.nextToken();
                    }
                    catch(Exception e){
                        //NoSuchElementException => dato vacio
                        dataRow[i] = "";
                    }
                }
                dataRow[4] = "0";
                saveRecord(SQL,colTypes,dataRow);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("capacity");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }           
        
    }
    
    private void deleteAllFromMonth(String tableName, String monthColumn, String month){
        /*Realiza un delete de todas las filas que hallan sido ingresadas en el mes month,
         * usando monthColumn como criterio de filtrado
         * */
        String sqlQuery = "delete from "+tableName+" where extract(month from "+monthColumn+") = '"+month+"'";
        
        PreparedStatement st;


        try {
            st = con.prepareStatement(sqlQuery);
            boolean b = st.execute();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    private void uploadDemandaSkills(BufferedReader reader,String mes){
        String actualLine;
        StringTokenizer separator = null;
        String tableName="demanda_skill";
        String[] colNames ={"SKLL_ID","TURN_NOMBRE","DDSK_FECHA","DDSK_REQUERIMIENTO","DDSK_VERSION"};
        String[] colTypes = {"String","String","Date","int","int"};
        
        /*TODO: Borrar todos los registros de la base de datos de este mes*/
        deleteAllFromMonth(tableName,"DDSK_FECHA",mes);
        
        
        PreparedStatement SQL = prepareInsertSQL(con,colNames,tableName);

        String[] dataRow = null;
        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                separator = new StringTokenizer(actualLine,";");
                dataRow = new String[5];
                for(int i=0;i<dataRow.length;i++){
                    try{
                        dataRow[i] = separator.nextToken();
                    }
                    catch(Exception e){
                        //NoSuchElementException => dato vacio
                        dataRow[i] = "";
                    }
                }
                dataRow[4] = "0";
                saveRecord(SQL,colTypes,dataRow);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("DemandaSkills");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }           
        
    }    
    
    
    private void uploadSkills(BufferedReader reader){
        /*TODO: Carga en empleados skills,
         * */
        
        
        }
    
    private void uploadGrupos(BufferedReader reader){
        /*TODO : De este archivo sale CoAsignacion y Grupos
         * Avergiaur como realizarlo :S
         * */
        String actualLine;
        StringTokenizer separator = null;
        

        
        String tableName="coasignacion";
        String[] colNames ={"EMPL_RUT","GRPO_NOMBRE","COAG_FECHA","COAG_VERSION"};
        String[] colTypes = {"String","String","Date","int"};
        
        PreparedStatement SQL = prepareInsertUpdate(con,colNames,tableName);
        String[] dataRow = null;
        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                //System.out.println(actualLine);
                separator = new StringTokenizer(actualLine,";");
                dataRow = new String[4];
                for(int i=0;i<2;i++){
                    try{
                        dataRow[i] = separator.nextToken();
                    }
                    catch(Exception e){
                        //NoSuchElementException => dato vacio
                        dataRow[i] = "";
                    }
                }
                SimpleDateFormat formatAux = new SimpleDateFormat("dd/MM/yyyy");
                dataRow[2]=formatAux.format(Calendar.getInstance().getTime());
                dataRow[3]="0"; 
                    
                saveRecord(SQL,colTypes,dataRow);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("vacaciones");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }   
                
        
        }
    
    private void uploadTurnosNoPermitidos(BufferedReader reader){
        /*TODO : Archivo genera Restriccion asignacion, requiere 
         * logica compleja : iterador entre fechas, condicional
         * entre dias
         */
        }
    
    private void uploadFijarAsignacion(BufferedReader reader){
        /* TODO:  que es Fjag dia y mes
         * Logica compleja (iterador entre fechas
         * */
        }
    
    private void uploadCapacityTurno(BufferedReader reader){
        String actualLine;
        StringTokenizer separator = null;
        String tableName="capacity_turno";
        String[] colNames ={"CAPH_FECHA","CRGO_ID","TURN_NOMBRE","CAPT_REQUERIMIENTO","CAPT_VERSION"};
        String[] colTypes = {"date","String","String","int","int"};
        
        PreparedStatement SQL = prepareInsertUpdate(con,colNames,tableName);

        String[] dataRow = null;
        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                separator = new StringTokenizer(actualLine,";");
                dataRow = new String[5];
                for(int i=0;i<dataRow.length;i++){
                    try{
                        dataRow[i] = separator.nextToken();
                    }
                    catch(Exception e){
                        //NoSuchElementException => dato vacio
                        dataRow[i] = "";
                    }
                }
                dataRow[4] = "0";
                saveRecord(SQL,colTypes,dataRow);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("capacityTurno");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }           
                  
    }

    private void uploadVacacion(BufferedReader reader) {
        String actualLine;
        StringTokenizer separator = null;
        

        
        String tableName="vacaciones";
        String[] colNames ={"VCCN_ID","EMPL_RUT","VCCN_FECHA_INICIO","VCCN_FECHA_TERMINO"};
        String[] colTypes = {"int","String","Date","Date"};
 
        /*TODO: Borrar todos los registros de la base de datos correspondientes al mes a ser actualizado
         * 
         * */
        
        PreparedStatement SQL = prepareInsertUpdate(con,colNames,tableName);
        String[] dataRow = null;
        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                System.out.println(actualLine);
                separator = new StringTokenizer(actualLine,";");
                dataRow = new String[separator.countTokens()];
                for(int i=0;i<dataRow.length;i++){
                    try{
                        dataRow[i] = separator.nextToken();
                    }
                    catch(Exception e){
                        //NoSuchElementException => dato vacio
                        dataRow[i] = "";
                    }
                }
                saveRecord(SQL,colTypes,dataRow);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("vacaciones");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }   
        
    }

    private void uploadCapacitacion(BufferedReader reader) {
        String actualLine;
        StringTokenizer separator = null;
        String tableName="capacitaciones";
        String[] colNames ={"CAPT_ID","EMPL_RUT","CAPT_FECHA_INICIO","CAPT_FECHA_TERMINO","CAPT_HORA_INICIO","CAPT_HORA_TERMINO"};
        String[] colTypes = {"int","String","Date","Date","String","String"};
        
        PreparedStatement SQL = prepareInsertUpdate(con,colNames,tableName);
        //Datos de la tabla
        String[] dataRow = null;
        try {
            while ((actualLine = reader.readLine()) != null)   {
                //Saltarse Lineas de comentarios:
                if(actualLine.startsWith("#") || actualLine.startsWith(";") || actualLine.startsWith("﻿#"))
                        continue;
                separator = new StringTokenizer(actualLine,";");
                dataRow = new String[separator.countTokens()];
                for(int i=0;i<dataRow.length;i++){
                    try{
                        dataRow[i] = separator.nextToken();
                        //System.out.print(dataRow[i]+"\t");
                    }
                    catch(Exception e){
                        //NoSuchElementException => dato vacio
                        dataRow[i] = "";
                    }
                }
                saveRecord(SQL,colTypes,dataRow);
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error leyendo el archivo de input");
            e.printStackTrace();
        }
        try {
                reader.close();
                newUpldFileRecord("capacitaciones");
                //con.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }   
    }

    private void newUpldFileRecord(String fileName) {
        //Inserta en la tabla archivos subidos un nuevo registro.
        String tableName="archivos_subidos";
        String[] colNames ={"ARCH_NOMBRE","ARCH_FECHA_SUBIDA"};
        String[] colTypes = {"String","Date"};
        PreparedStatement SQL = prepareInsertSQL(con,colNames,tableName);
        
        SimpleDateFormat formatter = new SimpleDateFormat("_MM_yyyy");
        SimpleDateFormat aux2 = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date actualDate = Calendar.getInstance().getTime();
        String dateString =formatter.format(actualDate);
        String[] dataRow = {fileName+dateString+".csv",aux2.format(actualDate)};
        saveRecord(SQL,colTypes,dataRow);
    }
}

