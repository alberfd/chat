/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import servidorchat.Sirviente;

/**
 *
 * @author Alberto
 */
public abstract class Controlador {
    Sirviente sirviente;
    
    
    
    public Controlador(Sirviente sirviente){
        
        this.sirviente = sirviente;
  
    }
}
