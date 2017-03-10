/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import java.awt.Color;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alberto
 */
public class Mensaje implements Serializable{
    Date dateTime;
    Color colorNombreUsuario;
    
    public Mensaje(){

    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Color getColorNombreUsuario() {
        return colorNombreUsuario;
    }

    public void setColorNombreUsuario(Color colorNombreUsuario) {
        this.colorNombreUsuario = colorNombreUsuario;
    }
       
}
