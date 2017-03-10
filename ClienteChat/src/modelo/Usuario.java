/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author Alberto
 */
public class Usuario implements Serializable {
    
    int codUsuario;
    String nombreUsuario;
    String password;
    String email;
    boolean activo;
    String hashConfirmacion;
    
    public Usuario(){
        
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getHashConfirmacion() {
        return hashConfirmacion;
    }

    public void setHashConfirmacion(String hashConfirmacion) {
        this.hashConfirmacion = hashConfirmacion;
    }
    
    @Override
    public int hashCode(){
        return codUsuario * nombreUsuario.length();
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Usuario))
            return false;
        
        if( ((Usuario)o ).codUsuario == 0)
            return false;
        
        if( ((Usuario)o ).codUsuario == codUsuario)
            return true;
        
        return false;
    }
    
    @Override
    public String toString(){
        return this.nombreUsuario; 
    }
    
    
    
}
