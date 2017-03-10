/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clientechat.CellUsuario;
import clientechat.SceneChat;
import clientechat.SirvienteEscritor;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import mensajes.MensajeActualizacionUsuariosConectados;
import mensajes.MensajeChatPrivado;
import mensajes.MensajeChatPublico;
import mensajes.MensajeUsuariosConectados;
import modelo.MensajesPrivados;
import modelo.MensajesPublicos;
import modelo.Usuario;
import sun.applet.Main;

/**
 *
 * @author Alberto
 */
public class ControladorChat extends Controlador {
    
    
    MensajesPrivados mensajesPrivados;
    MensajesPublicos mensajesPublicos;
    
    Usuario usuarioConectado;
    SirvienteEscritor sirvienteEscritor;
    
    SceneChat sceneChat;
    
    Media sonidoNuevoMensajePrivado;
    MediaPlayer reproductor;
    
    public ControladorChat(SirvienteEscritor sirvienteEscritor){
        mensajesPrivados = new MensajesPrivados();
        mensajesPublicos = new MensajesPublicos();
        
        this.sirvienteEscritor = sirvienteEscritor;
        
        String audioConnected = "resources/sounds/newPrivateMessage.mp3";
        sonidoNuevoMensajePrivado =  new Media(new File(audioConnected).toURI().toString());
        reproductor = new MediaPlayer(sonidoNuevoMensajePrivado);
    }
    
    public void procesaMensajeUsuariosConectados(MensajeUsuariosConectados mensaje){
        List<CellUsuario> cellUsuarios = new ArrayList<CellUsuario>();
        for(Usuario u : mensaje.getUsuarios()){
            CellUsuario cellUsuario = new CellUsuario();
            cellUsuario.setUsuario(u);
            if(mensajesPrivados.getMensajesPrivados().get(u) != null)
                cellUsuario.setNumeroMensajes(mensajesPrivados.getMensajesPrivados().get(u).size());
            else
                cellUsuario.setNumeroMensajes(0);
            
            cellUsuarios.add(cellUsuario);
        }
        sceneChat.cargaUsuariosConectados(cellUsuarios);
    }

    public SceneChat getSceneChat() {
        return sceneChat;
    }

    public void setSceneChat(SceneChat sceneChat) {
        this.sceneChat = sceneChat;
    }
    
    

    public MensajesPrivados getMensajesPrivados() {
        return mensajesPrivados;
    }

    public void setMensajesPrivados(MensajesPrivados mensajesPrivados) {
        this.mensajesPrivados = mensajesPrivados;
    }

    public MensajesPublicos getMensajesPublicos() {
        return mensajesPublicos;
    }

    public void setMensajesPublicos(MensajesPublicos mensajesPublicos) {
        this.mensajesPublicos = mensajesPublicos;
    }

    public Usuario getUsuarioConectado() {
        return usuarioConectado;
    }

    public void setUsuarioConectado(Usuario usuarioConectado) {
        this.usuarioConectado = usuarioConectado;
    }

    public SirvienteEscritor getSirvienteEscritor() {
        return sirvienteEscritor;
    }

    public void setSirvienteEscritor(SirvienteEscritor sirvienteEscritor) {
        this.sirvienteEscritor = sirvienteEscritor;
    }
    
    
    
    public void procesaMensajePublicoSalida(String mensaje){
        //construimos el mensaje publico
        MensajeChatPublico mensajeChatPublico = new MensajeChatPublico(mensaje);
        
        mensajeChatPublico.setUsuarioOrigen(usuarioConectado);
        
        sirvienteEscritor.insertaMensaje(mensajeChatPublico);
    }
    
    public void procesaMensajePublicoEntrada(MensajeChatPublico mensaje){
        mensajesPublicos.nuevoMensaje(mensaje);
    }
    
    public void procesaMensajePrivadoSalida(MensajeChatPrivado mensaje){
        sirvienteEscritor.insertaMensaje(mensaje);
    }
    
    public void procesaMensajePrivadoEntrada(MensajeChatPrivado mensaje){
        mensajesPrivados.nuevoMensaje(mensaje);
        
        
        //solamente reproducimos los sonidos cuando sena mensajes de otros usuarios
        if(!mensaje.getUsuarioOrigen().equals(usuarioConectado)){
            new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
              public void run() {
                try {
                  Clip clip = AudioSystem.getClip();
                  AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    new File("resources\\sounds\\newPrivateMessage.wav"));
                  clip.open(inputStream);
                  clip.start(); 
                } catch (Exception e) {
                  System.err.println(e.getMessage());
                }
              }
            }).start();
        }
        
    }
    
    public void procesaMensajeActualizacionUsuariosConectados(MensajeActualizacionUsuariosConectados mensaje){
        if(mensaje.getUsuario() == null || mensaje.getEstadoConexion() == null)
            return;
        
        sceneChat.actualizacionUsuario(mensaje.getUsuario(), mensaje.getEstadoConexion());
    }
 
}
