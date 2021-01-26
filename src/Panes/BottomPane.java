package Panes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BottomPane extends JEditorPane {
    public BottomPane() throws IOException {
        setEditable(false);
        setBackground( new Color(0, 0, 0, 0));

        setPage("http://facebook.com");
        //setOpaque(false);
    }
}
