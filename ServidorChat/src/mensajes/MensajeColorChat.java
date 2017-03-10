/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Alberto
 */
public class MensajeColorChat extends Mensaje implements Serializable {
    
    Color color;
    
    public MensajeColorChat(){
        
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
    
}
