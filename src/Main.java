import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class Main {


        public static void main(String[] args) throws IOException{
            ImageHandler imageHandler = new ImageHandler("./images/psycho.jpg");
            BufferedImage grayImage = imageHandler.getGrayImage();
            BufferedImage histEqualed = imageHandler.histogramEqualization();
            BufferedImage tempImage = imageHandler.linearConversion(grayImage);
            File outputImage = new File("./images/grayImage.jpg");
            File histEqualedFile = new File("./images/histgramed.jpg");
            File tempFile = new File("./images/linearConverged.jpg");
            try {
                ImageIO.write(grayImage,"jpg",outputImage);
                ImageIO.write(histEqualed,"jpg",histEqualedFile);
                ImageIO.write(tempImage,"jpg",tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Use a label to display the image
            JFrame frame = new JFrame();
            JLabel label = new JLabel(new ImageIcon(grayImage));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
            //关闭窗口--退出调试
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }
}

