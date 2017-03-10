/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class DataMapperUsuario {
    
    Connection conexion;
    
    
    public DataMapperUsuario(){
        try
        {
            // Se registra el Driver de MySQL
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            
            // Se obtiene una conexión con la base de datos. Hay que
            // cambiar el usuario "root" y la clave "la_clave" por las
            // adecuadas a la base de datos que estemos usando.
            conexion = DriverManager.getConnection (
                "jdbc:mysql://localhost/chat","root", "");
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean loginCorrecto(Usuario usuario) throws SQLException{
        // Se crea un Statement, para realizar la consulta
        Statement s = conexion.createStatement();
            
        // Se realiza la consulta. Los resultados se guardan en el 
        // ResultSet rs
        ResultSet rs = s.executeQuery ("select * from usuarios where NOMBRE_USUARIO = '" + usuario.getNombreUsuario() + 
                "' AND PASSWORD = '" + usuario.getPassword() + "' and BOL_ACTIVO = 1");
        
        // Se recorre el ResultSet, mostrando por pantalla los resultados.
        while (rs.next())
        {
            return true;
        }
        
        return false;
    }
    
    public boolean existeUsuario(Usuario usuario) throws SQLException{
        // Se crea un Statement, para realizar la consulta
        Statement s = conexion.createStatement();
            
        // Se realiza la consulta. Los resultados se guardan en el 
        // ResultSet rs
        ResultSet rs = s.executeQuery ("select * from usuarios where nombre_usuario = '" + usuario.getNombreUsuario() + 
                "' or email = '" + usuario.getEmail() + "'");
        
        // Se recorre el ResultSet, mostrando por pantalla los resultados.
        while (rs.next())
        {
            return true;
        }
        
        return false;
    }
    
    public boolean insertaUsuario(Usuario usuario) throws SQLException{
        // Se crea un Statement, para realizar la consulta
        PreparedStatement s = conexion.prepareStatement("INSERT INTO USUARIOS (NOMBRE_USUARIO, PASSWORD, EMAIL, BOL_ACTIVO,"
                + "TXT_HASH_CONFIRMACION) VALUES (?,?,?,?,?)");
        
        s.setString(1, usuario.getNombreUsuario());
        s.setString(2, usuario.getPassword());
        s.setString(3, usuario.getEmail());
        s.setBoolean(4, false);
        s.setString(5, usuario.getHashConfirmacion());
        
        if(s.executeUpdate() > 0)
            return true;
        
        return false;
    }
    
    public Usuario getUsuarioByNombreUsuario(String nombreUsuario) throws SQLException{
        Usuario usuario = null;
        
        // Se crea un Statement, para realizar la consulta
        PreparedStatement s = conexion.prepareStatement("SELECT CODIGO_USUARIO, NOMBRE_USUARIO, PASSWORD, EMAIL FROM " + 
                "USUARIOS WHERE NOMBRE_USUARIO = ?");
        
        s.setString(1, nombreUsuario);
        
        ResultSet rs = s.executeQuery();
        
        while (rs.next()){
            usuario = new Usuario();
            usuario.setCodUsuario(rs.getInt("CODIGO_USUARIO"));
            usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
            usuario.setPassword(rs.getString("PASSWORD"));
            usuario.setEmail(rs.getString("EMAIL"));
            
        }
        
        return usuario;
        
        
    }
}
