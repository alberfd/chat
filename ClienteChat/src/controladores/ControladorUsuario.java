/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clientechat.SceneChat;
import clientechat.SceneLogin;
import clientechat.SirvienteEscritor;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mensajes.MensajeLogin;
import mensajes.MensajeLoginRespuesta;
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
    
    
    
}
