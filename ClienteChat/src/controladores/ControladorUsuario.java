/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clientechat.SceneChat;
import clientechat.SceneLogin;
import clientechat.SceneRegistro;
import clientechat.SirvienteEscritor;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mensajes.MensajeColorChat;
import mensajes.MensajeLogin;
import mensajes.MensajeLoginRespuesta;
import mensajes.MensajeRegistro;
import mensajes.MensajeRegistroRespuesta;
import modelo.TipoRespuestaLogin;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class ControladorUsuario extends Controlador {
    
    //Usuario conectado
    Usuario usuario;
    
    SceneLogin sceneLogin;
    SceneChat sceneChat;
    SceneRegistro sceneRegistro;
    
    SirvienteEscritor sirvienteEscritor;
    
    Media sonidoConectado;
    MediaPlayer reproductor;
    
    public ControladorUsuario(SirvienteEscritor sirvienteEscritor){
        this.sirvienteEscritor = sirvienteEscritor;
        
        String audioConnected = "resources/sounds/connected.mp3";
        sonidoConectado =  new Media(new File(audioConnected).toURI().toString());
    }
    
    public void procesaLoginRespuesta(MensajeLoginRespuesta mensaje){
        if(mensaje.getTipoRespuestaLogin() == TipoRespuestaLogin.ERROR){
            sceneLogin.setRespuesta("Login incorrecto");
        }else{
            reproductor = new MediaPlayer(sonidoConectado);
            reproductor.play();
            this.usuario = mensaje.getUsuario();
            this.app.getControladorChat().setUsuarioConectado(usuario);
            this.app.cambiaEscenaChat();
            
        }
    }

    public SceneLogin getSceneLogin() {
        return sceneLogin;
    }

    public void setSceneLogin(SceneLogin sceneLogin) {
        this.sceneLogin = sceneLogin;
    }

    public SceneChat getSceneChat() {
        return sceneChat;
    }

    public void setSceneChat(SceneChat sceneChat) {
        this.sceneChat = sceneChat;
    }

    public SceneRegistro getSceneRegistro() {
        return sceneRegistro;
    }

    public void setSceneRegistro(SceneRegistro sceneRegistro) {
        this.sceneRegistro = sceneRegistro;
    }
    
    
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public void login(Usuario usuario){
        //construimos el mensaje de login
        MensajeLogin mensajeLogin = new MensajeLogin();
        mensajeLogin.setUsuario(usuario);
        
        //enviamos el mensaje
        sirvienteEscritor.insertaMensaje(mensajeLogin);
        
    }
    
    public void procesaRegistroRespuesta(MensajeRegistroRespuesta mensaje){
        sceneRegistro.mensajeRegistroRespuestaRecibido(mensaje);
    }
    
    public void enviaMensajeRegistro(MensajeRegistro mensaje){
        sirvienteEscritor.insertaMensaje(mensaje);
    }
    
    
    
    
    
}
