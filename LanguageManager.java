package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class LanguageManager extends PLauncher implements ActionListener {
    private int lang;
    public static final String[] langs = {"Italiano", "English"};
    public void actionPerformed(ActionEvent e) { 
        try {
            lang = frame.getLang();
        } catch (Exception exx) {
            lang = 0;
        }
        String clg;
        if(lang == 0) clg = new String("English");
        else if(lang == 1) clg = new String("Italiano");
        else clg = new String("none");
        try {
            String s;
            try {
                s = (String) JOptionPane.showInputDialog(frame.getFrame(), getTranslationsFromFile("ChooseLanguage", lang), getTranslationsFromFile("Warning", lang), JOptionPane.PLAIN_MESSAGE, null, langs, clg);
            } catch (NullPointerException ex3) {
                s = (String) JOptionPane.showInputDialog(null, getTranslationsFromFile("ChooseLanguage", lang), getTranslationsFromFile("Warning", lang), JOptionPane.PLAIN_MESSAGE, null, langs, clg);
            }
            if (s != null) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "language.txt"));
                bw.write(s);
                bw.close();
            } else {
                BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "language.txt"));
                bw.write("English");
                bw.close();
            }
        } catch (IOException ex) {
            try {
                JOptionPane.showMessageDialog(frame.getFrame(), getTranslationsFromFile("PermsError", lang));
            } catch (NullPointerException ex2) {
                JOptionPane.showMessageDialog(null, getTranslationsFromFile("PermsError", lang));
            }
            return;
        }
    }

    public static String[] getTranslatedStrings(int value, int lang) {
        switch(value) {
            case 0:
                if (true) {
                    String[] tmp = {"Close", "New", "Open", "Open folder", "Save", "Save as...", "Exit", "Undo", "Redo", "Cut", "Copy", "Paste", "Language", "Tab size", "Automatic newline", "App theme", "Check updates", "About MText", "About Java"};
                    return tmp;
                }            
            case 3:  
                if (true) {              
                    String[] tmp = {getTranslationsFromFile("Close", lang), getTranslationsFromFile("New", lang), getTranslationsFromFile("Open", lang), getTranslationsFromFile("OpenFolder", lang), getTranslationsFromFile("Save", lang), getTranslationsFromFile("SaveAs", lang), getTranslationsFromFile("Exit", lang), getTranslationsFromFile("Undo", lang), getTranslationsFromFile("Redo", lang), getTranslationsFromFile("Cut", lang), getTranslationsFromFile("Copy", lang), getTranslationsFromFile("Paste", lang), getTranslationsFromFile("Language", lang), getTranslationsFromFile("TabLength", lang), getTranslationsFromFile("AutomaticNewline", lang), getTranslationsFromFile("AppTheme", lang), getTranslationsFromFile("CheckUpdates", lang), getTranslationsFromFile("InfoAboutMText", lang),  getTranslationsFromFile("AboutJava", lang)};
                    return tmp;
                }
                
            case 1:
                if (true) {
                    String[] tmp = {"2 " + getTranslationsFromFile("Spaces", lang), "4 " + getTranslationsFromFile("Spaces", lang), "8 " + getTranslationsFromFile("Spaces", lang)};
                    return tmp;
                } 
            case 2:
                if (true) {
                    String[] tmp = {getTranslationsFromFile("Yes", lang), getTranslationsFromFile("No", lang)};
                    return tmp;
                }
        }
        String[] tmp = {"none", "none"};
        return tmp;
    }
    public static String getTranslationsFromFile(String property, int lang) {
        String prefix, contents = "";
        switch (lang) {
            case 0: 
                prefix = SysConst.getPrePath() + File.separator + "langs" + File.separator + "eng" + File.separator;
                break;
            case 1:
                prefix = SysConst.getPrePath() + File.separator + "langs" + File.separator + "ita" + File.separator;
                break;
            default:
                prefix = SysConst.getPrePath() + File.separator + "langs" + File.separator + "eng" + File.separator;
                break;
        }
        try {
            File lf = new File(prefix + property + ".txt");
            Scanner s = new Scanner(lf);
            do {
                contents = contents + s.nextLine() + '\n';
            } while (s.hasNextLine());
            s.close();
        } catch (FileNotFoundException e) {}
        return contents;
    }
    public static String getJavaVersionString(int lang) {
        if (lang == 0) return "Version: " + System.getProperty("java.vm.version") + "\nInstall path:  " + System.getProperty("java.home") + "\nOperating system: " + System.getProperty("os.name");
        else if (lang == 1) return "Versione: " + System.getProperty("java.vm.version") + "\nPercorso di installazione:  " + System.getProperty("java.home") + "\nSistema operativo: " + System.getProperty("os.name");
        return "none";
    }
    public static String getTranslatedString(int value, int lang) {
            System.out.println("Missing translation: number " + value);
            return "none";        
    }
    

}
