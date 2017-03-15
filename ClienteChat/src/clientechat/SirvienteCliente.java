/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import controladores.ControladorChat;
import controladores.ControladorUsuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;

import mensajes.Mensaje;
import mensajes.MensajeActualizacionUsuariosConectados;
import mensajes.MensajeChatPrivado;
import mensajes.MensajeChatPublico;
import mensajes.MensajeColorChat;
import mensajes.MensajeLogin;
import mensajes.MensajeLoginRespuesta;
import mensajes.MensajeRegistro;
import mensajes.MensajeRegistroRespuesta;
import mensajes.MensajeUsuariosConectados;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class SirvienteCliente extends Observable implements Runnable {

    Socket clientSocket;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    
    ControladorChat controladorChat;
    ControladorUsuario controladorUsuario;
    
    FXApplicationMain app;
    
    public SirvienteCliente(Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois, FXApplicationMain app) throws IOException{
        this.clientSocket = clientSocket;
        this.oos = oos;
        this.ois = ois;
        
        
        this.app = app;
    }
    
    @Override
    public void run() {
        Mensaje mensaje;
        
        try {
            while((mensaje = (Mensaje)ois.readObject()) != null){
                
                if(mensaje instanceof MensajeChatPublico){
                    MensajeChatPublico mensajeChatPublico = (MensajeChatPublico) mensaje;
                    controladorChat.procesaMensajePublicoEntrada(mensajeChatPublico);
                }else if(mensaje instanceof MensajeLoginRespuesta){
                    MensajeLoginRespuesta mensajeLoginRespuesta = (MensajeLoginRespuesta)mensaje;
                    
                    controladorUsuario.procesaLoginRespuesta(mensajeLoginRespuesta);
                    
                }else if(mensaje instanceof MensajeRegistroRespuesta){
                    MensajeRegistroRespuesta mensajeRegistroRespuesta = (MensajeRegistroRespuesta) mensaje;
                    
                    controladorUsuario.procesaRegistroRespuesta(mensajeRegistroRespuesta);
  
                }else if(mensaje instanceof MensajeUsuariosConectados){
                    MensajeUsuariosConectados mensajeUsuariosConectados = (MensajeUsuariosConectados) mensaje;
                    
                    controladorChat.procesaMensajeUsuariosConectados(mensajeUsuariosConectados);
                    
                    
                }else if(mensaje instanceof MensajeChatPrivado){
                    MensajeChatPrivado mensajechatPrivado = (MensajeChatPrivado) mensaje;
                    
                    controladorChat.procesaMensajePrivadoEntrada(mensajechatPrivado);
                    
                }else if(mensaje instanceof MensajeActualizacionUsuariosConectados){
                    MensajeActualizacionUsuariosConectados mensajeActualizacionUsuariosConectados =  
                            (MensajeActualizacionUsuariosConectados) mensaje;
                    controladorChat.procesaMensajeActualizacionUsuariosConectados(mensajeActualizacionUsuariosConectados);
                            
                }else if(mensaje instanceof MensajeColorChat){
                    MensajeColorChat mensajeColorChat = (MensajeColorChat) mensaje;
                    controladorChat.procesaMensajeColorChat(mensajeColorChat);
                }
            }
            desconectar();
            
            System.out.println("Hemos salido del sirviente del cliente");
                      
        } catch (IOException ex) {
            Logger.getLogger(SirvienteCliente.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SirvienteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            desconectar();
        }
    }

    public ControladorChat getControladorChat() {
        return controladorChat;
    }

    public void setControladorChat(ControladorChat controladorChat) {
        this.controladorChat = controladorChat;
    }

    public ControladorUsuario getControladorUsuario() {
        return controladorUsuario;
    }

    public void setControladorUsuario(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;
    }
    

    public void desconectar(){
        try {
            this.clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SirvienteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}
