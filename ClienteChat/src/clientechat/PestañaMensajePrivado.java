/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import controladores.ControladorChat;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import mensajes.MensajeChatPrivado;
import static modelo.HTMLUtils.generaHTMLMensajePrivado;
import static modelo.HTMLUtils.generaHTMLMensajePrivadoPropio;
import modelo.MensajesPrivados;
import modelo.Usuario;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 *
 * @author Alberto
 */
public class PestañaMensajePrivado extends Tab implements Observer {
    private final GridPane gridPaneMensajePrivado;
    private final TextArea textAreaMensajePrivado;
    private final WebView webViewMensajesPrivados;
    private final TextField textFieldMensajePrivado;
    
    
    Usuario usuarioRemoto;
    
    //Controlador
    ControladorChat controladorChat;
    
    SceneChat sceneChat;
    
    public PestañaMensajePrivado(ControladorChat controlador, SceneChat sceneChat){
        super();
        
        
        this.controladorChat = controlador;
        
        this.sceneChat = sceneChat;
        
        this.setOnClosed(e -> {
            controladorChat.getMensajesPrivados().deleteObserver(this);
            //hay que liberar asi recursos por un bug en java fx. Se deberian liberar ellos
            //solos al cerrar la pestaña pero no lo hace
            try {
                this.finalize();
            } catch (Throwable ex) {
                Logger.getLogger(PestañaMensajePrivado.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        gridPaneMensajePrivado = new GridPane();
        textAreaMensajePrivado = new TextArea();
        textFieldMensajePrivado = new TextField();
        textAreaMensajePrivado.setWrapText(true);
        webViewMensajesPrivados = new WebView();
        webViewMensajesPrivados.getEngine().loadContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "<title>Documento sin título</title>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body id=\"content\">\n" +
                    " \n" +
                    "</body>\n" +
                    "</html>");
        sceneChat.executejQuery(webViewMensajesPrivados.getEngine(), "$(document)");
        
        
        GridPane.setConstraints(webViewMensajesPrivados, 0, 0);
        GridPane.setConstraints(textFieldMensajePrivado, 0, 1);
        
        gridPaneMensajePrivado.getChildren().addAll(webViewMensajesPrivados, textFieldMensajePrivado);
        
        this.setContent(gridPaneMensajePrivado);
        
        configuraListeners();
        
        
    }
    
    private void configuraListeners(){
        textFieldMensajePrivado.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode().equals(KeyCode.ENTER))
                introPulsado();
        });
    }
    
    private void introPulsado(){
        //Si el textfield esta vacio no hacemos nada
        if(textFieldMensajePrivado.getText().equals(""))
            return;
        
        //construimos el mensaje privado
        MensajeChatPrivado mensajeChatPrivado = new MensajeChatPrivado();
        mensajeChatPrivado.setUsuarioOrigen(controladorChat.getUsuarioConectado());
        mensajeChatPrivado.setUsuarioDestino(this.getUsuarioRemoto());
        mensajeChatPrivado.setMensaje(textFieldMensajePrivado.getText());
        mensajeChatPrivado.setColorNombreUsuario(controladorChat.getColorChat());
        
        //mostramos el mensaje en el webview
        escribeMensaje(mensajeChatPrivado);
        
        //le decimos al controlador que procese el mensaje privado de salida
        controladorChat.procesaMensajePrivadoSalida(mensajeChatPrivado);
        
        //limpiamos el textField para los siguientes mensajes
        textFieldMensajePrivado.setText("");
        
        
    }
    
    public void suscribete(MensajesPrivados mensajesPrivados){
        
        
        mensajesPrivados.addObserver(this);
        
        //Justo despues de suscribirnos miramos a ver si habia mensajes acumulados del usuario remoto
        //este proceso solo se ejecutara al suscribirnos ya que despues entra en juego el patron
        //observador. Pero antes de cargar los mensajes que pudieran estar acumulados esperamos a 
        //que la web se haya cargado, sino, los mensajes no se añadiran
       
        webViewMensajesPrivados.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // new page has loaded, process:     
                cargaMensajesAcumulados();
            }
        });
    }
    
    public void cargaMensajesAcumulados(){
        Iterator iterador;
        MensajeChatPrivado mensajeChatPrivado;
        String texto;
        if(controladorChat.getMensajesPrivados().getMensajesPrivados().get(usuarioRemoto) != null){
                iterador = controladorChat.getMensajesPrivados().getMensajesPrivados().get(usuarioRemoto).iterator();
                while(iterador.hasNext()){
                    
                    mensajeChatPrivado = controladorChat.getMensajesPrivados().getMensajesPrivados().get(usuarioRemoto).poll();
                    
                    escribeMensaje(mensajeChatPrivado);
                    
                    texto = mensajeChatPrivado.getUsuarioOrigen().getNombreUsuario() + ": " +
                            mensajeChatPrivado.getMensaje();
                    this.textAreaMensajePrivado.appendText(texto + "\n");
                }
    }            }

    
    private boolean pestañaSeleccionada(){
        TabPane tabPane = sceneChat.getTabPane();
        //Si la pestaña actual no es la seleccionada le ponemos un asterisco
        //para que el usuario vea que en esta pestaña tiene mensajes nuevos y
        //metemos en el listview un asterisco
         return tabPane.getSelectionModel().getSelectedItem() == this;
    }
 

    @Override
    public void update(Observable o, Object arg) {
        
        Platform.runLater(() -> {
        
        MensajeChatPrivado mensaje = (MensajeChatPrivado) arg;
        Usuario usuarioOrigen = mensaje.getUsuarioOrigen();
        
        MensajesPrivados mensajesPrivados = (MensajesPrivados) o;
        Iterator iterador;
        MensajeChatPrivado mensajeChatPrivado;
            
        
        //si el mensajes que ha entrado no es de esta pestaña pasamos del tema
        if(!usuarioOrigen.equals(this.usuarioRemoto))
            return;
        
        if(!pestañaSeleccionada()){
            this.setText("* " + this.usuarioRemoto.getNombreUsuario());
            
            PestañaSala pestañaSala = sceneChat.damePestañaSala();
            
            //modificamos el listview de la pestaña de la sala poniendo un asterisco
            //en el usuario que envio el mensaje
            if(pestañaSala != null)
                pestañaSala.incrementaMensajesLeidosUsuario(usuarioOrigen);
            
        }
        
        //escribimos los mensajes que haya en la cola en el webview:
        iterador = mensajesPrivados.getMensajesPrivados().get(usuarioRemoto).iterator();
        
        while(iterador.hasNext()){
            mensajeChatPrivado = (MensajeChatPrivado) iterador.next();
            iterador.remove();
            escribeMensaje(mensajeChatPrivado);
          
        }
        });
        
    }

    public Usuario getUsuarioRemoto() {
        return usuarioRemoto;
    }

    public void setUsuarioRemoto(Usuario usuarioRemoto) {
        this.usuarioRemoto = usuarioRemoto;
    }
    
    
    
    private void escribeMensaje(MensajeChatPrivado mensaje){
        
        String html;
        if(mensaje.getUsuarioOrigen().equals(controladorChat.getUsuarioConectado()))
            html = generaHTMLMensajePrivadoPropio(mensaje);
        else
            html = generaHTMLMensajePrivado(mensaje);
           
        sceneChat.executejQuery(webViewMensajesPrivados.getEngine(), "$('#content').append(\'" + html + "\');");
             
        sceneChat.executejQuery(webViewMensajesPrivados.getEngine(), "$(\"html, body\").animate({ scrollTop: $(document).height()-$(window).height() }, 100);");
        
    }
    
    
    
}
