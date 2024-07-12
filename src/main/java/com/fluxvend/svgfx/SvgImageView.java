/*
 * Copyright (c) 2008, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.fluxvend.svgfx;

import com.fluxvend.svgfx.utils.SvgLoader;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;

/**
 * SvgImageView is a custom ImageView component for displaying SVG images in JavaFX.
 */
@DefaultProperty("svg")
public class SvgImageView extends Control {
    private PauseTransition pauseTransition;

    @FXML
    private StringProperty color = new SimpleStringProperty();
    @FXML
    private StringProperty svg = new SimpleStringProperty();

    private ImageView imageView = new ImageView();

    private static final String DEFAULT_STYLE_CLASS = "svg-image-view";
    private static final int DEFAULT_SIZE = 100;

    /**
     * Get the color property
     *
     * @return the color property
     */
    @FXML
    public StringProperty colorProperty() {
        return color;
    }

    /**
     * Get the color property
     *
     * @return the current color
     */
    @FXML
    public String getColor() {
        return color.get();
    }

    /**
     * Set the color property
     *
     * @param color the color to set
     */
    @FXML
    public void setColor(String color) {
        this.color.set(color);
    }

    /**
     * Get the svg property
     *
     * @return the svg property
     */
    @FXML
    public StringProperty svgProperty() {
        return svg;
    }

    /**
     * Get the svg property
     *
     * @return the current svg
     */
    @FXML
    public String getSvg() {
        return svg.get();
    }

    /**
     * Set the svg property
     *
     * @param svg the svg to set
     */
    @FXML
    public void setSvg(String svg) {
        this.svg.set(svg);
    }


    /**
     * Loads the image for the current svg and color.
     *
     * @return the loaded image
     */
    public Image loadImage() {
        if(StringUtils.isBlank(this.getSvg())) {
            return null;
        }
        Double width = imageView.getFitWidth() <= 0.00 ? null : imageView.getFitWidth();
        Double height = imageView.getFitHeight() <= 0.00 ? null :  imageView.getFitHeight();
        String url = this.getSvg();
        String color = this.getColor();
        Image image = SvgLoader.getInstance().loadSvgImage(url, color,false,width,height);
        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
        return image;
    }

    /**
     * Loads the image asynchronously
     * This method will load the image in a background thread and update the image view when done.
     * It will also add a pause transition to prevent flickering when the image is changed rapidly.
     */
    private void loadImageAsync() {
        if (pauseTransition != null && pauseTransition.getStatus() == Animation.Status.RUNNING) {
            pauseTransition.stop();
        }
        pauseTransition = new PauseTransition(Duration.seconds(2));
        pauseTransition.setOnFinished(event -> {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    return loadImage();
                }

                @Override
                protected void succeeded() {
                    imageView.setImage(getValue());
                }
            };
            new Thread(loadImageTask).start();
        });
        pauseTransition.play();
    }



    /**
     * Default constructor for SvgImageView. Sets default size, and initializes listeners.
     */
    public SvgImageView() {
        this.setPrefSize(DEFAULT_SIZE,DEFAULT_SIZE);
        imageView.setFitHeight(DEFAULT_SIZE);
        imageView.setFitWidth(DEFAULT_SIZE);
        this.setWidth(DEFAULT_SIZE);
        this.setHeight(DEFAULT_SIZE);

        this.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);

        imageView.setImage(loadImage());
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.getChildren().add(imageView);

        this.svg.addListener(svgChangeListener);
        this.color.addListener(colorChangeListener);
        this.prefWidthProperty().addListener(widthChangeListener);
        this.prefHeightProperty().addListener(heightChangeListener);
    }

    /**
     * Constructor for SvgImageView with a specified svg.
     *
     * @param svg the initial svg to set
     */
    public SvgImageView(String svg) {
        this.setPrefSize(DEFAULT_SIZE,DEFAULT_SIZE);
        imageView.setFitHeight(DEFAULT_SIZE);
        imageView.setFitWidth(DEFAULT_SIZE);
        this.setWidth(DEFAULT_SIZE);
        this.setHeight(DEFAULT_SIZE);

        this.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);

        this.svg.set(svg);
        imageView.setImage(loadImage());
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.getChildren().add(imageView);

        this.svg.addListener(svgChangeListener);
        this.color.addListener(colorChangeListener);
        this.prefWidthProperty().addListener(widthChangeListener);
        this.prefHeightProperty().addListener(heightChangeListener);
    }

    /**
     * Change listener for the svg property
     * This listener will reload the image
     */
    private final ChangeListener<String> svgChangeListener = (observable, oldValue, newValue) -> {
        imageView.setImage(loadImage());
    };

    /**
     * Change listener for the color property
     * This listener will reload the image
     */
    private final ChangeListener<String> colorChangeListener = (observable, oldValue, newValue) -> {
        loadImageAsync();
    };

    /**
     * Change listener for the width property
     * This listener will resize the image view to fit the new width and height
     * and reload the image
     */
    private final ChangeListener<Number> widthChangeListener = (observable, oldValue, newValue) -> {
        imageView.setFitWidth(newValue.doubleValue());
        loadImageAsync();
    };

    /**
     * Change listener for the height property
     * This listener will resize the image view to fit the new width and height
     * and reload the image
     */
    private final ChangeListener<Number> heightChangeListener = (observable, oldValue, newValue) -> {
        imageView.setFitHeight(newValue.doubleValue());
        loadImageAsync();
    };

    /** {@inheritDoc} */
    protected void layoutChildren() {
        this.imageView.resizeRelocate(0.0, 0.0, this.getWidth(), this.getHeight());
    }
}