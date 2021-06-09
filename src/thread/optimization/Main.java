package thread.optimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private static final String SOURCE_FILE = "./resources/flowers.jpeg";
    private static final String DESTINATION_FILE = "./out/flowers.jpeg";

    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        int numberOfThreads = 6;
        long startTime = System.currentTimeMillis();
        recolorSingleThreaded(originalImage, resultImage);
        long endTime = System.currentTimeMillis() - startTime;

        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpeg", outputFile);

        System.out.print("Threads: " + numberOfThreads + "\nTime: " + String.valueOf(endTime));
    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) throws InterruptedException {
        var threads = new ArrayList<Thread>(numberOfThreads);
        var width = originalImage.getWidth();
        var height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++){
            final int multiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * multiplier;
                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });
            threads.add(thread);
        }

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }
    public static void recolorSingleThreaded(BufferedImage originalImage,
                                             BufferedImage resultImage){
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }
    public static void recolorImage(BufferedImage originalImage,
                                    BufferedImage resultImage,
                                    int leftCorner,
                                    int topCorner,
                                    int width,
                                    int height){
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++){
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++){
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage,
                                    int x, int y){
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red, green, blue)){
            newRed = Math.min(255, red + 10);
            newGreen = Math.min(255, green - 80);
            newBlue = Math.min(255, blue - 20);
        }
        else{
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        resultImage.getRaster().setDataElements(x,y, resultImage.getColorModel().getDataElements(newRGB,null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue){
        return Math.abs(red -green) < 30  && Math.abs(red - blue) < 30
                && Math.abs(green - blue) < 30;
    }
    public static int createRGBFromColors(int red, int green, int blue){
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;
        return rgb;
    }
    public static int getRed(int rgb){
        return (rgb & 0X00FF0000) >> 16;
    }
    public static int getGreen(int rgb){
        return (rgb & 0x0000FF00) >> 8;
    }
    public static int getBlue(int rgb){
        return rgb & 0x000000FF;
    }
}
