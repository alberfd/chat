/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml; 
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class TestList extends Application {
    
    ListView<Usuario> listView;
    
    WebView webView;
    
    HTMLEditor htmlEditor;
    
    @Override
    public void start(Stage primaryStage) {
        
        WebView wv = new WebView();
        Button btn = new Button();
        TextField textField = new TextField();
        
        VBox vb = new VBox();
        
        WebEngine appendEngine = wv.getEngine();
            btn.setOnAction(new EventHandler<ActionEvent>() {
             @Override public void handle(ActionEvent event) {
               executejQuery(appendEngine, "$('#content').append(\"<p align=right><b>World!"+ escapeHtml(textField.getText()) + "<b><p>\");");
                 
               executejQuery(appendEngine, "$(\"html, body\").animate({ scrollTop: $(document).height()-$(window).height() });");
             }
            });
            
            wv.getEngine().loadContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
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
            
        
        
        vb.getChildren().addAll(wv, btn, textField);
        
        Scene scene = new Scene(vb);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
   

    public void procesa(){
        ObservableList<Usuario> list;
        list = listView.getSelectionModel().getSelectedItems();
        
        for(Usuario u : list){
            System.out.println(u.getEmail());
        }
        
    }
    
    private static Object executejQuery(final WebEngine engine, String script) {
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
