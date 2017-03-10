/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import mensajes.Mensaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensajes.MensajeChatPrivado;
import mensajes.MensajeChatPublico;
import mensajes.MensajeLogin;
import mensajes.MensajeRegistro;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class ClienteChat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket clientSocket;
        int portNumber = 5555;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
        clientSocket = new Socket("127.0.0.1", portNumber);
        ois = new ObjectInputStream(clientSocket.getInputStream());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        
        
        MensajeLogin mensajeLogin = new MensajeLogin();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("beatriz");
        usuario.setPassword("123456");
        usuario.setEmail("alberfd@gmail.com");
        usuario.setCodUsuario(2);
        mensajeLogin.setUsuario(usuario);
        
        
        /*
        MensajeChatPrivado mensajePrivado = new MensajeChatPrivado();
        mensajePrivado.setMensaje("Mensaje privado");
        mensajePrivado.setUsuarioOrigen(usuario);
        mensajePrivado.setUsuarioDestino(usuario);
        */
        
        /*
        MensajeRegistro mensajeRegistro = new MensajeRegistro();
        mensajeRegistro.setEmail("auri@gmail.com");
        mensajeRegistro.setLogin("auri");
        mensajeRegistro.setPassword("123456");
        mensajeRegistro.setRepeatPassword("123456");
        */
        
        SirvienteCliente sirvienteCliente = new SirvienteCliente(clientSocket, oos, ois, null);
        
        new Thread(sirvienteCliente).start();
        
        
        oos.writeObject(mensajeLogin);
        /*
        oos.writeObject(mensajePrivado);
        MensajeChatPublico mensajeChat;
        for(int i=0;i<5;i++){
            
           mensajeChat = new MensajeChatPublico("Hola" + i);
           mensajeChat.setLoginOrigen("Alberto");
           
           oos.writeObject(mensajeChat);
           
           
        }
        */
    
        /*
           MensajeRegistro mensaje = new MensajeRegistro();
           mensaje.setEmail("juan@gmail.com");
           mensaje.setLogin("Juan");
           mensaje.setPassword("SinPass");
           mensaje.setRepeatPassword("SinPass");
           oos.writeObject(mensaje);
           
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        
        //oos.writeObject(null);
        
        //clientSocket.close();
        //ois.close();
        //oos.close();
        
    }
    
}
