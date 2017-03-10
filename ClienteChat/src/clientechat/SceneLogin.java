/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import controladores.ControladorUsuario;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mensajes.MensajeLogin;
import mensajes.MensajeLoginRespuesta;
import modelo.TipoRespuestaLogin;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class SceneLogin extends Scene {
    
    FXApplicationMain app;
    
    GridPane gPaneMain;
    HBox hboxRegistrarse;
    
    Text textHeader;
    Text textNombreUsuario;
    Text textPassword;
    Text textRegistrarse;
    Text textRespuesta;
    
    
    TextField textFieldNombreUsuario;
    PasswordField textFieldPassword;
    
    Button buttonAcceder;
    Button buttonRegistrarse;
    
    ControladorUsuario controladorUsuario;

    public SceneLogin(Parent root, FXApplicationMain app, ControladorUsuario controlador) {
        super(new GridPane());
        
        this.app = app;
        
        this.controladorUsuario = controlador;
        
        start();
    }
    
    public void start() {
        
        
        creaNodos();
        colocaNodos();
        configuraNodos();
        configuraListeners();
        
   
    }
    
    public void creaNodos(){
        gPaneMain = new GridPane();
        hboxRegistrarse = new HBox(10);
        
        textHeader = new Text("Acceso");
        textNombreUsuario = new Text("Nombre de Usuario:");
        textPassword = new Text("Password:");
        textRegistrarse = new Text("¿No tienes cuenta?");
        textRespuesta = new Text("");
        
        buttonAcceder = new Button("Acceder");
        buttonRegistrarse = new Button("Registrase");
        
        textFieldNombreUsuario = new TextField();
        textFieldPassword = new PasswordField();
    }
    
    public void colocaNodos(){
        gPaneMain.setGridLinesVisible(false);
        gPaneMain.setVgap(10);
        gPaneMain.setHgap(20);
        gPaneMain.setPadding(new Insets(10,10,10,10));
        
        
        
        GridPane.setConstraints(textHeader, 0, 0);
        GridPane.setConstraints(textNombreUsuario, 0, 1);
        GridPane.setConstraints(textPassword, 0, 2);
        GridPane.setConstraints(textFieldNombreUsuario, 1, 1);
        GridPane.setConstraints(textFieldPassword, 1, 2);
        GridPane.setConstraints(buttonAcceder, 0, 3);
        GridPane.setConstraints(hboxRegistrarse, 0, 4);
        GridPane.setConstraints(textRespuesta, 0, 5);
        
        GridPane.setColumnSpan(textHeader, 2);
        GridPane.setColumnSpan(buttonAcceder, 2);
        GridPane.setColumnSpan(hboxRegistrarse, 2);
         GridPane.setColumnSpan(textRespuesta, 2);
        
        GridPane.setHalignment(textHeader, HPos.CENTER);
        GridPane.setHalignment(textNombreUsuario, HPos.RIGHT);
        GridPane.setHalignment(textPassword, HPos.RIGHT);
        GridPane.setHalignment(buttonAcceder, HPos.RIGHT);
        GridPane.setHalignment(textRespuesta, HPos.CENTER);
        

        hboxRegistrarse.getChildren().addAll(textRegistrarse, buttonRegistrarse);
        hboxRegistrarse.setAlignment(Pos.CENTER_RIGHT);
        
        gPaneMain.getChildren().addAll(textHeader, textNombreUsuario,
            textPassword,textFieldNombreUsuario, textFieldPassword, buttonAcceder,
            hboxRegistrarse, textRespuesta);
        
        
        this.setRoot(gPaneMain);
        
  
    }
    
    public void configuraNodos(){
        
    }
    
    public void configuraListeners(){
        buttonAcceder.setOnAction((ActionEvent event) -> {
            accederPulsado();
        });
        
        buttonRegistrarse.setOnAction((ActionEvent event) -> {
            ((FXApplicationMain)app).cambiaEscenaRegistro();
        });
    }
    
    private void accederPulsado(){
        //deshabilitamos el boton de acceder para prevenir que el usuario
        //lo pulse constantemente y mostramos el mensaje conectando...
        buttonAcceder.setDisable(true);
        textRespuesta.setText("Conectando...");
        
        //Construimos usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(textFieldNombreUsuario.getText());
        usuario.setPassword(textFieldPassword.getText());
        
        
        //le decimos al controlador que procese la peticion del usuario
        controladorUsuario.login(usuario);
        
        //Volvemos a habilitar el boton de acceder en caso de que no 
        //hubiera respuesta por parte del servidor
        Runnable runnable = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SceneLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            Platform.runLater(() -> {
                if(buttonAcceder.isDisabled())
                    buttonAcceder.setDisable(false);
            });
        };
        new Thread(runnable).start();
               
        
    }
   
    
    public void setRespuesta(String respuesta){
        
        Platform.runLater(
            () -> {
              buttonAcceder.setDisable(false);
              this.textRespuesta.setText(respuesta);
            }
        );
        
    }
    
}
