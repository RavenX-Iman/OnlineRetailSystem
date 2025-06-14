package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {

        String path = "C:\\Users\\SL\\Desktop\\OnlineRetailSystem\\Login Simple Retail Logo.png";  // Replace with your image file name
        File file = new File(path);

        if (file.exists()) {
            backgroundImage = new ImageIcon(path).getImage();
        } else {
            System.err.println("Background image not found at: " + path);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}