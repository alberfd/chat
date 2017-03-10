/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import java.io.Serializable;
import modelo.TipoRespuestaLogin;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class MensajeLoginRespuesta extends Mensaje implements Serializable {
    TipoRespuestaLogin tipoRespuestaLogin;
    Usuario usuario;
    
    public MensajeLoginRespuesta(){
        
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoRespuestaLogin getTipoRespuestaLogin() {
        return tipoRespuestaLogin;
    }

    public void setTipoRespuestaLogin(TipoRespuestaLogin tipoRespuestaLogin) {
        this.tipoRespuestaLogin = tipoRespuestaLogin;
    }
    
    
}
