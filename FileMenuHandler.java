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
                programName = JOptionPane.showInputDialog(frame.getFrame(), "Program name: ", "Example");
                if (programName.contains("\\") || programName.contains("/")) {
                    JOptionPane.showMessageDialog(frame.getFrame(), "Si prega di non inserire il carattere \"\\\" o \"/\" nel nome del programma.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    programName = new String("");
                }
                } while (programName.equals(""));
                do {
                executablePath = JOptionPane.showInputDialog(frame.getFrame(), "Executable path: ");
                } while (executablePath.equals(""));
                StringTokenizer st = new StringTokenizer(executablePath, "\\");
                int tcount = st.countTokens();
                
                for (int i = 0; i < tcount; i++) {
                    exeName = new String(st.nextToken());
                }
                do {
                workingDirectory = JOptionPane.showInputDialog(frame.getFrame(), "Working directory: ", executablePath.replace(exeName, ""));
                } while (workingDirectory.equals(""));
                iconPath = JOptionPane.showInputDialog(frame.getFrame(), "Icon path: ");
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
                frame.tPane.removeAll();
                frame.loadPrograms();
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(frame.getFrame(), "Impossibile aggiungere un nuovo programma!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (e.getActionCommand().equals("Modify")) {
            if (frame.tPane.getSelectedIndex() == -1) return;
            ProgramTab current = frame.pTabs[frame.tPane.getSelectedIndex()];
            System.out.println("Programma da modificare: " + current.getProgramName());
            File conffile = new File(SysConst.getPrePath() + "programs" + File.separator + current.getProgramName() + ".txt");
            String[] options = {"Nome", "Percorso eseguibile", "Cartella di lavoro", "Percorso icona", "Descrizione opzionale"};
            String s = (String) JOptionPane.showInputDialog(frame.getFrame(), "Seleziona la proprietà da modificare:", "Modifica programma", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (s.equals(null)) return;
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("none"));
                
                if(s.equals("Nome")) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), "Program name:", "Modifica programma", JOptionPane.QUESTION_MESSAGE);
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
                else if(s.equals("Cartella di lavoro")) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), "Working directory:", "Modifica programma", JOptionPane.QUESTION_MESSAGE);
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
                else if(s.equals("Percorso eseguibile")) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), "Executable path:", "Modifica programma", JOptionPane.QUESTION_MESSAGE);
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
                else if(s.equals("Percorso icona")) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), "Icon path:", "Modifica programma", JOptionPane.QUESTION_MESSAGE);
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
                else if(s.equals("Descrizione opzionale")) {
                    do {
                        s = (String) JOptionPane.showInputDialog(frame.getFrame(), "Optional description:", "Modifica programma", JOptionPane.QUESTION_MESSAGE);
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
                JOptionPane.showMessageDialog(frame.getFrame(), "Errore durante la modifica!", "Errore", JOptionPane.ERROR_MESSAGE);
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
            } else JOptionPane.showMessageDialog(frame.getFrame(), "Impossibile eliminare il programma!", "Errore", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}