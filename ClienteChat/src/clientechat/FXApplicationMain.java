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
import java.util.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class FXApplicationMain extends Application implements Observer {
    
    Socket clientSocket;
    int portNumber = 5555;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    
    Usuario usuario;
    
    
    SirvienteCliente sirvienteCliente;
    SirvienteEscritor sirvienteEscritor;
    
    Thread hiloCliente;
    Thread hiloEscritor;
    
    //controladores
    ControladorUsuario controladorUsuario;
    ControladorChat controladorChat;
    
    SceneLogin sceneLogin;
    SceneRegistro sceneRegistro;
    SceneChat sceneChat;
    
    Stage stage;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        clientSocket = new Socket("127.0.0.1", portNumber);
        ois = new ObjectInputStream(clientSocket.getInputStream());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        
        sirvienteCliente = new SirvienteCliente(clientSocket, oos, ois, this);
        sirvienteEscritor = new SirvienteEscritor(clientSocket, oos, ois);
        
        controladorUsuario = new ControladorUsuario(sirvienteEscritor);
        controladorChat = new ControladorChat(sirvienteEscritor);
        
        sceneLogin = new SceneLogin(null, this, controladorUsuario);
        sceneRegistro = new SceneRegistro(null, this);
        sceneChat = new SceneChat(null, this, controladorChat);
        
        
        //asignamos a los controladores la aplicacion principal
        controladorUsuario.setApp(this);
        controladorChat.setApp(this);
        
        //asignamos al controlador frontal los controladores concretos
        sirvienteCliente.setControladorChat(controladorChat);
        sirvienteCliente.setControladorUsuario(controladorUsuario);
        
        
        
        
        //asignamos las vistas a los controladores
        controladorUsuario.setSceneLogin(sceneLogin);
        controladorUsuario.setSceneChat(sceneChat);
        
        controladorChat.setSceneChat(sceneChat);
        
        hiloCliente = new Thread(sirvienteCliente);
        hiloCliente.start();
        hiloEscritor = new Thread(sirvienteEscritor);
        hiloEscritor.start();
        
        
        sirvienteCliente.addObserver(this);
        
        
        this.stage = primaryStage;
        
        
        
        
        
        primaryStage.setTitle("Acceder");
        primaryStage.setScene(sceneLogin);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> cerrarApp());
        
 
    }
    
    private void iniciaComunicaciones() {
        
        
        
    }
    
    public void cambiaEscenaRegistro(){
        stage.setTitle("Registrarse");
        stage.setScene(sceneRegistro);
    }
    
    public void cambiaEscenaLogin(){
        stage.setTitle("Acceder");
        stage.setScene(sceneLogin);
    }
    
    public void cambiaEscenaChat(){
        Platform.runLater(
            () -> {
              stage.setTitle("Chat Usuario: " + controladorChat.getUsuarioConectado());
              stage.setScene(sceneChat);
              centraStage();
            }
        );
        
    }
    
    public void centraStage(){
        stage.centerOnScreen();
    }
    
    public Stage getStage(){
        return this.stage;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public SceneLogin getSceneLogin() {
        return sceneLogin;
    }

    public void setSceneLogin(SceneLogin sceneLogin) {
        this.sceneLogin = sceneLogin;
    }

    public SceneRegistro getSceneRegistro() {
        return sceneRegistro;
    }

    public void setSceneRegistro(SceneRegistro sceneRegistro) {
        this.sceneRegistro = sceneRegistro;
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
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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

    
    
    @Override
    public void update(Observable o, Object arg) {
        
        
    }
    
    private void cerrarApp(){
         stage.close();
        
        hiloCliente.interrupt();
        hiloEscritor.interrupt();
        
        System.exit(0);
        
       
    }
    
}
