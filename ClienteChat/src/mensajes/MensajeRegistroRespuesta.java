/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes;

import modelo.TipoRespuestaRegistro;
import java.io.Serializable;

/**
 *
 * @author Alberto
 */
public class MensajeRegistroRespuesta extends Mensaje implements Serializable {
    
    TipoRespuestaRegistro tipoRespuesta;
    
    String textoRespuesta;
    
    public MensajeRegistroRespuesta(){
        
    }

    public TipoRespuestaRegistro getTipoRespuesta() {
        return tipoRespuesta;
    }

    public void setTipoRespuesta(TipoRespuestaRegistro tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }
    
    
    
}
