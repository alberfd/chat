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
    
    ObjectOutputStream oos;
    ObjectInputStream ois;
    ControladorUsuario controladorUsuario;
    ControladorChat controladorChat;
    
    
    public Sirviente(Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois, 
            ControladorUsuario controladorUsuario, ControladorChat controladorChat) throws IOException{
        this.clientSocket = clientSocket;
        
        this.oos = oos;
        this.ois = ois;
        
        this.controladorUsuario = controladorUsuario;
        this.controladorChat = controladorChat;
        
        
    }
    
    public Socket getClientSocket() {
        return clientSocket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ControladorUsuario getControladorUsuario() {
        return controladorUsuario;
    }

    public void setControladorUsuario(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;
    }

    public ControladorChat getControladorChat() {
        return controladorChat;
    }

    public void setControladorChat(ControladorChat controladorChat) {
        this.controladorChat = controladorChat;
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
