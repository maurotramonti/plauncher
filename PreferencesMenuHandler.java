package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Scanner;
import java.io.*;

class PreferencesMenuHandler extends PLauncher implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Language")) {
           LanguageManager lm = new LanguageManager();
           lm.actionPerformed(null);            
        }

    }
}