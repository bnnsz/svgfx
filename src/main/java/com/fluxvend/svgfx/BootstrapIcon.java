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
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.AccessibleRole;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * BootstrapIcon is a custom ImageView component for displaying Bootstrap icons in JavaFX.
 * The icon and its color can be set using properties.
 */
@DefaultProperty("icon")
public class BootstrapIcon extends ImageView {
    @FXML
    private ObjectProperty<Bi> icon = new SimpleObjectProperty<>();
    @FXML
    private StringProperty color = new SimpleStringProperty();
    private static String ICON_PATH = "/com/fluxvend/svgfx/images/svg/bi/";
    private static final String DEFAULT_STYLE_CLASS = "image-view";

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
     * Loads the image for the current icon and color.
     *
     * @return the loaded image
     */
    public Image loadImage() {
        Bi bi = icon.get();
        if(bi == null){
            return null;
        }
        int width = (int) this.getFitWidth();
        int height = (int) this.getFitHeight();
        String color = this.getColor();
        return SvgLoader.getInstance().loadSvgImage(ICON_PATH+"bi-"+bi.getIcon()+".svg",color,false,width,height);
    }
    
    

    /**
     * Default constructor for BootstrapIcon. Sets default size, icon, and initializes listeners.
     */
    public BootstrapIcon() {
        this.setFitHeight(24);
        this.setFitWidth(24);
        this.icon.set(Bi.ARROW_LEFT_CIRCLE);
        this.setImage(loadImage());


        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.icon.addListener(iconChangeListener);
        this.color.addListener(colorChangeListener);
    }

    /**
     * Constructor for BootstrapIcon with a specified icon.
     *
     * @param icon the initial icon to set
     */
    public BootstrapIcon(Bi icon) {
        this.setFitHeight(24);
        this.setFitWidth(24);
        this.icon.set(icon);
        this.setImage(loadImage());

        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.icon.addListener(iconChangeListener);
        this.color.addListener(colorChangeListener);
    }

    private final ChangeListener<Bi> iconChangeListener = (observable, oldValue, newValue) -> {
        this.setImage(loadImage());
    };

    private final ChangeListener<String> colorChangeListener = (observable, oldValue, newValue) -> {
        this.setImage(loadImage());
    };


}

