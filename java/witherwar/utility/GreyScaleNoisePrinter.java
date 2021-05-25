package witherwar.utility;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GreyScaleNoisePrinter {
    //just convinence methods for debug
	
	
	

    public static void greyWriteImage(float[][] data){
        //this takes and array of doubles between 0 and 1 and generates a grey scale image from them

        BufferedImage image = new BufferedImage(data.length,data[0].length, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < data[0].length; y++)
        {
          for (int x = 0; x < data.length; x++)
          {
            if (data[x][y]>1){
                data[x][y]=1;
            }
            if (data[x][y]<0){
                data[x][y]=0;
            }
              Color col=new Color( data[x][y] ,data[x][y] ,data[x][y]); 
            image.setRGB(x, y, col.getRGB());
          }
        }

        try {
            // retrieve image
            File outputfile = new File("C:\\Users\\Guiltygate\\Documents\\mc_work\\old_setup\\wither_war\\saved.png");
            outputfile.createNewFile();

            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
          System.out.println("Failed to write.");
        }
    }


//    public static void main(String args[]){
//        double[][] data=new double[2][4];
//        data[0][0]=0.5;
//        data[0][5]=1;
//        data[1][0]=0.7;
//        data[1][6]=1;
//
//        greyWriteImage(data);
//    }
}
	
	

