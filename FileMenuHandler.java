package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.StringTokenizer;


class FileMenuHandler extends PLauncher implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Create new")) {
            String programName, executablePath, workingDirectory, iconPath, optionalDescription;
            String exeName = new String("");
            try {
                do {
                    programName = JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramName", lang), "Example");
                    if (programName.contains("\\") || programName.contains("/")) {
                        JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("DontInsertCharacters", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.WARNING_MESSAGE);
                        programName = new String("");
                    }
                } while (programName.equals(""));
                do {
                executablePath = JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ExecutablePath", lang)).replaceAll("\"", "");
                } while (executablePath.equals(""));
                StringTokenizer st = new StringTokenizer(executablePath, "\\");
                int tcount = st.countTokens();
                
                for (int i = 0; i < tcount; i++) {
                    exeName = new String(st.nextToken());
                }
                do {
                workingDirectory = JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("WorkingDir", lang) + ": ", executablePath.replace(exeName, ""));
                } while (workingDirectory.equals(""));
                iconPath = JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("IconPath", 0)).replaceAll("\"", "");
                optionalDescription = JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("OptionalDescription", lang));
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
                frame.tPane.removeAll();
                frame.loadPrograms();
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramAddingError", lang), LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (e.getActionCommand().equals("Modify")) {
            if (frame.tPane.getSelectedIndex() == -1) return;
            ProgramTab current = frame.pTabs[frame.tPane.getSelectedIndex()];
            System.out.println("Programma da modificare: " + current.getProgramName());
            File conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
            String[] options = {LanguageManager.getTranslationsFromFile("ProgramName", lang), LanguageManager.getTranslationsFromFile("ExecutablePath", lang), LanguageManager.getTranslationsFromFile("WorkingDir", lang), LanguageManager.getTranslationsFromFile("IconPath", lang), LanguageManager.getTranslationsFromFile("OptionalDescription", lang)};
            String s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("SelectProperty", lang), LanguageManager.getTranslationsFromFile("EditProgram", lang), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (s.equals(null)) return;
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("none"));
                
                if(s.equals(LanguageManager.getTranslationsFromFile("ProgramName", lang))) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramName", lang), LanguageManager.getTranslationsFromFile("EditProgram", lang), JOptionPane.QUESTION_MESSAGE);
                        if (s.equals(null)) return;
                    } while (s.equals(""));
                    conffile.delete();
                    conffile = new File(SysConst.getPrePath() + "programs" + File.separator + s + ".txt");
                    conffile.createNewFile();
                    bw = new BufferedWriter(new FileWriter(conffile.getAbsolutePath()));
                    bw.write(s + '\n');
                    bw.write(current.getWorkingDirectory() + '\n');
                    bw.write(current.getExecutablePath() + '\n');
                    bw.write(current.getIconPath() + '\n');
                    bw.write(current.getOptionalDescription());                
                }
                else if(s.equals(LanguageManager.getTranslationsFromFile("WorkingDir", lang))) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("WorkingDir", lang), LanguageManager.getTranslationsFromFile("EditProgram", lang), JOptionPane.QUESTION_MESSAGE);
                        if (s.equals(null)) return;
                    } while (s.equals(""));
                    conffile.delete();
                    conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
                    conffile.createNewFile();
                    bw = new BufferedWriter(new FileWriter(conffile.getAbsolutePath()));
                    bw.write(current.getProgramName() + '\n');
                    bw.write(s + '\n');
                    bw.write(current.getExecutablePath() + '\n');
                    bw.write(current.getIconPath() + '\n');
                    bw.write(current.getOptionalDescription());                
                }
                else if(s.equals(LanguageManager.getTranslationsFromFile("ExecutablePath", lang))) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ExecutablePath", lang), LanguageManager.getTranslationsFromFile("EditProgram", lang), JOptionPane.QUESTION_MESSAGE);
                        if (s.equals(null)) return;
                    } while (s.equals(""));
                    conffile.delete();
                    conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
                    conffile.createNewFile();
                    bw = new BufferedWriter(new FileWriter(conffile.getAbsolutePath()));
                    bw.write(current.getProgramName() + '\n');
                    bw.write(current.getWorkingDirectory() + '\n');
                    bw.write(s + '\n');
                    bw.write(current.getIconPath() + '\n');
                    bw.write(current.getOptionalDescription());                
                }
                else if(s.equals(LanguageManager.getTranslationsFromFile("IconPath", lang))) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("IconPath", lang), LanguageManager.getTranslationsFromFile("EditProgram", lang), JOptionPane.QUESTION_MESSAGE);
                        if (s.equals(null)) return;
                    } while (s.equals(""));
                    conffile.delete();
                    conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
                    conffile.createNewFile();
                    bw = new BufferedWriter(new FileWriter(conffile.getAbsolutePath()));
                    bw.write(current.getProgramName() + '\n');
                    bw.write(current.getWorkingDirectory() + '\n');
                    bw.write(current.getExecutablePath() + '\n');
                    bw.write(s + '\n');
                    bw.write(current.getOptionalDescription());                
                }
                else if(s.equals(LanguageManager.getTranslationsFromFile("OptionalDescription", lang))) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("OptionalDescription", lang), LanguageManager.getTranslationsFromFile("EditProgram", lang), JOptionPane.QUESTION_MESSAGE);
                        if (s.equals(null)) return;
                    } while (s.equals(""));
                    conffile.delete();
                    conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
                    conffile.createNewFile();
                    bw = new BufferedWriter(new FileWriter(conffile.getAbsolutePath()));
                    bw.write(current.getProgramName() + '\n');
                    bw.write(current.getWorkingDirectory() + '\n');
                    bw.write(current.getExecutablePath() + '\n');
                    bw.write(current.getIconPath() + '\n');
                    bw.write(s);                
                }
                bw.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame.getFrame(), "Errore durante la modifica!", LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
            }
                frame.tPane.removeAll();
                frame.loadPrograms();

        } else if (e.getActionCommand().equals("Delete")) {
            if (frame.tPane.getSelectedIndex() == -1) return;
            ProgramTab current = frame.pTabs[frame.tPane.getSelectedIndex()];
            File conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
            if(conffile.delete()) {
                frame.tPane.removeAll();
                frame.loadPrograms();
            } else JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramDeletingError", lang), LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
        }

    }
    
}