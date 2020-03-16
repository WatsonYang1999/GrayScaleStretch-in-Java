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

        BufferedImage grayImage = new BufferedImage(width,height,image.getType());


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



    public BufferedImage linearConversion(BufferedImage input) {
        double min = 0;
        double max = 128;
        int val;
        BufferedImage output = new BufferedImage(input.getWidth(),input.getHeight(),input.getType());
        for(int i=0;i<input.getWidth();i++) {
            for(int j=0;j<input.getHeight();j++) {
                val = input.getRGB(i,j)&0xff;
                val = (int)((max-min)/256*val+min);
                output.setRGB(i,j,0xff000000|val<<16|val<<8|val);
            }
        }
        return output;
    }

    public BufferedImage histogramEqualization() {
        BufferedImage grayImage = linearConversion(getGrayImage());

        //统计不同灰度的像素个数
        int[] count = new int[256];
        for(int i=0;i<grayImage.getWidth();i++) {
            for(int j=0;j<grayImage.getHeight();j++) {
                count[grayImage.getRGB(i,j)&0xff]++;
            }
        }

        double[] histogram = new double[256];
        int N = grayImage.getHeight() * grayImage.getWidth();
        for(int i=0;i<count.length;i++) {
            histogram[i] = (double)count[i] / N;
        }

        //开始均衡化处理
        for(int i=1;i<histogram.length;i++) {
            histogram[i] += histogram[i-1];
        }
        System.out.println(histogram[100]);
        System.out.println(histogram[255]);
        //求出新的图像的灰度值
        BufferedImage outputImage = new BufferedImage(width,height,image.getType());

        int grayVal,newVal;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                grayVal = grayImage.getRGB(i,j)&0xff;
                newVal = (int)(histogram[grayVal]*255) & 0xff;
                outputImage.setRGB(i,j,0xff000000|newVal<<16|newVal<<8|newVal);
            }
        }

        return outputImage;
    }
}
