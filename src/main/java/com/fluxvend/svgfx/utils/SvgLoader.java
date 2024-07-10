/*
 * Copyright 2016 Andreas Fester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.w3c.dom.svg.SVGDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SvgLoader {

    private static Logger log = Logger.getLogger(SvgLoader.class.getName());

    private static SvgLoader loader;

    public static SvgLoader getInstance() {
        if (loader == null) {
            loader = new SvgLoader();
        }
        return loader;
    }

    private SvgLoader() {
    }

    public Image loadSvgImage(String url, boolean fillStroke, int width, int height) {
        return loadSvgImage(url, null, fillStroke, width, height);
    }

    public Image loadSvgImage(String url, String colorClass, boolean fillStroke, int width, int height) {

        BufferedImageTranscoder trans = new BufferedImageTranscoder(width, height);
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
            doc.getDocumentElement().setAttribute("width", width + "px");
            doc.getDocumentElement().setAttribute("height", height + "px");
            TranscoderInput transIn = new TranscoderInput(doc);
            try {

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

    public Image loadSvgImage(String url) {
        return loadSvgImage(url, null, false, 300, 300);
    }

    public Image loadSvgImage(String url, String colorClass, boolean fillStroke) {
        return loadSvgImage(url, colorClass, fillStroke, 300, 300);
    }
    
    public Image loadSvgImage(String url, boolean fillStroke) {
        return loadSvgImage(url, null, fillStroke);
    }

    public Image loadSvgImage(String url, String colorClass) {
        return loadSvgImage(url, colorClass, false, 300, 300);
    }

    public static Image bi(String name, String colorClass, boolean fillStroke, int width, int height) {
        return getInstance().loadSvgImage("/images/application/icons/svg/bi/bi-" + name + ".svg", colorClass, fillStroke, width, height);
    }

    public static Image bi(String name, String colorClass, boolean fillStroke, double width, double height) {
        return getInstance().loadSvgImage("/images/application/icons/svg/bi/bi-" + name + ".svg", colorClass, fillStroke, (int) width, (int) height);
    }

    public static Image bi(String name, String colorClass, double width, double height) {
        return getInstance().loadSvgImage("/images/application/icons/svg/bi/bi-" + name + ".svg", colorClass, false, (int) width, (int) height);
    }

    public static Image bi(String name, String colorClass, boolean fillStroke) {
        return bi(name, colorClass, fillStroke, 300, 300);
    }

    public static Image bi(String name, String colorClass) {
        return bi(name, colorClass, false);
    }

    private void applyThemeColor(String property, SVGDocument document, boolean fillStroke) {
        try {
            CssParser parser = new CssParser();
            Stylesheet css = parser.parse(getClass().getResource("/styles/themes/default.css").toURI().toURL());
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

    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public String toHexString(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()));
    }

}



