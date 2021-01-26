package Panes;

import Panes.ObjUtils.Favorites;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class TopPane extends HBox{
    private BorderPane mainPane;
    private CenterPane centerPane;

    private final Button[] navigateButtons;
    private final TextField urlField;
    private final Favorites favoritesTable = new Favorites();

    private MenuButton rootMenu;
    private Menu favorites;
    private Menu historyMenu;
    private MenuItem resetHistory;
    private Button plus;
    private Button minus;
    private Menu settings;

    private RadioMenuItem anonymous;

    public TopPane(){
        setSpacing(10);

        this.urlField = new TextField();
        this.urlField.prefWidthProperty().bind(this.widthProperty());
        this.urlField.setText("https://www.google.com/");

        this.navigateButtons = new Button[5];
        for(int i = 0; i < 5; i++){
            this.navigateButtons[i] = new Button();
            this.navigateButtons[i].getStyleClass().add("navButtons");
            this.navigateButtons[i].setId("button" + i + "B");
        }

        getChildren().addAll(navigateButtons);
        getChildren().add(3, urlField);

        initMenu();
    }

    public void setPanes(BorderPane mainPane, CenterPane centerPane){
        this.mainPane = mainPane;
        this.centerPane = centerPane;

        initEvents();
    }

    public void initMenu(){
        this.rootMenu = new MenuButton();
        this.rootMenu.setId("rootMenuB");
        this.rootMenu.getStyleClass().add("rootMenu");
        this.rootMenu.nodeOrientationProperty().setValue(NodeOrientation.RIGHT_TO_LEFT);

        this.favorites = new Menu("Preferiti");

        Label clearText = new Label("Svuota");

        this.resetHistory = new MenuItem();
        this.resetHistory.setGraphic(clearText);
        resetHistory.setOnAction((e) -> {
            this.historyMenu.getItems().remove(2, this.historyMenu.getItems().size());
        });

        this.historyMenu = new Menu("Cronologia");
        this.historyMenu.getItems().addAll(resetHistory, new SeparatorMenuItem());

        this.settings = new Menu("Impostazioni");

        Menu theme = new Menu("Tema");
        RadioMenuItem light = new RadioMenuItem("Light");
        light.setOnAction((e) -> changeTheme(1));
        light.setSelected(true);

        RadioMenuItem dark = new RadioMenuItem("Dark");
        dark.setOnAction((e) -> changeTheme(0));

        ToggleGroup toggleTheme = new ToggleGroup();
        toggleTheme.getToggles().add(light);
        toggleTheme.getToggles().add(dark);

        Menu zoom = new Menu("Zoom");
        this.plus = new Button("+");
        this.minus = new Button("-");

        this.plus.getStyleClass().add("zoomButton");
        this.minus.getStyleClass().add("zoomButton");

        CustomMenuItem plusCustom = new CustomMenuItem(this.plus);
        CustomMenuItem minusCustom = new CustomMenuItem(this.minus);
        plusCustom.setHideOnClick(false);
        minusCustom.setHideOnClick(false);

        zoom.getItems().addAll(plusCustom, minusCustom);

        this.anonymous = new RadioMenuItem("Incognito");

        ToggleGroup togglePrivacy = new ToggleGroup();
        togglePrivacy.getToggles().add(this.anonymous);

        theme.getItems().addAll(light, new SeparatorMenuItem(), dark);
        this.settings.getItems().addAll(theme, zoom, this.anonymous);
        this.rootMenu.getItems().addAll(this.favorites, this.historyMenu, this.settings);

        getChildren().add(rootMenu);
    }

    public void initEvents(){
        WebView webView = this.centerPane.getWebView();
        WebEngine webEngine = this.centerPane.getWebEngine();
        WebHistory webHistory = webEngine.getHistory();

        this.navigateButtons[0].setOnAction((e) -> {
            try{webHistory.go(-1);}
            catch (IndexOutOfBoundsException ignored){}

            urlField.setText(webEngine.getLocation());
        });

        this.navigateButtons[1].setOnAction((e) -> {
            try{webHistory.go(1);}
            catch (IndexOutOfBoundsException ignored){}

            urlField.setText(webEngine.getLocation());
        });

        this.navigateButtons[2].setOnAction((e) -> {
            webEngine.reload();
            urlField.setText(webEngine.getLocation());
        });

        this.navigateButtons[3].setOnAction((e) -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nome pagina per preferita");
            dialog.setHeaderText("Se il nome viene lasciato in bianco \nverrà dato uno di default");
            dialog.setContentText("Scegliere nome:");

            Optional<String> result = dialog.showAndWait();

            String link = this.urlField.getText();

            try {
                String url = new URI(link).getHost();
                String idName;

                if(result.isPresent()){
                    idName = (result.get().length() != 0) ? result.get() : url;

                    Menu newFav = new Menu(idName);
                    newFav.setOnAction((e1) -> {
                        String newLink = this.favoritesTable.getUrl(idName);

                        if(newLink.length() != 0)
                            webEngine.load(newLink);
                    });

                    Button deleteFav = new Button("X");
                    deleteFav.setStyle("-fx-background-color: #ff3528");
                    deleteFav.setOnAction((e1) -> {
                        int coso = this.favoritesTable.remove(idName);
                        this.favorites.getItems().remove(coso);
                        System.out.println(coso);
                    });

                    CustomMenuItem delFavCustom = new CustomMenuItem(deleteFav);
                    newFav.getItems().add(delFavCustom);

                    this.favoritesTable.insert(idName, link);
                    this.favorites.getItems().add(newFav);
                }
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });

        this.navigateButtons[4].setOnAction((e) -> {
            String link = this.urlField.getText();
            urlField.setText(link);
            webEngine.load(link);
        });

        webEngine.getHistory().currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            Label linkLabel = new Label(webHistory.getEntries().get((Integer) newValue).getUrl());
            linkLabel.setPrefWidth(170);

            MenuItem page = new MenuItem();
            page.setGraphic(linkLabel);

            page.setOnAction((e) -> {
                String link = ((MenuItem)e.getSource()).getGraphic().toString();
                webEngine.load(link.split("'")[1]);
            });

            if(!this.anonymous.isSelected())
                this.historyMenu.getItems().add(page);

            urlField.setText(webEngine.getLocation());
        });

        this.plus.setOnAction((e) -> webView.setZoom((webView.getZoom() + 0.2)));

        this.minus.setOnAction((e) -> webView.setZoom((webView.getZoom() - 0.2)));
    }

    public void changeTheme(int theme){
        ArrayList<String> themeRefer = new ArrayList<>(Arrays.asList("B", "W"));
        if(theme == 0) Collections.reverse(themeRefer);

        for(Button b: this.navigateButtons)
            b.setId(b.getId().replace(themeRefer.get(1), themeRefer.get(0)));

        this.rootMenu.setId(this.rootMenu.getId().replace(themeRefer.get(1), themeRefer.get(0)));
        this.mainPane.setId(this.mainPane.getId().replace(themeRefer.get(1), themeRefer.get(0)));
    }
}
