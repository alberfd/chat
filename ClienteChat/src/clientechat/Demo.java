/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import java.util.logging.Level;
import java.util.logging.Logger;

class Demo {
    public static void main(String[] args) {
   Thread t = new Thread(
                 new Runnable() {
                     public void run () {
                         //do something
                     }
                  }
    );
    Thread t1 = new Thread(
                 new Runnable() {
                     public void run () {
                         try {
                             setDae
                         } catch (InterruptedException ex) {
                             Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                  }
    );
    t.start(); // Line 15
        try {
            t.join();  // Line 16
        } catch (InterruptedException ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
    t1.start();
    t.
    }
}