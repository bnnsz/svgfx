open module svgfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires java.prefs;
    requires java.base;
    requires transitive batik.anim;
    requires transitive batik.transcoder;
    requires transitive batik.util;
    requires transitive batik.bridge;
    requires transitive batik.css;
    requires transitive batik.dom;
    requires transitive xml.apis.ext;
    requires javafx.swing;
    requires org.apache.commons.lang3;
    requires java.compiler;
    requires java.logging;
    exports com.fluxvend.svgfx;
}