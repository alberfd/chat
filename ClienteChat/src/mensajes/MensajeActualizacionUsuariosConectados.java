/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import java.io.Serializable;
import modelo.EstadoConexion;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class MensajeActualizacionUsuariosConectados extends Mensaje implements Serializable {
    
    //el usuario que se conecta o desconecta
    Usuario usuario;
    EstadoConexion estadoConexion;
    
    public MensajeActualizacionUsuariosConectados(){
        
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoConexion getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(EstadoConexion estadoConexion) {
        this.estadoConexion = estadoConexion;
    }
    
    
    
}
