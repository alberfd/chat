/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import controladores.ControladorChat;
import controladores.ControladorUsuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensajes.Mensaje;
import mensajes.MensajeChatPublico;
import mensajes.MensajeLogin;
import mensajes.MensajeRegistro;
import mensajes.MensajeUsuariosConectados;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public abstract class Sirviente implements Runnable {
    
    Socket clientSocket;
    ServidorChat servidorChat;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    ControladorUsuario controladorUsuario;
    ControladorChat controladorChat;
    Usuario usuario_;
    
    public Sirviente(ServidorChat servidorChat, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException{
        this.servidorChat = servidorChat;
        this.clientSocket = clientSocket;
        
        this.oos = oos;
        this.ois = ois;
        
        controladorUsuario = new ControladorUsuario(this);
        controladorChat = new ControladorChat(this);
    }
    
    public Socket getClientSocket() {
        return clientSocket;
    }

    public ServidorChat getServidorChat() {
        return servidorChat;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public Usuario getUsuario_() {
        return usuario_;
    }

    public void setUsuario_(Usuario usuario_) {
        this.usuario_ = usuario_;
    }

    public void desconectar(){
        try {
            this.clientSocket.close();
            this.oos.close();
            this.ois.close();
        } catch (IOException ex) {
            Logger.getLogger(Sirviente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
