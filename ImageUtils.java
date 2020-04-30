// Created by Brendan C Reidy
// Created on 4/22/2020
// Last modified 4/29/2020
// Useful utils for buffered images.

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    private static JFrame frame;
    private static JLabel label;

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage drawCircle(BufferedImage img, Color color, int posX, int posY, int radius)
    {
        Color colorB = new Color(100,150,255); // Default circle color (also color of the outline)
        if(color==null) // If no specified color, use outline color as main color
            color = colorB;
        int rThresh = radius * 8/40; // Used to draw an outline around the circle
        float detailFactor = 1; // Used to draw the circle with more detail [Higher number = more detail = longer render time] (useful for large circles where there are lines at angles between whole number degrees)
        for(int theta = 0; theta<360*detailFactor; theta++) // Do for every degree of angle theta
        {
            for(int r=0; r<radius; r++) // Draw line from middle of circle out to radius
            {
                float fTheta = (float) Math.toRadians(theta/detailFactor); // Get theta as floating point
                int x = (int) (Math.cos(fTheta)*r) + posX; // Get pos X of line
                int y = (int) (Math.sin(fTheta)*r) + posY; // Get pos Y of line
                if(r>=radius-rThresh) // Draw outline of circle if the line is approaching edge of circle (outline is 1/5th of total circle)
                    img.setRGB(x, y, colorB.getRGB()); // Draw outline
                else
                    img.setRGB(x, y, color.getRGB()); // Draw inner circle
            }
        }
        return img;
    }

    public static BufferedImage drawLine(BufferedImage image, Color color, int thickness, int x1, int y1, int x2, int y2)
    {
        if(color==null)
            color = new Color(0,0,0); // Default color
        int sizeX = Math.abs(x2-x1); // Length of leg A (pixels)
        int sizeY = Math.abs(y2-y1); // Length of leg B (pixels)
        int hyp = (int) Math.sqrt(sizeX*sizeX + sizeY*sizeY); // Length of hypotenuse (pixels)
        float theta = getAngle(x1, y1, x2, y2); // Angle of triangle
        theta = (float) Math.toRadians(theta); // Change to radians
        float normal = (float) Math.toRadians(90) + theta; // Get normal line (for adding thickness to line)

        for(int r=0; r<hyp; r++)
        {
            float length = ((float)r); // Not add a multiplier to for loop and divisor here to make line more high definition
            float x = (float) (Math.cos(theta) * length) + x1; // X coordinate of pixel
            float y = (float) (Math.sin(theta) * length) + y1; // Y coordinate of pixel
            for(int t = 0; t<thickness; t++) { // Thicken pixel base on normal line (draws parallel lines above and below current line)
                float floatOffset = t;
                float offsetX = (float) (Math.cos(normal)*floatOffset); // x coordinate of one pixel in line (along the normal line)
                float offsetY = (float) (Math.sin(normal)*floatOffset); // y coordinate [...] normal line
                if(t%2==0){ // One pixel the line below, one pixel above the line, alternate every other iteration
                    offsetX=-offsetX; // Make offset negative on even iterations, positive on odd
                    offsetY=-offsetY;
                }
                image.setRGB((int) (x + offsetX/2), (int) (y+offsetY/2), color.getRGB()); // Draw parallel line
            }
            image.setRGB((int) x, (int) y, color.getRGB()); // Draw main line
        }
        return image;
    }
    public static float getAngle(int x1, int y1, int x2, int y2) { // Gets angle between two legs of triangle
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
    public static void saveImage(BufferedImage anImage, String location, String fileName) // Saves image to a file at specified directory
    {
        try {
            BufferedImage bi = anImage;
            File outputfile = new File(location + fileName);
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {

        }
    }

    public static void display(BufferedImage image){ // Displays the image as a JFrame to user
        if(frame==null){
            frame=new JFrame();
            frame.setTitle("Neural Network");
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label=new JLabel();
            label.setIcon(new ImageIcon(image));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        }else label.setIcon(new ImageIcon(image));
    }
}
