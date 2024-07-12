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

import javafx.css.CssParser;
import javafx.css.Rule;
import javafx.css.Stylesheet;
import javafx.css.converter.ColorConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGOMSVGElement;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.svg.SVGDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SvgLoader is a utility class for loading and processing SVG images in JavaFX.
 * It supports applying colors and resizing images.
 */
public class SvgLoader {

    public static final String BI_ICON_PATH = "/com/fluxvend/svgfx/images/svg/bi/bi-";
    private static Logger log = Logger.getLogger(SvgLoader.class.getName());

    private static SvgLoader loader;

    private String themePath = "/styles/themes/default.css";

    /**
     * Returns the singleton instance of SvgLoader.
     *
     * @return the singleton instance
     */
    public static SvgLoader getInstance() {
        if (loader == null) {
            loader = new SvgLoader();
        }
        return loader;
    }

    public static SvgLoader getInstance(String themePath) {
        if (loader == null) {
            loader = new SvgLoader();
        }
        loader.themePath = themePath;
        return loader;
    }

    private SvgLoader() {
    }

    /**
     * Loads an SVG image from a URL with specified width and height.
     *
     * @param url        the URL of the SVG file
     * @param fillStroke whether to fill stroke color
     * @param width      the width of the image
     * @param height     the height of the image
     * @return the loaded image
     */
    public Image loadSvgImage(String url, boolean fillStroke, Double width, Double height) {
        return loadSvgImage(url, null, fillStroke, width, height);
    }

    /**
     * Loads an SVG image from a URL with specified color class, fill stroke, width, and height.
     *
     * @param url        the URL of the SVG file
     * @param colorClass the color class to apply
     * @param fillStroke whether to fill stroke color
     * @param width      the width of the image
     * @param height     the height of the image
     * @return the loaded image
     */
    public Image loadSvgImage(String url, String colorClass, boolean fillStroke, Double width, Double height) {
        try (InputStream file = getClass().getResourceAsStream(url)) {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            SVGDocument doc = f.createSVGDocument(url, file);


            if (colorClass != null) {
                if (colorClass.startsWith("-fx-")) {
                    applyThemeColor(colorClass, doc, fillStroke);
                } else if (colorClass.startsWith("#")) {
                    applyHexColor(colorClass, doc, fillStroke);
                } else {
                    applyHexColor(toHexString(Color.web(colorClass)), doc, fillStroke);
                }
            }
            doc.getDocumentElement().normalize();

            String widthAttribute = doc.getDocumentElement().getAttribute("width");
            Double imageWidth = StringUtils.isBlank(widthAttribute) ? null : Double.parseDouble(widthAttribute.replace("px", ""));
            String heightAttribute = doc.getDocumentElement().getAttribute("height");
            Double imageHeight = StringUtils.isBlank(heightAttribute) ? null : Double.parseDouble(heightAttribute.replace("px", ""));
            if (imageWidth == null || imageHeight == null) {
                String viewBox = doc.getDocumentElement().getAttribute("viewBox");
                if (StringUtils.isNotBlank(viewBox)) {
                    String[] parts = viewBox.split(" ");
                    if (imageWidth == null) {
                        imageWidth = Double.parseDouble(parts[2]);
                    }
                    if (imageHeight == null) {
                        imageHeight = Double.parseDouble(parts[3]);
                    }
                }
            }

            if (imageWidth != null && imageHeight != null) {
                if (width != null && height != null) {
                    double aspectRatio = imageWidth / imageHeight;
                    double newAspectRatio = width / height;
                    if (newAspectRatio > aspectRatio) {
                        width = height * aspectRatio;
                    } else {
                        height = width / aspectRatio;
                    }
                } else if (width != null) {
                    height = width / imageWidth * imageHeight;
                } else if (height != null) {
                    width = height / imageHeight * imageWidth;
                } else {
                    width = imageWidth;
                    height = imageHeight;
                }
            }

            if (width == null) {
                width = 300.0;
            }
            if (height == null) {
                height = 300.0;
            }

            doc.getDocumentElement().setAttribute("width", width + "px");
            doc.getDocumentElement().setAttribute("height", height + "px");
            TranscoderInput transIn = new TranscoderInput(doc);
            try {
                BufferedImageTranscoder trans = new BufferedImageTranscoder(width, height);
                trans.transcode(transIn, null);
                return SwingFXUtils.toFXImage(trans.getBufferedImage(), null);
            } catch (TranscoderException ex) {
                log.log(Level.SEVERE,"", ex);
                throw new RuntimeException(ex);
            }
        } catch (IOException io) {
            log.log(Level.SEVERE,"", io);
            throw new RuntimeException(io);
        }
    }

    /**
     * Loads an SVG image from a URL with default settings.
     *
     * @param url the URL of the SVG file
     * @return the loaded image
     */
    public Image loadSvgImage(String url) {
        return loadSvgImage(url, null, false, null, null);
    }

    /**
     * Loads an SVG image from a URL with specified color class and fill stroke.
     *
     * @param url        the URL of the SVG file
     * @param colorClass the color class to apply
     * @param fillStroke whether to fill stroke color
     * @return the loaded image
     */
    public Image loadSvgImage(String url, String colorClass, boolean fillStroke) {
        return loadSvgImage(url, colorClass, fillStroke, null, null);
    }

    /**
     * Loads an SVG image from a URL with specified fill stroke.
     *
     * @param url        the URL of the SVG file
     * @param fillStroke whether to fill stroke color
     * @return the loaded image
     */
    public Image loadSvgImage(String url, boolean fillStroke) {
        return loadSvgImage(url, null, fillStroke);
    }

    /**
     * Loads an SVG image from a URL with specified color class.
     *
     * @param url        the URL of the SVG file
     * @param colorClass the color class to apply
     * @return the loaded image
     */
    public Image loadSvgImage(String url, String colorClass) {
        return loadSvgImage(url, colorClass, false, null, null);
    }

    /**
     * Loads a Bootstrap icon SVG image with specified parameters.
     *
     * @param name       the name of the icon
     * @param colorClass the color class to apply
     * @param fillStroke whether to fill stroke color
     * @param width      the width of the image
     * @param height     the height of the image
     * @return the loaded image
     */
    public static Image bi(String name, String colorClass, boolean fillStroke, double width, double height) {
        return getInstance().loadSvgImage(BI_ICON_PATH + name + ".svg", colorClass, fillStroke, width, height);
    }

    /**
     * Loads a Bootstrap icon SVG image with specified parameters.
     *
     * @param name       the name of the icon
     * @param colorClass the color class to apply
     * @param width      the width of the image
     * @param height     the height of the image
     * @return the loaded image
     */
    public static Image bi(String name, String colorClass, double width, double height) {
        return getInstance().loadSvgImage(BI_ICON_PATH + name + ".svg", colorClass, false, width, height);
    }

    /**
     * Loads a Bootstrap icon SVG image with specified parameters.
     *
     * @param name       the name of the icon
     * @param colorClass the color class to apply
     * @param fillStroke whether to fill stroke color
     * @return the loaded image
     */
    public static Image bi(String name, String colorClass, boolean fillStroke) {
        return bi(name, colorClass, fillStroke, 300, 300);
    }

    /**
     * Loads a Bootstrap icon SVG image with specified parameters.
     *
     * @param name       the name of the icon
     * @param colorClass the color class to apply
     * @return the loaded image
     */
    public static Image bi(String name, String colorClass) {
        return bi(name, colorClass, false);
    }

    /**
     * Applies a theme color to an SVG document.
     *
     * @param property   the CSS property to use for the color
     * @param document   the SVG document
     * @param fillStroke whether to fill stroke color
     */
    private void applyThemeColor(String property, SVGDocument document, boolean fillStroke) {
        try {
            CssParser parser = new CssParser();
            Stylesheet css = parser.parse(getClass().getResource(themePath).toURI().toURL());
            final Rule rootRule = css.getRules().get(0); // .root
            Color color = rootRule.getDeclarations().stream()
                    .filter(d -> d.getProperty().equals(property))
                    .findFirst()
                    .map(d -> ColorConverter.getInstance().convert(d.getParsedValue(), null))
                    .orElse(null);
            if (color != null) {
                String colorString = toHexString(color);
                ((SVGOMSVGElement) document.getElementsByTagName("svg").item(0)).setAttribute("fill", colorString);
                if (fillStroke) {
                    ((SVGOMSVGElement) document.getElementsByTagName("svg").item(0)).setAttribute("stroke", colorString);
                }
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE,"", ex);
        }
    }

    /**
     * Applies a hex color to an SVG document.
     *
     * @param hexColor   the hex color to apply
     * @param document   the SVG document
     * @param fillStroke whether to fill stroke color
     */
    private void applyHexColor(String hexColor, SVGDocument document, boolean fillStroke) {
        try {
            ((SVGOMSVGElement) document.getElementsByTagName("svg").item(0)).setAttribute("fill", hexColor);
            if (fillStroke) {
                ((SVGOMSVGElement) document.getElementsByTagName("svg").item(0)).setAttribute("stroke", hexColor);
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE,"", ex);
        }
    }

    /**
     * Converts a color to a hex string.
     *
     * @param color the color to convert
     * @return the hex string
     */
    public String toHexString(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()));
    }

}



