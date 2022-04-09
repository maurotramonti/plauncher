package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;


class AboutMenuHandler extends PLauncher implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("About PLauncher")) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("VersionString", lang), "Version info", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(SysConst.getLogoPath()));
        }

        else if (e.getActionCommand().equals("About Java")) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getJavaVersionString(lang), "About Java", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(SysConst.getJavaLogoPath()));
        }
    }
}