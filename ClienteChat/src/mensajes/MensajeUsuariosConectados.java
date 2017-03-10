/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import java.io.Serializable;
import java.util.ArrayList;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class MensajeUsuariosConectados extends Mensaje implements Serializable {
    
    ArrayList<Usuario> usuarios;
    
    public MensajeUsuariosConectados(){
        
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
     
}
