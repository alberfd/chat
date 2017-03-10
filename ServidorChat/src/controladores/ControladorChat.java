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
import servidorchat.Sirviente;
import servidorchat.SirvienteEscritor;

/**
 *
 * @author Alberto
 */
public class ControladorChat extends Controlador{
    
    
    public ControladorChat(Sirviente sirviente){
        super(sirviente);
            
    }
    
    public void mensajeChatPublico(MensajeChatPublico mensaje){
        Color color = getColorUsuario(mensaje.getUsuarioOrigen());
        mensaje.setColorNombreUsuario(color);
        for(SirvienteEscritor sirvienteEscritor : sirviente.getServidorChat().getSirvientesEscritor().values()){
            sirvienteEscritor.insertaMensaje(mensaje);
        }  
    }
    
    public void mensajeChatPrivado(MensajeChatPrivado mensaje){
        Usuario usuarioDestino = mensaje.getUsuarioDestino();
        Usuario usuarioOrigen = mensaje.getUsuarioOrigen();
        Color color = getColorUsuario(mensaje.getUsuarioOrigen());
        if(color == null)
            System.out.print("el color es nulo");
        mensaje.setColorNombreUsuario(color);
        
        //obtenemos el sirviente escritor que corresponde al usuario destino en el servidor central
        //y le enviamos el mensaje
        SirvienteEscritor sirvienteEscritor = sirviente.getServidorChat().getSirvientesEscritor().get(usuarioDestino);
        sirvienteEscritor.insertaMensaje(mensaje);
        //obtenemos el sirviente escritor que corresponde al usuario origen en el servidor central
        //y le enviamos el mensaje
        sirvienteEscritor = sirviente.getServidorChat().getSirvientesEscritor().get(usuarioOrigen);
        sirvienteEscritor.insertaMensaje(mensaje);
        
        
    }
    
    private Color getColorUsuario(Usuario usuario){
        return sirviente.getServidorChat().getColoresAsignados().get(usuario);
    }
    
    
    
   
        
        
 
}
