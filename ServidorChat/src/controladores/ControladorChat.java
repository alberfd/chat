/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensajes.MensajeChatPrivado;
import mensajes.MensajeChatPublico;
import modelo.Usuario;
import servidorchat.ServidorChat;
import servidorchat.Sirviente;
import servidorchat.SirvienteEscritor;
import servidorchat.SirvienteLector;

/**
 *
 * @author Alberto
 */
public class ControladorChat extends Controlador{
    
    ControladorUsuario controladorUsuario;
    
    public ControladorChat(ServidorChat servidor,
            SirvienteEscritor sirvienteEscritor, SirvienteLector sirvienteLector){
        super(servidor, sirvienteEscritor, sirvienteLector);
            
    }
    
    public void mensajeChatPublico(MensajeChatPublico mensaje){
        
        Usuario usuarioOrigen = mensaje.getUsuarioOrigen();
        
        System.out.println("Estamos aqui lo cual indica que no esta del todo mal");
        //enviamos el mensaje publico a todos menos al que envio el mensaje
        for(Map.Entry<Usuario, SirvienteEscritor> entry : this.servidorChat.getSirvientesEscritor().entrySet()){
            if(!entry.getKey().equals(usuarioOrigen)){
                System.out.println("Estamos enviando");
                entry.getValue().insertaMensaje(mensaje);
            }
        }  
    }
    
    public void mensajeChatPrivado(MensajeChatPrivado mensaje){
        Usuario usuarioDestino = mensaje.getUsuarioDestino();
       
        //obtenemos el sirviente escritor que corresponde al usuario destino en el servidor central
        //y le enviamos el mensaje
        SirvienteEscritor sirvienteEscritor = servidorChat.getSirvientesEscritor().get(usuarioDestino);
        sirvienteEscritor.insertaMensaje(mensaje);

    }
    
    private Color getColorUsuario(Usuario usuario){
        return servidorChat.getColoresAsignados().get(usuario);
    }

    public ControladorUsuario getControladorUsuario() {
        return controladorUsuario;
    }

    public void setControladorUsuario(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;
    }
    
    
    
}
