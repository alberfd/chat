/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import controladores.ControladorChat;
import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import modelo.EstadoConexion;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class SceneChat extends Scene implements Observer {

    FXApplicationMain app;
    
    VBox vboxMain;
    TabPane tabPane;
    
    //menu
    MenuBar menuBar;
    Menu menuArchivo;
    Menu menuEdicion;
    MenuItem menuItemSalir;
    
    ControladorChat controladorChat;
    

    public SceneChat(Parent root, FXApplicationMain app, ControladorChat controlador) {
        super(new GridPane());
        
        this.app = app;
        
        this.controladorChat = controlador;
        
        controladorChat.getMensajesPrivados().addObserver(this);
        
        start();
    }
    
    
    public void start() {
        
        
        creaNodos();
        colocaNodos();
        configuraNodos();
        configuraListeners();
        

    }
    
    public void creaNodos(){
        vboxMain = new VBox(0);
        tabPane = new TabPane();

        PestañaSala pestañaSala = new PestañaSala(controladorChat, this);
        pestañaSala.suscribete(controladorChat.getMensajesPublicos());
        pestañaSala.setClosable(false);
        
        tabPane.getTabs().add(pestañaSala);
        
        menuBar = new MenuBar();
        menuArchivo = new Menu("Archivo");
        menuEdicion = new Menu("Edicion");
        menuItemSalir = new MenuItem("Salir");
        
        menuBar.getMenus().addAll(menuArchivo, menuEdicion);
        menuArchivo.getItems().addAll(menuItemSalir);

        vboxMain.getChildren().addAll(menuBar, tabPane);
        
        
        
        this.setRoot(vboxMain);
        
    }
    
    public void colocaNodos(){
        
  
    }
    
    public void configuraNodos(){
        
    }
    
    public void configuraListeners(){
        
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                //TODO Quitar la imagen del titulo de la pestaña (Si existiera) y
                //de la lista de usuarios conectados (Si existiera tambien)
                if(newTab instanceof PestañaMensajePrivado){
                    
                    PestañaMensajePrivado pestaña = (PestañaMensajePrivado) newTab;
                    Usuario usuarioRemoto = pestaña.getUsuarioRemoto();
                    pestaña.setText(pestaña.getUsuarioRemoto().getNombreUsuario());
                    
                    PestañaSala pestañaSala = damePestañaSala();
                    
                    pestañaSala.marcaMensajesLeidosListView(usuarioRemoto);
                    
                    
                }
            }
        });
    }
    
    public PestañaSala damePestañaSala(){
        for(Tab tab : tabPane.getTabs()){
            if(tab instanceof PestañaSala){
                return (PestañaSala) tab;
            }
        }
        
        return null;
    }
    
    public PestañaMensajePrivado damePestañaMensajePrivadoByUsuario(Usuario usuario){
        
        
        for(Tab tab : tabPane.getTabs()){
            if(tab instanceof PestañaMensajePrivado){
                if( ((PestañaMensajePrivado)tab).getUsuarioRemoto().equals(usuario) )
                    return (PestañaMensajePrivado)tab;
                
            }
        }
        return null;
    }
    
    

    public void setControlador(ControladorChat controlador){
        this.controladorChat = controlador;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }
    
    
    
    public void cargaUsuariosConectados(List<CellUsuario> usuarios){
       PestañaSala pestañaSala = damePestañaSala();
       
       pestañaSala.cargaUsuariosConectados(usuarios);
           
       
    }
    

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
        Usuario usuarioRemoto = (Usuario)arg;
        //si el mensaje es del propio usuario no hacemos nada
        if(usuarioRemoto.equals(controladorChat.getUsuarioConectado())){
            return;
        }
        //Si ya hay una pestaña atendiendo los mensajes del usuario no hacemos nada
        if(damePestañaMensajePrivadoByUsuario(usuarioRemoto) != null)
            return;
        
        
        PestañaSala pestañaSala = damePestañaSala();
        
        pestañaSala.incrementaMensajesLeidosUsuario(usuarioRemoto);
        });
        
    }
    
    public void actualizacionUsuario(Usuario usuario, EstadoConexion estado){
        PestañaSala pestañaSala = damePestañaSala();
        
        pestañaSala.actualizaEstadoUsuario(usuario, estado);
        
    }
    
    public static Object executejQuery(final WebEngine engine, String script) {
        return engine.executeScript(
          "(function(window, document, version, callback) { "
          + "var j, d;"
          + "var loaded = false;"
          + "if (!(j = window.jQuery) || version > j.fn.jquery || callback(j, loaded)) {"
          + " var script = document.createElement(\"script\");"
          + " script.type = \"text/javascript\";"
          + " script.src = \"http://code.jquery.com/jquery-1.7.2.min.js\";"
          + " script.onload = script.onreadystatechange = function() {"
          + " if (!loaded && (!(d = this.readyState) || d == \"loaded\" || d == \"complete\")) {"
          + " callback((j = window.jQuery).noConflict(1), loaded = true);"
          + " j(script).remove();"
          + " }"
          + " };"
          + " document.documentElement.childNodes[0].appendChild(script) "
          + "} "
          + "})(window, document, \"1.7.2\", function($, jquery_loaded) {" + script + "});"
        );
    }
    
    public String colorToHex(Color color){
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return hex;
    }

    
    
}
