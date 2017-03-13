/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import controladores.ControladorChat;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import mensajes.MensajeChatPublico;
import modelo.EstadoConexion;
import static modelo.HTMLUtils.generaHTMLInicialSala;
import static modelo.HTMLUtils.generaHTMLMensajePublico;
import static modelo.HTMLUtils.generaHTMLMensajePublicoPropio;
import modelo.MensajesPublicos;
import modelo.Usuario;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 *
 * @author Alberto
 */
public class PestañaSala extends Tab implements Observer {

    GridPane gridPaneSala;
    TextField textFieldMensajePublico;
    TextArea textAreaPublico;
    WebView webViewChatSala;
    ListView<CellUsuario> listViewUsuarios;
    
    SceneChat sceneChat;
    
    //Controlador
    ControladorChat controladorChat;
    
    public PestañaSala(ControladorChat controlador, SceneChat sceneChat){
        super("Sala");
        
        this.controladorChat = controlador;
        
        this.sceneChat = sceneChat;
        
        gridPaneSala = new GridPane();
        textFieldMensajePublico = new TextField();
        textAreaPublico = new TextArea();
        webViewChatSala = new WebView();
        webViewChatSala.getEngine().loadContent(generaHTMLInicialSala());
        sceneChat.executejQuery(webViewChatSala.getEngine(), "$(document)");

        listViewUsuarios = new ListView<>();
        
        GridPane.setConstraints(webViewChatSala, 0, 0);
        GridPane.setConstraints(textFieldMensajePublico, 0, 1);
        GridPane.setConstraints(listViewUsuarios, 1, 0);
        
        gridPaneSala.setGridLinesVisible(false);
        gridPaneSala.getChildren().addAll(webViewChatSala, textFieldMensajePublico, listViewUsuarios);
        
        this.setContent(gridPaneSala);
        
        
        configuraListeners();
    }
    
    private void configuraListeners(){
        textFieldMensajePublico.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER))
                    introPulsado();
            }
            
        });
        
        listViewUsuarios.setCellFactory(param -> new ListCell<CellUsuario>() {
            @Override
            protected void updateItem(CellUsuario item, boolean empty) {
                super.updateItem(item, empty);
                   
                if (empty || item == null || item.getUsuario()== null) {
                    setText(null);
                } else {
                    String text = item.getUsuario().getNombreUsuario();
                    
                    if(item.getNumeroMensajes() > 0){
                        text = text.concat(" (" + item.getNumeroMensajes() + ")");
                    }
                    
                    setText(text);
                }
            }
        });
        
        listViewUsuarios.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Usuario usuarioSeleccionado =((ListView<CellUsuario>)event.getSource()).getSelectionModel().getSelectedItem().getUsuario();
                    //comprobamos si ya existe una pestaña abierta para ese usuario
                    PestañaMensajePrivado pestaña = null;
                    boolean abierta = false;
                    for(Tab tab : sceneChat.getTabPane().getTabs()){
                        if(tab instanceof PestañaMensajePrivado){
                            pestaña = (PestañaMensajePrivado) tab;
                            if(pestaña.getUsuarioRemoto().equals(usuarioSeleccionado)){
                                abierta = true;
                                break;
                            }
                        }
                    }
                    if(!abierta){
                        pestaña = new PestañaMensajePrivado(controladorChat, sceneChat);
                        pestaña.setUsuarioRemoto(usuarioSeleccionado);
                        pestaña.setText(usuarioSeleccionado.getNombreUsuario());
                        pestaña.suscribete(controladorChat.getMensajesPrivados());
                        sceneChat.getTabPane().getTabs().add(pestaña);
                    }
                    
                    //ponemos el foco en la pestaña del usuario seleccionado
                    sceneChat.getTabPane().getSelectionModel().select(pestaña);
                }
            }
            
        });
    }
    
    public void incrementaMensajesLeidosUsuario(Usuario usuario){
        
                
        for(CellUsuario cellUsuario : listViewUsuarios.getItems()){
            if(cellUsuario.getUsuario().equals(usuario)){
                ObservableList<CellUsuario> ob = listViewUsuarios.getItems();
                cellUsuario.setNumeroMensajes(cellUsuario.getNumeroMensajes() + 1);
                ob.set(ob.indexOf(cellUsuario), cellUsuario);
  
            }
        }
    }
    
    public void marcaMensajesLeidosListView(Usuario usuario){
        //buscamos el CELLVIEW
        CellUsuario cellUsuario = dameCellViewByUsuario(usuario);
        cellUsuario.setNumeroMensajes(0);
        
        ObservableList<CellUsuario> ob = listViewUsuarios.getItems();
        
        ob.set(ob.indexOf(cellUsuario), cellUsuario);
        
        
    }
    
    public CellUsuario dameCellViewByUsuario(Usuario usuario){
        for(CellUsuario cellUsuario : listViewUsuarios.getItems()){
            if(cellUsuario.getUsuario().equals(usuario)){
                return cellUsuario;
            }
        }
        
        return null;
    }
    
    public void introPulsado(){
        //si el textField esta vacio no hacemos nada
        if(textFieldMensajePublico.getText().equals(""))
            return;
        
        //escribimos el mensaje en el webview
        //String html = "<p><span style=\"font-size:14px;font-family: arial,sans-serif;\"><b>hola</b>hola</span></p>";
        String html = generaHTMLMensajePublicoPropio(controladorChat.getColorChat(), controladorChat.getUsuarioConectado().getNombreUsuario(),
                textFieldMensajePublico.getText());
  
   
        sceneChat.executejQuery(webViewChatSala.getEngine(), "$('#content').append(\'" + html + "\');");
             
        sceneChat.executejQuery(webViewChatSala.getEngine(), "$(\"html, body\").animate({ scrollTop: $(document).height()-$(window).height() }, 100);");
        
        //le decimos al controlador que se desea enviar un nuevo mensaje publico
        controladorChat.procesaMensajePublicoSalida(textFieldMensajePublico.getText());
        
        //borramos el contenido del textfield para prepararlo para otro mensaje
        textFieldMensajePublico.setText("");
    }
    
    //No es necesario que sea la propia vista la que se suscriba al modelo
    public void suscribete(MensajesPublicos mensajesPublicos){
        
        
        mensajesPublicos.addObserver(this);
    }
            
    
    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
        MensajesPublicos mensajesPublicos = ((MensajesPublicos) o);
        Iterator iterador;
        MensajeChatPublico mensajeChatPublico;
        String html;
        
        iterador = mensajesPublicos.getMensajesPublicos().iterator();
        
        
        while(iterador.hasNext()){
            mensajeChatPublico = mensajesPublicos.getMensajesPublicos().poll();
            
            html = generaHTMLMensajePublico(mensajeChatPublico);
            
            sceneChat.executejQuery(webViewChatSala.getEngine(), "$('#content').append(\'" + html + "\');");
             
            sceneChat.executejQuery(webViewChatSala.getEngine(), "$(\"html, body\").animate({ scrollTop: $(document).height()-$(window).height() }, 100);");
        }
        });
        
    }
    
    public void cargaUsuariosConectados(List<CellUsuario> cellUsuarios){
        Platform.runLater(() -> {
            listViewUsuarios.getItems().clear();
            listViewUsuarios.getItems().addAll(cellUsuarios);
            
        });
        
    }
    
    public void actualizaEstadoUsuario(Usuario usuario, EstadoConexion estado){
        
        Platform.runLater(() -> {
        //si el estado es desconexion buscamos el usuario y lo eliminamos de la listView
        //tambien cerramos la pestaña de mensajes privados con ese usuario
        if(estado == EstadoConexion.DESCONECTADO){
            for(CellUsuario cell : listViewUsuarios.getItems()){
                if(cell.getUsuario().equals(usuario)){
                    listViewUsuarios.getItems().remove(cell);
                    break;
                }
            }
            //cerramos las pestañas de usuario desconectado
            for(Tab tab : sceneChat.getTabPane().getTabs()){
                if(tab instanceof PestañaMensajePrivado){
                    if( ((PestañaMensajePrivado)tab).getUsuarioRemoto().equals(usuario)){
                        sceneChat.getTabPane().getTabs().remove(tab);
                        break;
                    }
                }
            }
        }else{
            //si el estado es conexion lo añadimos a la lista
            CellUsuario cell = new CellUsuario();
            cell.setUsuario(usuario);
            cell.setNumeroMensajes(0);
            listViewUsuarios.getItems().add(cell);
            
        }
            
        });
        
        
        
    }

    public ListView<CellUsuario> getListViewUsuarios() {
        return listViewUsuarios;
    }
    
    
    
    
    
}
