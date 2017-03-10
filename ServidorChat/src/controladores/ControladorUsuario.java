/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mappers.DataMapperUsuario;
import mensajes.Mensaje;
import mensajes.MensajeActualizacionUsuariosConectados;
import mensajes.MensajeLogin;
import mensajes.MensajeLoginRespuesta;
import mensajes.MensajeRegistro;
import mensajes.MensajeRegistroRespuesta;
import mensajes.MensajeUsuariosConectados;
import modelo.EstadoConexion;
import modelo.TipoRespuestaLogin;
import modelo.TipoRespuestaRegistro;
import modelo.Usuario;
import servidorchat.Sirviente;
import servidorchat.SirvienteEscritor;
import servidorchat.SirvienteLector;

/**
 *
 * @author Alberto
 */
public class ControladorUsuario extends Controlador {
    
    DataMapperUsuario dataMapperUsuario;
    
    public ControladorUsuario(Sirviente sirviente){
        super(sirviente);
                
        this.dataMapperUsuario = new DataMapperUsuario();
    }
    
    public void login(MensajeLogin mensaje){
        Usuario usuario = mensaje.getUsuario();
        
        MensajeLoginRespuesta mensajeLoginRespuesta = new MensajeLoginRespuesta();
        try {
            if(dataMapperUsuario.loginCorrecto(usuario)){
                
                Usuario usuarioConectado = dataMapperUsuario.getUsuarioByNombreUsuario(usuario.getNombreUsuario());
                
                mensajeLoginRespuesta.setTipoRespuestaLogin(TipoRespuestaLogin.OK);
                mensajeLoginRespuesta.setUsuario(usuarioConectado);
                
                //registramos al usuario que se acaba de conectar en el servidor central
                sirviente.getServidorChat().registraCliente((SirvienteLector) sirviente, ((SirvienteLector) sirviente).getSirvienteEscritor(), usuarioConectado);
                
                //asignamos a nuestros sirvientes el usuario que se acaba de conectar
                sirviente.setUsuario_(usuarioConectado);
                ((SirvienteLector) sirviente).getSirvienteEscritor().setUsuario_(usuarioConectado);

                //Construimos el mensaje con la lista de los usuarios conectados
                MensajeUsuariosConectados mensajeUsuariosConectados = new MensajeUsuariosConectados();
                //obtenemos todos los objetos usuarios logeados en el servidor
                ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
                for(Usuario u : sirviente.getServidorChat().getSirvientesLector().keySet()){
                    usuarios.add(u);
                }
                //asignamos todos los usuarios conectados al sistema al mensaje
                mensajeUsuariosConectados.setUsuarios(usuarios);

                //Informamos al usuario que se acaba de conectar de los usuarios que ya habia conectados
                ((SirvienteLector) sirviente).getSirvienteEscritor().insertaMensaje(mensajeUsuariosConectados);
                
                //informamos al resto de usuarios de que ha entrado un nuevo usuario
                this.usuarioConectado(usuarioConectado);
                
                //asignamos un color de escritura al usuario
                HashMap<Usuario, Color> coloresAsignados;
                coloresAsignados = sirviente.getServidorChat().getColoresAsignados();
                Color colorAleatorio = sirviente.getServidorChat().dameColorAleatorio();
                coloresAsignados.put(usuarioConectado, colorAleatorio);
                sirviente.getServidorChat().getColoresDisponibles().remove(colorAleatorio);
            }else{
                mensajeLoginRespuesta.setTipoRespuestaLogin(TipoRespuestaLogin.ERROR);
                
            }
                
            //enviamos el mensaje de respuesta al usuario
            ((SirvienteLector)sirviente).getSirvienteEscritor().insertaMensaje(mensajeLoginRespuesta);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
    public void registro(MensajeRegistro mensaje){
        Usuario usuario = new Usuario();
        MensajeRegistroRespuesta mensajeRegistroRespuesta = new MensajeRegistroRespuesta();
        
        
        if(!mensaje.getPassword().equals(mensaje.getRepeatPassword())){
            mensajeRegistroRespuesta.setTipoRespuesta(TipoRespuestaRegistro.ERROR);
            mensajeRegistroRespuesta.setTextoRespuesta("Las contraseñas no son iguales");
            
            
            ((SirvienteLector)sirviente).getSirvienteEscritor().insertaMensaje(mensajeRegistroRespuesta);
                
            
            return;
        }
        
        usuario.setEmail(mensaje.getEmail());
        usuario.setNombreUsuario(mensaje.getLogin());
        usuario.setPassword(mensaje.getPassword());
        
        try {
            if(dataMapperUsuario.existeUsuario(usuario)){
                mensajeRegistroRespuesta.setTipoRespuesta(TipoRespuestaRegistro.ERROR);
                mensajeRegistroRespuesta.setTextoRespuesta("El usuario ya existe");
                
                ((SirvienteLector)sirviente).getSirvienteEscritor().insertaMensaje(mensajeRegistroRespuesta);
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            usuario.setHashConfirmacion(generaHashConfirmacion(40));
            if(!dataMapperUsuario.insertaUsuario(usuario)){
                mensajeRegistroRespuesta.setTipoRespuesta(TipoRespuestaRegistro.ERROR);
                mensajeRegistroRespuesta.setTextoRespuesta("Error al introducir el usuario en base de datos");
                
                ((SirvienteLector)sirviente).getSirvienteEscritor().insertaMensaje(mensajeRegistroRespuesta);
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mensajeRegistroRespuesta.setTipoRespuesta(TipoRespuestaRegistro.OK);
        mensajeRegistroRespuesta.setTextoRespuesta("Usuario registrado correctamente");
        
        ((SirvienteLector)sirviente).getSirvienteEscritor().insertaMensaje(mensajeRegistroRespuesta);
        
        enviaCorreoConfirmacion(usuario);
    }
    
    public void usuariosConectados(){
        MensajeUsuariosConectados mensaje = new MensajeUsuariosConectados();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        
        for(Usuario usuario : sirviente.getServidorChat().getSirvientesLector().keySet() ){
            usuarios.add(usuario);
        }
        
        mensaje.setUsuarios(usuarios);
        
        for(SirvienteEscritor sirvienteEscritor : sirviente.getServidorChat().getSirvientesEscritor().values() ){
            sirvienteEscritor.insertaMensaje(mensaje);
        }
    }
    
    public void usuarioDesconectado(Usuario usuario){
        //Si el usuario no estaba conectado no hacemos nada
        if(usuario == null)
            return;
        
        //construimos un mensaje de actualizacion
        MensajeActualizacionUsuariosConectados mensaje = new MensajeActualizacionUsuariosConectados();
        mensaje.setUsuario(usuario);
        mensaje.setEstadoConexion(EstadoConexion.DESCONECTADO);
        
        
        //informamos al resto de usuarios de que se ha desconectado el usuario
        for(SirvienteEscritor sirvienteEscritor : sirviente.getServidorChat().getSirvientesEscritor().values()){
            sirvienteEscritor.insertaMensaje(mensaje);
        }
  
        
    }
    
    public void usuarioConectado(Usuario usuario){
        
        if(usuario == null)
            return;
        
        //construimos un mensaje de actualizacion
        MensajeActualizacionUsuariosConectados mensaje = new MensajeActualizacionUsuariosConectados();
        mensaje.setUsuario(usuario);
        mensaje.setEstadoConexion(EstadoConexion.CONECTADO);
        
        
        //informamos al resto de usuarios de que se ha conectado el usuario
        for(SirvienteEscritor sirvienteEscritor : sirviente.getServidorChat().getSirvientesEscritor().values()){
            
            if(! sirvienteEscritor.getUsuario_().equals(usuario))
                sirvienteEscritor.insertaMensaje(mensaje);
        }
    }
    
    private void enviaCorreoConfirmacion(Usuario usuario){
        try
        {
            // Propiedades de la conexión
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "alberfd@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cuentas@chatalberto.com"));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(usuario.getEmail()));
            message.setSubject("Activacion de cuenta");
            String contenidoEmail = 
            "Para activar su cuenta pulse sobre el siguiente enlace: <br>" +
            "<a href=\"localhost/activacion.php?hash=" + usuario.getHashConfirmacion() +"\">Enlace</a>";
            message.setContent(contenidoEmail, "text/html");

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("alberfd@gmail.com", "311205Alber");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private String generaHashConfirmacion(int longitud){
        String result = "";
        
        long semilla = new java.util.GregorianCalendar().getTimeInMillis();
        Random aleatorio = new Random(semilla);
        
        int i = 0;
        char caracter;
        while(i < longitud){
            caracter = (char)aleatorio.nextInt(255);
            
            if ( (caracter >= '0' && caracter <='9') || (caracter >='A' && caracter <='Z') ){
                result += caracter;
                i++;
            }
            
        }
        
        return result;
    }
    
    
    
}
