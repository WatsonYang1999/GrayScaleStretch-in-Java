import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImageProcessing {

    /**
     * @param args
     */

    static Image tmp;
    static int iwidth,iheight;//图像宽度，高度
    static double ma,mi;//线性变化灰度上下限
    static int[] pixels;//图像所有像素点
    static int[] pixels2;//备份pixels，用于灰度线性变化
    static int[] pixels3;//备份pixels，用于灰度拉伸
    static int[] histogram=new int[256];
    static String filename=null,directory=null,fileFormat=null;//要变换的图像路径名+文件名
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO Auto-generated method stub

        directory="./images";//读入文件路径
        filename="grayImage";//读入文件名
        fileFormat="jpg";
        grayImage();//灰度化
        histogramEqualization();//均衡化并输出

        mi=50;

        ma=150;
        linearConversion();
        grayStretch();
    }

    //灰度化
    public static void grayImage() throws IOException, InterruptedException
    {
        File input=new File(directory+"/"+filename+"."+fileFormat);
        BufferedImage reader=ImageIO.read(input);//图片文件读入流

        iwidth=reader.getWidth();
        iheight=reader.getHeight();
        pixels=new int[iwidth*iheight];
        pixels2=new int[iwidth*iheight];
        pixels3=new int[iwidth*iheight];

        BufferedImage grayImage=new BufferedImage(iwidth,iheight,BufferedImage.TYPE_BYTE_GRAY);//无符号 byte 灰度级图像
        for(int i=0;i<iwidth;i++)
            for(int j=0;j<iheight;j++)
            {
                int rgb=reader.getRGB(i, j);
                int grey=(int) ((0.3*((rgb&0xff0000)>>16)+0.59*((rgb&0xff00)>>8))+0.11*((rgb&0xff)));
                rgb=255<<24|grey<<16|grey<<8|grey;
                grayImage.setRGB(i, j, rgb);
            }//读入所有像素，转换图像信号,使其灰度化
        tmp=grayImage;
        PixelGrabber pg=new PixelGrabber(tmp, 0, 0, iwidth, iheight, pixels,0,iwidth);
        pg.grabPixels();//将该灰度化后的图片所有像素点读入pixels数组
        PixelGrabber pg2=new PixelGrabber(tmp, 0, 0, iwidth, iheight, pixels2,0,iwidth);
        pg2.grabPixels();
        PixelGrabber pg3=new PixelGrabber(tmp, 0, 0, iwidth, iheight, pixels3,0,iwidth);
        pg3.grabPixels();//
    }

    //直方图均衡
    public static void histogramEqualization() throws InterruptedException, IOException
    {
        //PixelGrabber pg=new PixelGrabber(tmp, 0, 0, iwidth, iheight, pixels,0,iwidth);
        //pg.grabPixels();
        BufferedImage greyImage=new BufferedImage(iwidth, iheight, BufferedImage.TYPE_BYTE_GRAY);

        for(int i=0;i<iheight-1;i++)
            for(int j=0;j<iwidth-1;j++)
            {
                int grey=pixels[i*iwidth+j]&0xff;
                histogram[grey]++;
            }//计算每一个灰度级的像素数
        double a=(double)255/(iwidth*iheight);
        double[] c=new double[256];
        c[0]=(a*histogram[0]);
        for(int i=1;i<256;i++)
            c[i]=c[i-1]+(int)(a*histogram[i]);//直方图均衡化(离散情况)
        for(int i=0;i<iheight;i++)
            for(int j=0;j<iwidth;j++)
            {
                int grey=pixels[i*iwidth+j]&0x0000ff;
                int hist=(int)c[grey];
                pixels[i*iwidth+j]=255<<24|hist<<16|hist<<8|hist;
                greyImage.setRGB(j, i, pixels[i*iwidth+j]);
            }
        tmp=greyImage;
        File f=new File(directory+"/s"+"histogramed.jpg");
        ImageIO.write(greyImage, "jpg", f);//在原路径下输出均衡化后的图像
    }

    //灰度线性变换
    public static void linearConversion() throws IOException
    {
        int min=255,max=0;
        for(int i=0;i<256;i++)
        {
            if(histogram[i]>0)
            {
                if(i<min)
                    min=i;
                if(i>max)
                    max=i;
            }
        }//找出灰度的最大级和最小级
        double k=(ma-mi)/(max-min);//计算变换比
        BufferedImage greyImage=new BufferedImage(iwidth, iheight, BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<iheight;i++)
            for(int j=0;j<iwidth;j++)
            {
                int grey=pixels2[i*iwidth+j]&0xff;
                grey=(int)(k*(grey-min)+mi);
                if(grey>255)
                    grey=255;
                if(grey<0)
                    grey=0;
                pixels2[i*iwidth+j]=255<<24|grey<<16|grey<<8|grey;
                greyImage.setRGB(j, i, pixels2[i*iwidth+j]);
            }//灰度线性变换
        File f=new File(directory+"/"+"conversioned.jpg");
        ImageIO.write(greyImage, "jpg", f);//在原路径下输出均衡化后的图像
    }

    //灰度拉伸
    public static void grayStretch() throws IOException
    {
        int min = 0,max = 1;
        int sum=0;
        for(int i=0;i<256;i++)
        {
            sum+=histogram[i];
            if(sum>iwidth*iheight*0.05)
            {
                min=i;
                break;
            }
        }//找出灰度的大部分像素范围的最小级
        sum=0;
        for(int i=255;i>=0;i--)
        {
            sum+=histogram[i];
            if(sum>iwidth*iheight*0.05)
            {
                max=i;
                break;
            }
        }//找出灰度的大部分像素范围的最大级
        double k=(ma-mi)/(max-min);
        BufferedImage greyImage=new BufferedImage(iwidth, iheight, BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0;i<iheight;i++)
            for(int j=0;j<iwidth;j++)
            {
                int grey=pixels3[i*iwidth+j]&0xff;
                if(grey<min)
                    grey=(int) mi;//小于min部分设为下限
                else if(grey>=max)
                    grey=(int) ma;//大于max部分设为上限
                else
                {
                    grey=(int)(k*(grey-min)+mi);
                    if(grey>255)
                        grey=255;
                    if(grey<0)
                        grey=0;
                }//大部分区域线性变换

                pixels3[i*iwidth+j]=255<<24|grey<<16|grey<<8|grey;
                greyImage.setRGB(j, i, pixels3[i*iwidth+j]);
            }//灰度拉伸
        File f=new File(directory+"\\"+"灰度拉伸.jpg");
        ImageIO.write(greyImage, "jpg", f);//在原路径下输出拉伸后的图像
    }
}