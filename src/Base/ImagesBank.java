package Base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImagesBank {
    public static final int IMAGE_COUNT = 9;
    public Image images[] = new Image[IMAGE_COUNT];

    public ImagesBank() {
        loadImages();
    }

    private void loadImages() {
        for(int i = 0; i < IMAGE_COUNT; ++i)
            images[i] = loadImageFromFile("images/" + i + ".png");
    }

    private Image loadImageFromFile(String fileName) {
        File file = new File(fileName);

        try {
            return ImageIO.read(file);
        } catch(IOException e) {
            System.out.println(e.toString());
        }

        return null;
    }

    public int getImageWidth() {
        return images[0].getWidth(null);
    }

    public int getImageHeight() {
        return images[0].getHeight(null);
    }
}
