/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author haishand
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppFrame frame = new AppFrame();
        SwingUtilities.invokeLater(new Runnable() {

//            @Override
            public void run() {
                                 
                //The following code ensure the window shown on the center of screen.  
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
                frame.setLocation((dim.width - frame.getWidth()) / 2, (dim.height - frame.getHeight()) / 2); 
                
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                frame.setResizable(false);
                frame.setVisible(true);  
                
            }
        });
 
    }
    
}
