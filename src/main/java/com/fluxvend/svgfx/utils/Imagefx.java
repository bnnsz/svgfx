/*
 * Copyright 2024 Fluxvend
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.fluxvend.svgfx.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling image operations.
 * Provides methods for loading, caching, compressing, and manipulating images.
 * Uses JavaFX and AWT libraries for image processing.
 */
public class Imagefx {
    
    private static Logger log = Logger.getLogger(Imagefx.class.getName());

    private static final Executor executor = Executors.newFixedThreadPool(5);


    /**
     * Loads an image from the specified URL with default dimensions (70x100).
     *
     * @param imageUrl the URL of the image
     * @return the loaded Image
     */
    public static Image loadImage(String imageUrl) {
        return loadImage(imageUrl, 70, 100);
    }

    /**
     * Loads an image asynchronously from the specified URL with given dimensions.
     *
     * @param imageUrl      the URL of the image
     * @param width         the width of the image
     * @param height        the height of the image
     * @param onImageLoaded a callback to handle the loaded image
     */
    public static void loadImageAsync(String imageUrl, int width, int height, Consumer<Image> onImageLoaded) {
        executor.execute(() -> {
            if (StringUtils.isBlank(imageUrl) || imageUrl.equals("null")) {
                return;
            }
            Image image = loadImage(imageUrl, width, height);
            onImageLoaded.accept(image);
        });
    }

    /**
     * Loads an image asynchronously from the specified URL with default dimensions.
     *
     * @param imageUrl      the URL of the image
     * @param onImageLoaded a callback to handle the loaded image
     */
    public static void loadImageAsync(String imageUrl, Consumer<Image> onImageLoaded) {
        executor.execute(() -> {
            Image image = loadImage(imageUrl);
            onImageLoaded.accept(image);
        });
    }

    /**
     * Loads an SVG image into an ImageView from the specified path.
     *
     * @param imageView the ImageView to load the SVG into
     * @param svgPath   the path of the SVG file
     */
    public static void loadSvg(ImageView imageView, String svgPath) {
        imageView.setImage(SvgLoader.getInstance().loadSvgImage("/images/application/icons/svg/" + svgPath,
                imageView.isPreserveRatio(), imageView.getFitWidth(), imageView.getFitHeight()));
    }

    /**
     * Loads an image from the specified URL with given dimensions.
     * Uses a cache to store and retrieve images to reduce network load.
     *
     * @param imageUrl the URL of the image
     * @param width    the width of the image
     * @param height   the height of the image
     * @return the loaded Image
     */
    public static Image loadImage(String imageUrl, int width, int height) {
        // set cache directory and filename
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        File cacheDir = new File(System.getProperty("user.home"), ".imagecache");
        cacheDir.mkdirs();
        String cacheKey = imageUrl.replaceAll("[^a-zA-Z0-9.-]", "_");
        File cacheFile = new File(cacheDir, cacheKey + ".png");

        // check if image exists in cache and is not expired
        if (cacheFile.exists() && !isCacheExpired(cacheFile)) {
            byte[] imageData = readBytesFromFile(cacheFile.getAbsolutePath());
            return getJavaFXImage(imageData, width, height);
        }

        byte[] imageData = downloadImage(imageUrl, cacheFile, width, height);
        return getJavaFXImage(imageData, width, height);
    }

    /**
     * Downloads an image from the specified URL and stores it in a cache file.
     *
     * @param imageUrl the URL of the image
     * @param cacheFile the file to store the cached image
     * @param width the width of the image
     * @param height the height of the image
     * @return the byte array of the downloaded image
     */
    private static byte[] downloadImage(String imageUrl, File cacheFile, int width, int height) {
        // download image and store in cache
        byte[] imageData = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (InputStream in = conn.getInputStream()) {
                BufferedImage image = ImageIO.read(in);

                // Compress and reduce quality
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //fill image boundaries with most common color in image to match the provided ratio
                image = fillImage(image, width, height);
                image = compressImage(image, 0.1f);
                ImageIO.write(image, "png", baos);
                imageData = baos.toByteArray();

                try (FileOutputStream out = new FileOutputStream(cacheFile)) {
                    out.write(imageData);
                }
            }
        } catch (IOException ex) {
            log.log(Level.WARNING, "Error downloading image", ex);
        }
        return imageData;
    }

    /**
     * Fills image boundaries with the most common color in the image to match the provided ratio.
     *
     * @param originalImage the original image
     * @param width the width of the image
     * @param height the height of the image
     * @return the filled image
     */
    public static BufferedImage fillImage(BufferedImage originalImage, int width, int height) {
        try {
            //scale the original image to match the provided ratio
            BufferedImage newImage = scaleImage(originalImage, width, height, true);
            // Compress and reduce quality
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newImage = compressImage(newImage, 0.1f);
            ImageIO.write(newImage, "png", baos);
            return newImage;
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error filling image", ex);
        }
        return originalImage;
    }

    /**
     * Scales the image to the specified dimensions, optionally maintaining the aspect ratio.
     *
     * @param originalImage the original image
     * @param newWidth the new width of the image
     * @param newHeight the new height of the image
     * @param maintainAspectRatio whether to maintain the aspect ratio
     * @return the scaled image
     */
    public static BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight, boolean maintainAspectRatio) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g2d = resizedImage.createGraphics();
        int commonColor = getMostCommonColor(originalImage);
        g2d.setColor(new Color(commonColor));
        if (maintainAspectRatio) {
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            double scaleFactor = getScaleFactor(originalWidth, originalHeight, newWidth, newHeight);

            // center the image when scaling

            //x should be 0 if the image width is less than the new width
            double x = Math.max(0, Math.abs((originalWidth - (double) newWidth) / 2));
            //y should be 0 if x is not 0
            double y = x == 0 ? Math.abs((originalHeight - (double) newHeight) / 2) : 0;

            AffineTransform transform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            transform.translate(x, y);
            g2d.drawImage(originalImage, transform, null);
        } else {
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        }

        g2d.dispose();

        return resizedImage;
    }

    /**
     * Calculates the scale factor to fit the image within the specified dimensions.
     *
     * @param originalWidth the original width of the image
     * @param originalHeight the original height of the image
     * @param newWidth the new width of the image
     * @param newHeight the new height of the image
     * @return the scale factor
     */
    private static double getScaleFactor(int originalWidth, int originalHeight, int newWidth, int newHeight) {
        double scaleX = (double) newWidth / originalWidth;
        double scaleY = (double) newHeight / originalHeight;
        return Math.min(scaleX, scaleY);
    }

    /**
     * Finds the most common color in the image.
     *
     * @param image the image
     * @return the most common color
     */
    private static int getMostCommonColor(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        int[] counts = new int[256 * 256 * 256];
        for (int pixel : pixels) {
            counts[pixel & 0x00ffffff]++;
        }

        int maxCount = 0;
        int maxColor = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
                maxColor = i;
            }
        }
        return maxColor;
    }

    /**
     * Checks if the cache file is expired (older than 24 hours).
     *
     * @param cacheFile the cache file
     * @return true if the cache file is expired, false otherwise
     */
    private static boolean isCacheExpired(File cacheFile) {
        long age = System.currentTimeMillis() - cacheFile.lastModified();
        return age > (24 * 60 * 60 * 1000); // expire after 24 hours
    }

    /**
     * Converts a byte array to a JavaFX Image.
     *
     * @param raw the byte array of the image data
     * @param width the width of the image
     * @param height the height of the image
     * @return the JavaFX Image
     */
    public static Image getJavaFXImage(byte[] raw, int width, int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            if (raw != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(raw);
                BufferedImage read = ImageIO.read(bis);
                read = scaleImage(read, width, height, true);
                image = SwingFXUtils.toFXImage(read, null);
                read.flush();
                read = null;
            } else {
                URL url = Imagefx.class.getResource("noimage.png");
                BufferedImage read = ImageIO.read(url);
                read = scaleImage(read, width, height, true);
                image = SwingFXUtils.toFXImage(read, null);
                read.flush();
                read = null;
            }
        } catch (IOException | IllegalArgumentException ex) {
            log.log(Level.WARNING, "Error reading bytes from image", ex);
        }
        return image;
    }


    /**
     * Converts an Image to a byte array.
     *
     * @param image the Image
     * @return the byte array of the image data
     */
    public static byte[] readBytesFromImage(Image image) {
        byte[] res = null;
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
                ImageIO.write(bImage, "png", s);
                res = s.toByteArray();
            }
        } catch (IOException ex) {
            log.log(Level.WARNING, "Error reading bytes from image", ex);
        }
        return res;
    }

    /**
     * Converts a file to a byte array.
     *
     * @param filePath the path of the file
     * @return the byte array of the file data
     */
    public static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            log.log(Level.WARNING, "Error reading bytes from file", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.log(Level.WARNING, "Error closing file", e);
                }
            }

        }

        return bytesArray;

    }

    /**
     * Compress image
     *
     * @param originalImage
     * @param quality 0.0 - 1.0 (1.0 - highest quality)
     * @return
     */
    private static BufferedImage compressImage(BufferedImage originalImage, float quality) {
        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        // Get the image writer
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        // Set compression quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        // Write the image
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(newImage, null, null), param);
            return ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException e) {
            log.log(Level.WARNING, "Error compressing image", e);
        } finally {
            writer.dispose();
        }

        return newImage;
    }

    /**
     * Rounds the corners of an ImageView.
     *
     * @param clipped the ImageView to clip
     * @param topLeft the radius of the top-left corner
     * @param topRight the radius of the top-right corner
     * @param bottomRight the radius of the bottom-right corner
     * @param bottomLeft the radius of the bottom-left corner
     * @return the clipped ImageView
     */
    public static ImageView round(ImageView clipped,
                                  double topLeft,
                                  double topRight,
                                  double bottomRight,
                                  double bottomLeft) {
        topLeft *= 2;
        topRight *= 2;
        bottomRight *= 2;
        bottomLeft *= 2;

        double width = clipped.getFitWidth();
        double height = clipped.getFitHeight();

        double topEdgeHalf = (width - topLeft - topRight) / 2;
        double rightEdgeHalf = (height - topRight - bottomRight) / 2;
        double bottomEdgeHalf = (width - bottomLeft - bottomRight) / 2;
        double leftEdgeHalf = (height - topLeft - bottomLeft) / 2;

        double topLeftWidth = topEdgeHalf + 2 * topLeft;
        double topLeftHeight = leftEdgeHalf + 2 * topLeft;
        double topLeftX = 0;
        double topLeftY = 0;

        double topRightWidth = topEdgeHalf + 2 * topRight;
        double topRightHeight = rightEdgeHalf + 2 * topRight;
        double topRightX = width - topRightWidth;
        double topRightY = 0;

        double bottomRightWidth = bottomEdgeHalf + 2 * bottomRight;
        double bottomRightHeight = rightEdgeHalf + 2 * bottomRight;
        double bottomRightX = width - bottomRightWidth;
        double bottomRightY = height - bottomRightHeight;

        double bottomLeftWidth = bottomEdgeHalf + 2 * bottomLeft;
        double bottomLeftHeight = leftEdgeHalf + 2 * bottomLeft;
        double bottomLeftX = 0;
        double bottomLeftY = height - bottomLeftHeight;

        Rectangle topLeftRect = new Rectangle(topLeftX, topLeftY, topLeftWidth, topLeftHeight);
        Rectangle topRightRect = new Rectangle(topRightX, topRightY, topRightWidth, topRightHeight);
        Rectangle bottomRightRect = new Rectangle(bottomRightX, bottomRightY, bottomRightWidth, bottomRightHeight);
        Rectangle bottomLeftRect = new Rectangle(bottomLeftX, bottomLeftY, bottomLeftWidth, bottomLeftHeight);

        topLeftRect.setArcWidth(topLeft);
        topLeftRect.setArcHeight(topLeft);
        topRightRect.setArcWidth(topRight);
        topRightRect.setArcHeight(topRight);
        bottomRightRect.setArcWidth(bottomRight);
        bottomRightRect.setArcHeight(bottomRight);
        bottomLeftRect.setArcWidth(bottomLeft);
        bottomLeftRect.setArcHeight(bottomLeft);

        Group clipGroup = new Group(
                topLeftRect,
                topRightRect,
                bottomRightRect,
                bottomLeftRect
        );

        clipped.setClip(clipGroup);
        return clipped;
    }

}
