/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import mensajes.MensajeChatPrivado;

/**
 *
 * @author Alberto
 */
public class MensajesPrivados extends Observable{
    private HashMap<Usuario, Queue<MensajeChatPrivado>> mensajesPrivados;
    
    public MensajesPrivados(){
        this.mensajesPrivados = new HashMap<>();
    }

    public HashMap<Usuario, Queue<MensajeChatPrivado>> getMensajesPrivados() {
        return mensajesPrivados;
    }

    public void setMensajesPrivados(HashMap<Usuario, Queue<MensajeChatPrivado>> mensajesPrivados) {
        this.mensajesPrivados = mensajesPrivados;
    }
    
    public boolean existeUsuarioOrigen(Usuario usuario){
        if(mensajesPrivados.containsKey(usuario))
            return true;
        return false;
    }
    
    public void nuevoMensaje(MensajeChatPrivado mensaje){
        
        Usuario usuarioOrigen = mensaje.getUsuarioOrigen();
        if(existeUsuarioOrigen(usuarioOrigen)){
            mensajesPrivados.get(usuarioOrigen).add(mensaje);
        }else{
            LinkedList colaMensajes = new LinkedList();
            colaMensajes.add(mensaje);
            mensajesPrivados.put(usuarioOrigen, colaMensajes);
        }
        
        this.setChanged();
        this.notifyObservers(mensaje);
    }
    
    
    
}


