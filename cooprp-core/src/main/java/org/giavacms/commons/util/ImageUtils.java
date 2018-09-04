package org.giavacms.commons.util;

import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    static Logger logger = Logger.getLogger(ImageUtils.class.getCanonicalName());

    public ImageUtils() {
    }

    public static Integer getImageWidthProportional(Object imageData, int maxWidth, int maxHeight) {
        return getImageSizeProportional(imageData, maxWidth, maxHeight)[0];
    }

    public static Integer getImageHeightProportional(Object imageData, int maxWidth, int maxHeight) {
        return getImageSizeProportional(imageData, maxWidth, maxHeight)[1];
    }

    public static Integer[] getImageSizeProportional(Object imageData, int maxWidth, int maxHeight) {
        ImageIcon imageIcon = new ImageIcon((byte[]) ((byte[]) imageData));
        double ratioH = (double) maxHeight / (double) imageIcon.getIconHeight();
        double ratioW = (double) maxWidth / (double) imageIcon.getIconWidth();
        int targetWidth = imageIcon.getIconWidth();
        int targetHeight = imageIcon.getIconHeight();
        if (ratioW < ratioH) {
            if (ratioW < 1.0D) {
                targetWidth = (int) ((double) imageIcon.getIconWidth() * ratioW);
                targetHeight = (int) ((double) imageIcon.getIconHeight() * ratioW);
            }
        } else if (ratioH < 1.0D) {
            targetWidth = (int) ((double) imageIcon.getIconWidth() * ratioH);
            targetHeight = (int) ((double) imageIcon.getIconHeight() * ratioH);
        }

        return new Integer[]{targetWidth, targetHeight};
    }

    public static byte[] resizeImage(byte[] imageData, int maxWidthOrHeight, String type) throws IOException {
        ImageIcon imageIcon = new ImageIcon(imageData);
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        boolean isPortraitImage;
        if (width <= height) {
            isPortraitImage = true;
        } else {
            isPortraitImage = false;
        }

        double ratio;
        if (isPortraitImage && maxWidthOrHeight > 0 && height > maxWidthOrHeight) {
            ratio = (double) maxWidthOrHeight / (double) imageIcon.getIconHeight();
            logger.debug("resize ratio: " + ratio);
            width = (int) ((double) imageIcon.getIconWidth() * ratio);
            height = maxWidthOrHeight;
            logger.debug("imageIcon post scale width: " + width + "  height: " + maxWidthOrHeight);
        }

        if (!isPortraitImage && maxWidthOrHeight > 0 && width > maxWidthOrHeight) {
            ratio = (double) maxWidthOrHeight / (double) imageIcon.getIconWidth();
            logger.debug("resize ratio: " + ratio);
            height = (int) ((double) imageIcon.getIconHeight() * ratio);
            width = maxWidthOrHeight;
            logger.debug("imageIcon post scale width: " + maxWidthOrHeight + "  height: " + height);
        }

        BufferedImage bufferedResizedImage = new BufferedImage(width, height, 1);
        Graphics2D g2d = bufferedResizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(imageIcon.getImage(), 0, 0, width, height, (ImageObserver) null);
        g2d.dispose();
        ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedResizedImage, type.toUpperCase(), encoderOutputStream);
        byte[] resizedImageByteArray = encoderOutputStream.toByteArray();
        return resizedImageByteArray;
    }
}
