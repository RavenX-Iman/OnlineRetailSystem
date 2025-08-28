package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SplashScreenPanel extends JPanel {
    private Image logo;

    public SplashScreenPanel() {
        setOpaque(true); // Important to fully paint over any background
        setBackground(Color.BLACK); // Optional fallback

        String path = "C:\\Users\\SL\\Desktop\\OnlineRetailSystem\\CyanityLogo.png";
        File file = new File(path);

        if (file.exists()) {
            logo = new ImageIcon(path).getImage();
        } else {
            System.err.println("Splash logo image not found at: " + path);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (logo != null) {
            // Stretch image to fill panel
            g.drawImage(logo, 0, 0, getWidth(), getHeight(), this);
        }

        // Optional: Title
        // g.setColor(Color.CYAN);
        // g.setFont(new Font("SansSerif", Font.BOLD, 28));
        // String title = "CYANITY";
        // FontMetrics fm = g.getFontMetrics();
        // int titleWidth = fm.stringWidth(title);
        // g.drawString(title, (getWidth() - titleWidth) / 2, getHeight() - 40);
    }
}
