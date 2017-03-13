/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import modelo.Usuario;
import servidorchat.ServidorChat;
import servidorchat.Sirviente;
import servidorchat.SirvienteEscritor;
import servidorchat.SirvienteLector;

/**
 *
 * @author Alberto
 */
public abstract class Controlador {
    
    ServidorChat servidorChat;
    
    Usuario usuario_;
    
    SirvienteEscritor sirvienteEscritor;
    SirvienteLector sirvienteLector;
    
    
    
    public Controlador(ServidorChat servidorChat, SirvienteEscritor sirvienteEscritor, SirvienteLector sirvienteLector){
        
        this.servidorChat = servidorChat;
        this.sirvienteEscritor = sirvienteEscritor;
        this.sirvienteLector = sirvienteLector;
  
    }

    public ServidorChat getServidorChat() {
        return servidorChat;
    }

    public void setServidorChat(ServidorChat servidorChat) {
        this.servidorChat = servidorChat;
    }

    public Usuario getUsuario_() {
        return usuario_;
    }

    public void setUsuario_(Usuario usuario_) {
        this.usuario_ = usuario_;
    }
    
    
}
