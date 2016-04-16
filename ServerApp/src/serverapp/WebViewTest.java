/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.awt.Frame;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author haishand
 */
public class WebViewTest extends Application{

    private Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("位置");
        scene = new Scene(new Browser(), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        new WebViewTest().run();
    }
    public void run() {
//        new WebViewTest().run();
             SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }

    private void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("位置");
        final JFXPanel fxPanel = new JFXPanel();
//        frame.setLocationRelativeTo(f);
        frame.add(fxPanel);
        frame.setSize(750, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
       });
    }

    private void initFX(JFXPanel fxPanel)  {
        // This method is invoked on the JavaFX thread
        Scene scene;
//        Group root = new Group();
        try {
            scene = new Scene(new Browser(), 750, 500, Color.web("#666970"));
//            root.getChildren().add(new Browser());
            fxPanel.setScene(scene);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WebViewTest.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Scene scene = createScene();
        
    }
}

class Browser extends Region {
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    public Browser() throws MalformedURLException {
        getStyleClass().add("browser");
        
        File file = new File("map.html");
        String url = file.toURL().toString();
//        System.out.println(url);
        webEngine.load(url);
        getChildren().add(browser);
    }
    
}
