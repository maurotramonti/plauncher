package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;


class FileMenuHandler extends PLauncher implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Create new")) {
            String programName, executablePath, workingDirectory, iconPath, optionalDescription;
            try {
                do {
                programName = JOptionPane.showInputDialog(frame.getFrame(), "Program name: ", "Example");
                } while (programName.equals(""));
                do {
                executablePath = JOptionPane.showInputDialog(frame.getFrame(), "Executable path: ");
                } while (executablePath.equals(""));
                do {
                workingDirectory = JOptionPane.showInputDialog(frame.getFrame(), "Working directory: ");
                } while (workingDirectory.equals(""));
                do {
                iconPath = JOptionPane.showInputDialog(frame.getFrame(), "Icon path: ");
                } while (iconPath.equals(""));
                optionalDescription = JOptionPane.showInputDialog(frame.getFrame(), "Optional description: ", "No description");
            } catch (NullPointerException ex) {
                return;
            }
            try {
                File outFile = new File(SysConst.getPrePath() + "programs" + File.separator + programName + ".txt");
                outFile.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(outFile.getAbsolutePath()));
                bw.write(programName + '\n');
                bw.write(workingDirectory + '\n');
                bw.write(executablePath + '\n');
                bw.write(iconPath + '\n');
                bw.write(optionalDescription);
                bw.close();
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(frame.getFrame(), "Impossibile aggiungere un nuovo programma!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    
}