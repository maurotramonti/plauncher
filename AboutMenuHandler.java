package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;
import java.net.*;


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
        else if (e.getActionCommand().equals("Changelog")) {
            frame.loadChangelog();
        }
        else if (e.getActionCommand().equals("Check updates")) {
            final int internalVersion = 070;
            final String internalVersionString = new String("070");
            try {
                URL url = new URL("https://raw.githubusercontent.com/maurotramonti/plauncher/main/latest.txt");

                InputStream is = url.openStream();
                // Stream to the destionation file
                FileOutputStream fos = new FileOutputStream(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
		        // Read bytes from URL to the local file
                byte[] buffer = new byte[4096];
                int bytesRead = 0;

                System.out.println("Downloading " + "latest.txt");
                while ((bytesRead = is.read(buffer)) != -1) {
        	        fos.write(buffer, 0, bytesRead);
                }

                // Close destination stream
                fos.close();
                // Close URL stream
                is.close();
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
                Scanner scanner = new Scanner(file);
                String l = new String();
                l = scanner.nextLine();
                int internalVersionRead = Integer.parseInt(l);
                System.out.println(internalVersionRead);
                if (internalVersionRead > internalVersion) {
                    JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("CUTxtY", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("CUTxtN", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
                }
                scanner.close();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(internalVersionString);
                bw.close();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("CheckUpdatesErr", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
}