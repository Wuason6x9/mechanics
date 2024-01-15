package dev.wuason.mechanics.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AsciiUtils {
    /**
     * Creates a BufferedImage containing the specified text rendered with the given font and color.
     *
     * @param text  the text to be rendered
     * @param font  the font used for rendering the text
     * @param color the color used for rendering the text
     * @return a BufferedImage containing the rendered text
     */
    public static BufferedImage createTextImage(String text, Font font, Color color) {
        // Calcula el tamaño de la imagen
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummyImage.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        // Crea la imagen final con el texto
        BufferedImage textImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = textImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(color);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();

        return textImage;
    }

    /**
     * Converts a BufferedImage to ASCII art.
     *
     * @param image The BufferedImage to convert. Cannot be null.
     * @return The ASCII art representation of the image as a StringBuilder.
     */
    public static StringBuilder convertToAscii(BufferedImage image) {
        StringBuilder asciiArt = new StringBuilder();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha == 0) { // Pixel transparente
                    asciiArt.append(" ");
                } else {
                    asciiArt.append("#"); // Puedes mejorar la conversión con una escala de grises
                }
            }
            asciiArt.append("\n");
        }
        return asciiArt;
    }
}
