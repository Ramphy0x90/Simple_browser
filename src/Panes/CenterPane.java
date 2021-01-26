package Panes;

import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CenterPane extends VBox {
    private final WebView webView = new WebView();
    private final WebEngine webEngine = webView.getEngine();

    public CenterPane(){
        this.webView.prefWidthProperty().bind(this.widthProperty());
        this.webView.prefHeightProperty().bind(this.heightProperty());
        this.webView.setContextMenuEnabled(false);
        this.webView.setZoom(0.8);

        this.webEngine.load("https://google.com");

        getChildren().add(webView);
    }

    public WebView getWebView(){
        return this.webView;
    }

    public WebEngine getWebEngine(){
        return this.webEngine;
    }
}
