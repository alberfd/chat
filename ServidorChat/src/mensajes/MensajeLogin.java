/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import java.io.Serializable;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class MensajeLogin extends Mensaje implements Serializable{
    
    Usuario usuario;
    
    public MensajeLogin(){
        
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
