package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionUtil {
   
    private static String url="jdbc:mysql://localhost:3306/appdb";
    private static String USER="root";
     private static String password="Nivi@13";
     public static Connection getConnection(){
     try{

        return DriverManager.getConnection(url,USER,password);
     
    }
    catch(Exception e){
        e.printStackTrace();
    }
     return null;
}
}
