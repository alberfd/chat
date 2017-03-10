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
import java.util.logging.Level;
import java.util.logging.Logger;
import mensajes.Mensaje;
import mensajes.MensajeChatPrivado;
import mensajes.MensajeChatPublico;
import mensajes.MensajeLogin;
import mensajes.MensajeRegistro;
import mensajes.MensajeUsuariosConectados;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class SirvienteLector extends Sirviente implements Runnable {
    
    SirvienteEscritor sirvienteEscritor;
    
    public SirvienteLector(ServidorChat servidorChat, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException{
        super(servidorChat, clientSocket, oos, ois);
    }

    @Override
    public void run() {
       Mensaje mensaje;
        try {
            while((mensaje = (Mensaje)ois.readObject()) != null){
                //Solo atendemos a los mensajes de registro o login 
                //para los usuarios que no esten logeados
                if(!(mensaje instanceof MensajeRegistro) && 
                        !(mensaje instanceof MensajeLogin) &&
                            usuario_ == null){
                    continue;
                }
                
                if(mensaje instanceof MensajeRegistro){
                    controladorUsuario.registro((MensajeRegistro)mensaje);
                    
                }else if(mensaje instanceof MensajeChatPublico){
                    controladorChat.mensajeChatPublico((MensajeChatPublico) mensaje);
                }else if(mensaje instanceof MensajeLogin){
                    controladorUsuario.login((MensajeLogin)mensaje);
                }else if(mensaje instanceof MensajeChatPrivado){
                    controladorChat.mensajeChatPrivado((MensajeChatPrivado) mensaje);
                }
            }
            desconectar();
            servidorChat.eliminaCliente(this.usuario_);
            
            System.out.println("Hemos salido del sirviente");
                      
        } catch (IOException ex) {
            Logger.getLogger(Sirviente.class.getName()).log(Level.SEVERE, null, ex);
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sirviente.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
           desconectar();
           servidorChat.eliminaCliente(this.usuario_);
           controladorUsuario.usuarioDesconectado(usuario_);
        }
    }

    public SirvienteEscritor getSirvienteEscritor() {
        return sirvienteEscritor;
    }

    public void setSirvienteEscritor(SirvienteEscritor sirvienteEscritor) {
        this.sirvienteEscritor = sirvienteEscritor;
    }
    
    
    
    


    
}
