/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clientechat.FXApplicationMain;

/**
 *
 * @author Alberto
 */
public abstract class Controlador {
    FXApplicationMain app;

    public FXApplicationMain getApp() {
        return app;
    }

    public void setApp(FXApplicationMain app) {
        this.app = app;
    }
    
    
}
