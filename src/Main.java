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

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ImageHandler imageHandler = new ImageHandler("./images/self.jpg");
                    BufferedImage grayImage = imageHandler.getGrayImage();
                    BufferedImage histEqualed = imageHandler.histogramEqualization();
                    BufferedImage tempImage = imageHandler.linearConversion(grayImage);
                    File outputImage = new File("./images/grayImage.jpg");
                    File histEqualedFile = new File("./images/historgramed.jpg");
                    File tempFile = new File("./images/temp.jpg");
                    try {
                        ImageIO.write(grayImage,"jpg",outputImage);
                        ImageIO.write(histEqualed,"jpg",histEqualedFile);
                        ImageIO.write(tempImage,"jpg",tempFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    JFrame frame = new ImageViewerFrame();
//                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    frame.setVisible(true);
                }
            });

//            System.out.println("Please input the path of the image: ");
//            Scanner in = new Scanner(System.in);
//            String imagePath = in.nextLine();
//
//            File input = new File(imagePath);
//
//            BufferedImage imageReader = ImageIO.read(input);
//
//            int h = imageReader.getHeight();
//            int w = imageReader.getWidth();



        }
}


class ImageViewerFrame extends JFrame {
    private JLabel label;
    private JFileChooser chooser;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 400;

    public ImageViewerFrame(){
        setTitle("ImageViewer");
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        label = new JLabel();
        add(label);
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        JMenuBar menuBar= new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem openItem = new JMenuItem("Open");
        menu.add(openItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result = chooser.showOpenDialog(null);
                if (result==JFileChooser.APPROVE_OPTION){
                    String name = chooser.getSelectedFile().getPath();
                    label.setIcon(new ImageIcon(name));
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

    }

}