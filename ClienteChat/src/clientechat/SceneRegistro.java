/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mensajes.MensajeRegistro;
import mensajes.MensajeRegistroRespuesta;

/**
 *
 * @author Alberto
 */
public class SceneRegistro extends Scene {
    
    FXApplicationMain app;
    
    GridPane gPaneMain;
    HBox hboxAcceder;
    
    Text textHeader;
    Text textNombreUsuario;
    Text textEmail;
    Text textPassword;
    Text textRepeatPassword;
    Text textYaTienesCuenta;
    Text textRespuesta;
    
    TextField textFieldNombreUsuario;
    TextField textFieldEmail;
    PasswordField passwordFieldPassword;
    PasswordField PasswordFieldRepeatPassword;
    
    Button buttonRegistrarse;
    Button buttonAcceder;
    

    public SceneRegistro(Parent root, FXApplicationMain app) {
        super(new GridPane());
        
        this.app = app;
        
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
        hboxAcceder = new HBox(10);
        
        textHeader = new Text("Registrarse");
        textNombreUsuario = new Text("Nombre de Usuario:");
        textEmail = new Text("Email:");
        textPassword = new Text("Password:");
        textRepeatPassword = new Text("Repetir Password:");
        textYaTienesCuenta = new Text("¿Ya tienes cuenta?");
        textRespuesta = new Text("");

        textFieldNombreUsuario = new TextField();
        textFieldEmail = new TextField();
        
        passwordFieldPassword = new PasswordField();
        PasswordFieldRepeatPassword = new PasswordField();
        
        buttonAcceder = new Button("Acceder");
        buttonRegistrarse = new Button("Registrarse");
        
    }
    
    public void colocaNodos(){
        gPaneMain.setGridLinesVisible(false);
        gPaneMain.setVgap(10);
        gPaneMain.setHgap(20);
        gPaneMain.setPadding(new Insets(10,10,10,10));
        
        
        
        GridPane.setConstraints(textHeader, 0, 0);
        GridPane.setConstraints(textNombreUsuario, 0, 1);
        GridPane.setConstraints(textEmail, 0, 2);
        GridPane.setConstraints(textPassword, 0, 3);
        GridPane.setConstraints(textRepeatPassword, 0, 4);
        GridPane.setConstraints(textFieldNombreUsuario, 1, 1);
        GridPane.setConstraints(textFieldEmail, 1, 2);
        GridPane.setConstraints(passwordFieldPassword, 1, 3);
        GridPane.setConstraints(PasswordFieldRepeatPassword, 1, 4);
        GridPane.setConstraints(buttonRegistrarse, 0, 5);
        GridPane.setConstraints(hboxAcceder, 0, 6);
        GridPane.setConstraints(textRespuesta, 0, 7);
        
        GridPane.setColumnSpan(textHeader, 2);
        GridPane.setColumnSpan(buttonRegistrarse, 2);
        GridPane.setColumnSpan(hboxAcceder, 2);
        GridPane.setColumnSpan(textRespuesta, 2);
        
        GridPane.setHalignment(textHeader, HPos.CENTER);
        GridPane.setHalignment(textNombreUsuario, HPos.RIGHT);
        GridPane.setHalignment(textEmail, HPos.RIGHT);
        GridPane.setHalignment(textRepeatPassword, HPos.RIGHT);
        GridPane.setHalignment(textPassword, HPos.RIGHT);
        GridPane.setHalignment(buttonRegistrarse, HPos.RIGHT);
        GridPane.setHalignment(textRespuesta, HPos.CENTER);
        

        hboxAcceder.getChildren().addAll(textYaTienesCuenta, buttonAcceder);
        hboxAcceder.setAlignment(Pos.CENTER_RIGHT);
        
        gPaneMain.getChildren().addAll(textHeader, textNombreUsuario, textEmail,
            textPassword,textRepeatPassword, textFieldNombreUsuario, textFieldEmail,
            passwordFieldPassword, PasswordFieldRepeatPassword, buttonRegistrarse,
            hboxAcceder, textRespuesta);
        

        this.setRoot(gPaneMain);
        
    }
    
    public void configuraNodos(){
        
    }
    
    public void configuraListeners(){
        buttonAcceder.setOnAction((ActionEvent event) -> {
            ((FXApplicationMain)app).cambiaEscenaLogin();
        });
        
        buttonRegistrarse.setOnAction(e -> enviaMensajeRegistro());
    }
    
    private void enviaMensajeRegistro(){
        buttonRegistrarse.setDisable(true);
        textRespuesta.setText("Procesando...");
        
        //construimos el mensaje de registro
        MensajeRegistro mensaje = new MensajeRegistro();
        
        mensaje.setEmail(textFieldEmail.getText());
        mensaje.setLogin(textFieldNombreUsuario.getText());
        mensaje.setPassword(passwordFieldPassword.getText());
        mensaje.setRepeatPassword(PasswordFieldRepeatPassword.getText());
                
        
        try {
            app.getOos().writeObject(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(SceneRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void mensajeRegistroRespuestaRecibido(MensajeRegistroRespuesta mensaje){
        buttonRegistrarse.setDisable(false);
        textRespuesta.setText(mensaje.getTextoRespuesta());
    }

    
    
    
}
