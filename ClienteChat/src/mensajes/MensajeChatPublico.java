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
public class MensajeChatPublico extends Mensaje implements Serializable{

    Usuario usuarioOrigen;
    String mensaje;

    public MensajeChatPublico(String mensaje) {
        
        this.mensaje = mensaje;

    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuarioOrigen() {
        return usuarioOrigen;
    }

    public void setUsuarioOrigen(Usuario usuarioOrigen) {
        this.usuarioOrigen = usuarioOrigen;
    }

    
    
    
    

}
