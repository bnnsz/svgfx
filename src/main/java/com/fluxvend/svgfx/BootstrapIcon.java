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
package com.fluxvend.svgfx;

import com.fluxvend.svgfx.icons.Bi;
import com.fluxvend.svgfx.utils.SvgLoader;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.beans.DefaultProperty;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * BootstrapIcon is a custom ImageView component for displaying Bootstrap icons in JavaFX.
 * The icon and its color can be set using properties.
 */
@DefaultProperty("icon")
public class BootstrapIcon extends Control {
    private PauseTransition pauseTransition;
    @FXML
    private ObjectProperty<Bi> icon = new SimpleObjectProperty<>();
    @FXML
    private StringProperty color = new SimpleStringProperty();
    @FXML
    private DoubleProperty size = new SimpleDoubleProperty(24.0);
    private static String ICON_PATH = "/com/fluxvend/svgfx/images/svg/bi/";
    private static final String DEFAULT_STYLE_CLASS = "bootstrap-icon";

    private ImageView imageView = new ImageView();

    /**
     * Get the icon property
     *
     * @return the current icon
     */
    @FXML
    public Bi getIcon() {
        return icon.get();
    }

    /**
     * Set the icon property
     * @param icon the icon to set
     */
    @FXML
    public void setIcon(Bi icon) {
        this.icon.set(icon);
    }

    /**
     * Get the icon property
     *
     * @return the icon property
     */
    @FXML
    public ObjectProperty<Bi> iconProperty() {
        return icon;
    }

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
     * Get the size property
     *
     * @return the size property
     */
    @FXML
    public DoubleProperty sizeProperty() {
        return size;
    }

    /**
     * Get the size property
     *
     * @return the current size
     */
    @FXML
    public double getSize() {
        return size.get();
    }

    /**
     * Set the size property
     *
     * @param size the size to set
     */
    @FXML
    public void setSize(double size) {
        this.size.set(size);
    }


    /**
     * Loads the image for the current icon and color.
     *
     * @return the loaded image
     */
    public Image loadImage() {
        Bi bi = icon.get();
        if(bi == null){
            return null;
        }
        double width = this.getSize();
        double height = this.getSize();
        String color = this.getColor();
        return SvgLoader.getInstance().loadSvgImage(ICON_PATH+"bi-"+bi.getIcon()+".svg",color,false,width,height);
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
     * Default constructor for BootstrapIcon. Sets default size, icon, and initializes listeners.
     */
    public BootstrapIcon() {
        imageView.setFitHeight(this.getSize());
        imageView.setFitWidth(this.getSize());
        this.setWidth(this.getSize());
        this.setHeight(this.getSize());
        this.setPrefSize(this.getSize(),this.getSize());
        this.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);

        this.icon.set(Bi.ARROW_LEFT_CIRCLE);
        imageView.setImage(loadImage());
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.getChildren().add(imageView);

        this.icon.addListener(iconChangeListener);
        this.color.addListener(colorChangeListener);
        this.size.addListener(sizeChangeListener);
        this.prefWidthProperty().addListener(widthChangeListener);
        this.prefHeightProperty().addListener(heightChangeListener);

        this.prefHeightProperty().bindBidirectional(this.prefWidthProperty());
        this.size.bindBidirectional(this.prefWidthProperty());
    }

    /**
     * Constructor for BootstrapIcon with a specified icon.
     *
     * @param icon the initial icon to set
     */
    public BootstrapIcon(Bi icon) {
        imageView.setFitHeight(this.getSize());
        imageView.setFitWidth(this.getSize());
        this.setWidth(this.getSize());
        this.setHeight(this.getSize());
        this.setPrefSize(this.getSize(),this.getSize());
        this.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);

        this.icon.set(icon);
        imageView.setImage(loadImage());
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.getChildren().add(imageView);

        this.icon.addListener(iconChangeListener);
        this.color.addListener(colorChangeListener);
        this.size.addListener(sizeChangeListener);
        this.prefWidthProperty().addListener(widthChangeListener);
        this.prefHeightProperty().addListener(heightChangeListener);

        this.prefHeightProperty().bindBidirectional(this.prefWidthProperty());
        this.size.bindBidirectional(this.prefWidthProperty());
    }

    /**
     * Change listener for the icon property
     * This listener will reload the image
     */
    private final ChangeListener<Bi> iconChangeListener = (observable, oldValue, newValue) -> {
        loadImageAsync();
    };

    /**
     * Change listener for the color property
     * This listener will reload the image
     */
    private final ChangeListener<String> colorChangeListener = (observable, oldValue, newValue) -> {
        loadImageAsync();
    };

    /**
     * Change listener for the size property
     * This listener will resize the image view and reload the image
     */
    private final ChangeListener<Number> sizeChangeListener = (observable, oldValue, newValue) -> {
        imageView.setFitHeight(newValue.doubleValue());
        imageView.setFitWidth(newValue.doubleValue());
        loadImageAsync();
    };

    /**
     * Change listener for the width property
     * This listener will resize the image view to fit the new width and height
     * and reload the image
     */
    private final ChangeListener<Number> widthChangeListener = (observable, oldValue, newValue) -> {
        double val = Math.min(newValue.doubleValue(),this.getPrefHeight());
        this.setSize(val);
    };

    /**
     * Change listener for the height property
     * This listener will resize the image view to fit the new width and height
     * and reload the image
     */
    private final ChangeListener<Number> heightChangeListener = (observable, oldValue, newValue) -> {
        double val = Math.min(newValue.doubleValue(),this.getPrefWidth());
        this.setSize(val);
    };

    /** {@inheritDoc} */
    protected void layoutChildren() {
        this.imageView.resizeRelocate(0.0, 0.0, this.getSize(), this.getSize());
    }

}

