import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    private BufferedImage image;
    private int width;
    private int height;
    public ImageHandler(String path)  {
        File input = new File(path);
        try {
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
        }
        catch (IOException e){
            System.out.println("Wrong File Path");
        }
    }

    public BufferedImage getGrayImage() {

        BufferedImage grayImage = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);


        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                int rgb = image.getRGB(i,j);
                int r  = (rgb&0x00ff0000) >> 16;
                int g  = (rgb&0x0000ff00) >> 8 ;
                int b  = (rgb&0x000000ff);
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                rgb = 0xff << 24 | gray << 16 | gray<<8 | gray;
                grayImage.setRGB(i,j,rgb);
            }
        }
        return grayImage;
    }
}
