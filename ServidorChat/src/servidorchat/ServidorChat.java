/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import java.awt.Color;
import mensajes.Mensaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import mensajes.MensajeChatPublico;
import mensajes.MensajeRegistro;
import modelo.Usuario;

/**
 *
 * @author Alberto
 */
public class ServidorChat {

    ServerSocket serverSocket;
    Socket fromClientSocket;
    int portNumber = 5555;
   
    HashMap<Usuario,SirvienteLector> sirvientesLector;
    HashMap<Usuario,SirvienteEscritor> sirvientesEscritor;
    
    HashMap<Usuario, Color> coloresAsignados;
    ArrayList<Color> coloresDisponibles;
    ArrayList<Color> colores;
    
    public ServidorChat() throws IOException{
        sirvientesLector = new HashMap<Usuario, SirvienteLector>();
        sirvientesEscritor = new HashMap<Usuario, SirvienteEscritor>();
        serverSocket = new ServerSocket(portNumber);
        
        cargaColores();
        coloresDisponibles = new ArrayList<>(colores);
        coloresAsignados = new HashMap<Usuario, Color>();
        
        System.out.println("Servidor arrancado en el puerto " + portNumber);
    }
    
    public void run() throws IOException{
        
        
        while(true){
            fromClientSocket = serverSocket.accept();
            ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(fromClientSocket.getInputStream());
            SirvienteLector sirvienteLector = new SirvienteLector(this, fromClientSocket, oos, ois);
            SirvienteEscritor sirvienteEscritor = new SirvienteEscritor(this, fromClientSocket, oos, ois);
            
            //arrancamos los dos hilos que se encargaran de atender al usuario
            //uno para leer y el otro para escribir en el socket
            arrancaSirviente(sirvienteEscritor);
            arrancaSirviente(sirvienteLector);
            
            //hacemos que los dos sirvientes se conozcan entre ellos
            sirvienteEscritor.setSirivienteLector(sirvienteLector);
            sirvienteLector.setSirvienteEscritor(sirvienteEscritor);

        }
    }
    
    public void arrancaSirviente(Sirviente sirviente){
        new Thread(sirviente).start();
    }
    
    public void registraCliente(SirvienteLector sirvienteLector, SirvienteEscritor sirvienteEscritor, Usuario usuario){
        sirvientesLector.put(usuario,sirvienteLector);
        sirvientesEscritor.put(usuario, sirvienteEscritor);
        
        System.out.println("Numero de sirvientes lectores " + sirvientesLector.size());
        System.out.println("Numero de sirvientes escritores " + sirvientesEscritor.size());
    }
    
    public void eliminaCliente(Usuario usuario){
        
        sirvientesLector.remove(usuario);
        sirvientesEscritor.remove(usuario);
        
        Color colorUsuario = coloresAsignados.get(usuario);
        //eliminamos la asignacion del color del chat al usuario
        coloresAsignados.remove(usuario);
        
        //solo si no quedan usuarios conectados con el mismo color que el usuario
        //que se acaba de desconectar lo agragamos a la lista de colores disponibles
        if(!coloresAsignados.containsValue(colorUsuario))
            coloresDisponibles.add(colorUsuario);
        

        System.out.println("Numero de sirvientes lectores " + sirvientesLector.size());
        System.out.println("Numero de sirvientes escritores " + sirvientesEscritor.size());
    }
    
    public HashMap<Usuario,SirvienteEscritor> getSirvientesEscritor(){
        return this.sirvientesEscritor;
    }
    
    public HashMap<Usuario,SirvienteLector> getSirvientesLector(){
        return this.sirvientesLector;
    }
    
    private void cargaColores(){
        Color color;
        colores = new ArrayList<Color>();
        
        color = new Color(192, 57, 43);
        colores.add(color);
        color = new Color(231, 76, 60);
        colores.add(color);
        color = new Color(155, 89, 182);
        colores.add(color);
        color = new Color(142, 68, 173);
        colores.add(color);
        color = new Color(41, 128, 185);
        colores.add(color);
        color = new Color(52, 152, 219);
        colores.add(color);
        color = new Color(26, 188, 156);
        colores.add(color);
        color = new Color(22, 160, 133);
        colores.add(color);
        color = new Color(39, 174, 96);
        colores.add(color);
        color = new Color(46, 204, 113);
        colores.add(color);
        color = new Color(241, 196, 15);
        colores.add(color);
        color = new Color(243, 156, 18);
        colores.add(color);
        color = new Color(230, 126, 34);
        colores.add(color);
        color = new Color(211, 84, 0);
        colores.add(color);
        color = new Color(149, 165, 166);
        colores.add(color);
        color = new Color(52, 73, 94);
        colores.add(color);
        
    }
    
    public Color dameColorAleatorio(){
        int numeroColoresDisponibles;
        int aleatorio;
        
        if(!coloresDisponibles.isEmpty())
            numeroColoresDisponibles = coloresDisponibles.size();
        else
            numeroColoresDisponibles = colores.size();
        
        
        aleatorio = (int) (Math.random()*numeroColoresDisponibles);
        
        if(!coloresDisponibles.isEmpty())
            return coloresDisponibles.get(aleatorio);
        else
            return colores.get(aleatorio);
        
    }

    public HashMap<Usuario, Color> getColoresAsignados() {
        return coloresAsignados;
    }

    public void setColoresAsignados(HashMap<Usuario, Color> coloresAsignados) {
        this.coloresAsignados = coloresAsignados;
    }

    public ArrayList<Color> getColoresDisponibles() {
        return coloresDisponibles;
    }

    public void setColoresDisponibles(ArrayList<Color> coloresDisponibles) {
        this.coloresDisponibles = coloresDisponibles;
    }

    public ArrayList<Color> getColores() {
        return colores;
    }

    public void setColores(ArrayList<Color> colores) {
        this.colores = colores;
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        ServidorChat servidorChat = new ServidorChat();
        servidorChat.run();
          
    }
    
}
