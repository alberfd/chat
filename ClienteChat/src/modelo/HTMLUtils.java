/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Color;
import mensajes.Mensaje;
import mensajes.MensajeChatPrivado;
import mensajes.MensajeChatPublico;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 *
 * @author Alberto
 */
public class HTMLUtils {
    
    private static String htmlInicialSala = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "<title>Documento sin título</title>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body id=\"content\">\n" +
                    " \n" +
                    "</body>\n" +
                    "</html>";
    
    private static String htmlMensajePublico = "<p><span style=\"font-size:14px;\">"
                    + "<span style=\"font-family:Georgia\">"
                    + "<span style=\"color:"+  "///***COLOR***///"  +"; \"> <b>" + "///***NOMBRE_USUARIO***///" + ": " 
                    + "</b></span>" + "///***MENSAJE***///" + "</span></span></p>";
    
    private static String htmlMensajePublicoPropio = "<p style=\"text-align: right;\"><span style=\"font-size:14px; text-align:right\">"
                    + "<span style=\"font-family:Georgia\">"
                    + "<span style=\"color:"+ "///***COLOR***///" +"; \"> <b>" + "///***NOMBRE_USUARIO***///" + ": " 
                    + "</b></span>" + "///***MENSAJE***///" + "</span></span></p>";
    
    private static String htmlMensajePrivado = "<p><span style=\"font-size:14px;\">"
                    + "<span style=\"font-family:Georgia;\">"
                    + "<span style=\"color:"+ "///***COLOR***///" +";font-weight: bold;\">" + "///***NOMBRE_USUARIO***///" + ": " 
                    + "</span>" + "///***MENSAJE***///" + "</span></span></p>";
    
    private static String htmlMensajePrivadoPropio = "<p style=\"text-align: right;\"><span style=\"font-size:14px;\">"
                    + "<span style=\"font-family:Georgia;\">"
                    + "<span style=\"color:"+ "///***COLOR***///" +";font-weight: bold;\">" + "///***NOMBRE_USUARIO***///" + ": " 
                    + "</span>" + "///***MENSAJE***///" + "</span></span></p>";
    
    public HTMLUtils(){
        
    }
    
    public static String generaHTMLInicialSala(){
        return htmlInicialSala;
    }
    
    public static String generaHTMLMensaje(Mensaje mensaje, boolean propio){
        String result;
        if(mensaje instanceof MensajeChatPublico && propio){
            mensaje = (MensajeChatPublico) mensaje;
            result = htmlMensajePublicoPropio;
        }
        else if(mensaje instanceof MensajeChatPublico && ! propio){
            mensaje = (MensajeChatPublico) mensaje;
            result = htmlMensajePublico;
        }
        else if(mensaje instanceof MensajeChatPrivado && propio){
            mensaje = (MensajeChatPrivado) mensaje;
            result = htmlMensajePrivado;
        }
        else if(mensaje instanceof MensajeChatPrivado && !propio){
            mensaje = (MensajeChatPrivado) mensaje;
            result = htmlMensajePrivadoPropio;
        }
        else
            result = "";
        
        result = rellenaHTMLMensaje(result, mensaje);
        
        return result;
        
        
    }
    
    private static String rellenaHTMLMensaje(String html, Mensaje mensaje){
        if(mensaje instanceof MensajeChatPublico){
            MensajeChatPublico mensajePublico = (MensajeChatPublico) mensaje;
            html = html.replace("///***COLOR***///", colorToHex(mensaje.getColorNombreUsuario()));
            html = html.replace("///***NOMBRE_USUARIO***///", mensajePublico.getUsuarioOrigen().getNombreUsuario());
            html = html.replace("///***MENSAJE***///", escapeHtml(mensajePublico.getMensaje()).replace("'", "\\'"));
        }else if(mensaje instanceof MensajeChatPrivado){
            MensajeChatPrivado mensajeChatPrivado = (MensajeChatPrivado) mensaje;
            html = html.replace("///***COLOR***///", colorToHex(mensaje.getColorNombreUsuario()));
            html = html.replace("///***NOMBRE_USUARIO***///", mensajeChatPrivado.getUsuarioOrigen().getNombreUsuario());
            html = html.replace("///***MENSAJE***///", escapeHtml(mensajeChatPrivado.getMensaje()).replace("'", "\\'"));
            
        }
        
        return html;
    }
    
    public static String generaHTMLMensajePublico(MensajeChatPublico mensaje){
        String result =  htmlMensajePublico;
        result = result.replace("///***COLOR***///", colorToHex(mensaje.getColorNombreUsuario()));
        result = result.replace("///***NOMBRE_USUARIO***///", mensaje.getUsuarioOrigen().getNombreUsuario());
        result = result.replace("///***MENSAJE***///", escapeHtml(mensaje.getMensaje()).replace("'", "\\'"));
        
        return result;
    }
    
    public static String generaHTMLMensajePublicoPropio(Color color, String nombreUsuario, String mensaje){
        String result =  htmlMensajePublicoPropio;
        result = result.replace("///***COLOR***///", colorToHex(color));
        result = result.replace("///***NOMBRE_USUARIO***///", nombreUsuario);
        result = result.replace("///***MENSAJE***///", escapeHtml(mensaje).replace("'", "\\'"));
        
        return result;
    }
    
    public static String generaHTMLMensajePrivado(MensajeChatPrivado mensaje){
        String result =  htmlMensajePrivado;
        result = result.replace("///***COLOR***///", colorToHex(mensaje.getColorNombreUsuario()));
        result = result.replace("///***NOMBRE_USUARIO***///", mensaje.getUsuarioOrigen().getNombreUsuario());
        result = result.replace("///***MENSAJE***///", escapeHtml(mensaje.getMensaje()).replace("'", "\\'"));
        
        return result;
    }
    
    public static String generaHTMLMensajePrivadoPropio(MensajeChatPrivado mensaje){
        String result =  htmlMensajePrivadoPropio;
        result = result.replace("///***COLOR***///", colorToHex(mensaje.getColorNombreUsuario()));
        result = result.replace("///***NOMBRE_USUARIO***///", mensaje.getUsuarioOrigen().getNombreUsuario());
        result = result.replace("///***MENSAJE***///", escapeHtml(mensaje.getMensaje()).replace("'", "\\'"));
        
        return result;
    }
   
    private static String colorToHex(Color color){
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return hex;
    }
}
