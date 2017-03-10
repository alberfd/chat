/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import java.util.Observable;
import javafx.util.Callback;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class CellUsuario {
    Usuario usuario;
    int numeroMensajes;
    
    public CellUsuario(){
        
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getNumeroMensajes() {
        return numeroMensajes;
    }

    public void setNumeroMensajes(int numeroMensajes) {
        this.numeroMensajes = numeroMensajes;
    }
    
    
    
    
    
}
