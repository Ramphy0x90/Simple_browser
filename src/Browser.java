import Panes.TopPane;
import Panes.CenterPane;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;

public class
Browser extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cronos Browser");

        BorderPane mainPane = new BorderPane();
        mainPane.setId("mainPanelB");

        Scene scene = new Scene(mainPane, 1000, 666);
        scene.getStylesheets().add("./Assets/style.css");

        TopPane topPane = new TopPane();
        CenterPane centerPane = new CenterPane();
        BorderPane.setMargin(topPane, new Insets(10, 10, 10, 10));

        /*this.bottomPane = new BottomPane();
        final SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode, this.bottomPane);*/

        //mainPane.setCenter(swingNode);

        mainPane.setTop(topPane);
        mainPane.setCenter(centerPane);
        topPane.setPanes(mainPane, centerPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createSwingContent(SwingNode swingNode, JEditorPane pane) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(pane));
    }

    public static void main(String[] args){
        launch();
    }
}
