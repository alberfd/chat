/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensajes.Mensaje;

/**
 *
 * @author Alberto
 */
public class SirvienteEscritor extends Sirviente implements Runnable {
    
    SirvienteLector sirivienteLector;
    Queue<Mensaje> mensajes;
    
    public SirvienteEscritor(ServidorChat servidorChat, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException{
        super(servidorChat, clientSocket, oos, ois);
        
        mensajes = new LinkedList<Mensaje>();
    }
    
    @Override
    public void run() {
        Mensaje mensaje;
        
        while(true){
            
            synchronized(mensajes){
                
                if(mensajes.size() == 0){
                    try {
                        mensajes.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SirvienteEscritor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    
                mensaje = mensajes.poll();
                
                
            }
            
            
            if(mensaje != null){
                try {
                    oos.writeObject(mensaje);
                } catch (IOException ex) {
                    Logger.getLogger(SirvienteEscritor.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
                
            
        }
        
        
    }
    
    public boolean insertaMensaje(Mensaje mensaje){   
        boolean response = mensajes.add(mensaje);
        
        synchronized(mensajes){
            mensajes.notify();
        }
        
        return response;
        
    }

    public SirvienteLector getSirivienteLector() {
        return sirivienteLector;
    }

    public void setSirivienteLector(SirvienteLector sirivienteLector) {
        this.sirivienteLector = sirivienteLector;
    }

    public Queue<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(Queue<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
    
    
    
}
