# SVGFX

SVGFX is a custom JavaFX controller library that extends ImageView to support Bootstrap icons and other SVG images. It enables high-quality vector SVG rendering, ensuring crisp visuals on any display. SVGFX simplifies scalable graphics integration for visually appealing JavaFX applications.

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.fluxvend</groupId>
    <artifactId>svgfx</artifactId>
    <version>0.0.2</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.fluxvend:svgfx:0.0.2'
}
```

### Manual Installation

Clone the repository and build the project manually:

```bash
git clone https://github.com/bnnsz/svgfx.git
cd svgfx
mvn clean install
```

## Usage

### Bootstrap Icons
#### Java

```java
import com.fluxvend.svgfx.BootstrapIcon;
import com.fluxvend.svgfx.icons.Bi;

BootstrapIcon icon = new BootstrapIcon(Bi.ARROW_LEFT_CIRCLE);
icon.setColor("#FF0000");
```

#### FXML

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import com.fluxvend.svgfx.BootstrapIcon?>

<BootstrapIcon icon="ARROW_LEFT_CIRCLE" color="#FF0000" />
```

## Contributing

Contributions are welcome! Please read our [contributing guidelines](CONTRIBUTING.md) to get started.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE.md) file for details.
