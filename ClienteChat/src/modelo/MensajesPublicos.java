/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import mensajes.Mensaje;
import mensajes.MensajeChatPublico;

/**
 *
 * @author Alberto
 */
public class MensajesPublicos extends Observable {
    Queue<MensajeChatPublico> mensajesPublicos;
    
    public MensajesPublicos(){
        this.mensajesPublicos = new LinkedList<>();
    }

    public Queue<MensajeChatPublico> getMensajesPublicos() {
        return mensajesPublicos;
    }

    public void setMensajesPublicos(Queue mensajesPublicos) {
        this.mensajesPublicos = mensajesPublicos;
    }
    
    public void nuevoMensaje(MensajeChatPublico mensaje){
        mensajesPublicos.add(mensaje);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    
}
