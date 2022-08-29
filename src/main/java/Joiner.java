import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Joiner {

    public static final int MAX_WIDTH = 8;
    public static final int MARGIN = 2;
    public static final File PATH = new File("");
    public static final File IN_PATH = new File("C:\\unity\\Murphy Defense at SibGameJam\\Assets\\Sprites\\Towers\\1 red\\red_lights_01");
    public static final File OUT_PATH = new File("C:\\unity\\Murphy Defense at SibGameJam\\Assets\\Sprites\\Towers\\1 red");

    public static void main(String[] args) throws Exception {
//        buttonWithTextAndDisabled();
//        buttonWithText();
//        buttonNoText();
//        text();
        animation();
    }


    private static void animation() throws IOException {
        List<BufferedImage> images = Files.list(IN_PATH.toPath())
                .filter(path -> path.getFileName().toString().matches(".*png"))
                .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                .map(path -> {
                    try {
                        return ImageIO.read(path.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int width = multipleOf4(images.stream().mapToInt(BufferedImage::getWidth).max().orElse(0));
        int height = multipleOf4(images.stream().mapToInt(BufferedImage::getHeight).max().orElse(0));

        int rows = (int) Math.ceil(images.size() / (double) MAX_WIDTH);
        int cols = Math.min(MAX_WIDTH, images.size());

        BufferedImage combined = new BufferedImage(multipleOf4((width + MARGIN) * cols), multipleOf4((height + MARGIN) * rows), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();


        outer:
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < MAX_WIDTH; j++) {
                int index = i * MAX_WIDTH + j;
                if (index >= images.size()) {
                    break outer;
                }
                BufferedImage img = images.get(index);
                g.drawImage(img, j * (width + MARGIN) + (width - img.getWidth()) / 2, i * (height + MARGIN) + (height - img.getHeight()) / 2, null);
            }
        }

        g.dispose();

        System.out.println("width: " + width);
        System.out.println("height: " + height);

        OUT_PATH.mkdirs();

        ImageIO.write(combined, "PNG", new File(OUT_PATH, IN_PATH.getName() + ".png"));
    }

    private static void buttonWithText() throws IOException {
        String name = "flag_button";

        BufferedImage engNormal = ImageIO.read(new File(PATH, name + "_eng_normal.png"));
        BufferedImage engClicked = ImageIO.read(new File(PATH, name + "_eng_clicked.png"));

        BufferedImage rusNormal = ImageIO.read(new File(PATH, name + "_rus_normal.png"));
        BufferedImage rusClicked = ImageIO.read(new File(PATH, name + "_rus_clicked.png"));

        int width = engNormal.getWidth();
        int height = engNormal.getHeight();

        BufferedImage combined = new BufferedImage(multipleOf4(width * 2), multipleOf4(height * 2), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(engNormal, 0, 0, null);
        g.drawImage(engClicked, width, 0, null);
        g.drawImage(rusNormal, 0, height, null);
        g.drawImage(rusClicked, width, height, null);

        g.dispose();

        ImageIO.write(combined, "PNG", new File(PATH, name + ".png"));
    }

    private static void buttonWithTextAndDisabled() throws IOException {
        String name = "flag_button";

        BufferedImage engNormal = ImageIO.read(new File(PATH, name + "_eng_normal.png"));
        BufferedImage engClicked = ImageIO.read(new File(PATH, name + "_eng_clicked.png"));
        BufferedImage engDisabled = ImageIO.read(new File(PATH, name + "_eng_disabled.png"));

        BufferedImage rusNormal = ImageIO.read(new File(PATH, name + "_rus_normal.png"));
        BufferedImage rusClicked = ImageIO.read(new File(PATH, name + "_rus_clicked.png"));
        BufferedImage rusDisabled = ImageIO.read(new File(PATH, name + "_rus_disabled.png"));

        int width = engNormal.getWidth();
        int height = engNormal.getHeight();

        BufferedImage combined = new BufferedImage(multipleOf4(width * 3), multipleOf4(height * 2), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(engNormal, 0, 0, null);
        g.drawImage(engClicked, width, 0, null);
        g.drawImage(engDisabled, width * 2, 0, null);
        g.drawImage(rusNormal, 0, height, null);
        g.drawImage(rusClicked, width, height, null);
        g.drawImage(rusDisabled, width * 2, height, null);

        g.dispose();

        ImageIO.write(combined, "PNG", new File(PATH, name + ".png"));
    }

    private static void buttonNoText() throws IOException {
        String name = "close_button";

        BufferedImage normal = ImageIO.read(new File(PATH, name + "_normal.png"));
        BufferedImage clicked = ImageIO.read(new File(PATH, name + "_clicked.png"));
        BufferedImage disabled = ImageIO.read(new File(PATH, name + "_disabled.png"));

        int width = normal.getWidth();
        int height = normal.getHeight();

        BufferedImage combined = new BufferedImage(multipleOf4(width), multipleOf4(height * 3), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(normal, 0, 0, null);
        g.drawImage(clicked, 0, height, null);
        g.drawImage(disabled, 0, height * 2, null);

        g.dispose();

        ImageIO.write(combined, "PNG", new File(PATH, name + ".png"));
    }

    private static void text() throws IOException {
        String name = "text_your_score";

        BufferedImage eng = ImageIO.read(new File(PATH, name + "_eng.png"));
        BufferedImage rus = ImageIO.read(new File(PATH, name + "_rus.png"));

        int width = Math.max(eng.getWidth(), rus.getWidth());
        int height = eng.getHeight() + rus.getHeight();

        BufferedImage combined = new BufferedImage(multipleOf4(width), multipleOf4(height), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(eng, 0, 0, null);
        g.drawImage(rus, 0, eng.getHeight(), null);

        g.dispose();

        ImageIO.write(combined, "PNG", new File(PATH, name + ".png"));
    }

    private static int multipleOf4(int value) {
        if (value % 4 == 0) {
            return value;
        }
        return value + 4 - value % 4;
    }

}
