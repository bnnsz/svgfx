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


@DefaultProperty("icon")
public class BootstrapIcon extends ImageView {
    @FXML
    private ObjectProperty<Bi> icon = new SimpleObjectProperty<>();
    @FXML
    private StringProperty color = new SimpleStringProperty();
    private static String ICON_PATH = "/com/fluxvend/svgfx/images/svg/bi/";
    private static final String DEFAULT_STYLE_CLASS = "image-view";

    @FXML
    public Bi getIcon() {
        return icon.get();
    }

    @FXML
    public void setIcon(Bi icon) {
        this.icon.set(icon);
    }

    @FXML
    public ObjectProperty<Bi> iconProperty() {
        return icon;
    }

    @FXML
    public StringProperty colorProperty() {
        return color;
    }

    @FXML
    public String getColor() {
        return color.get();
    }

    @FXML
    public void setColor(String color) {
        this.color.set(color);
    }


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

