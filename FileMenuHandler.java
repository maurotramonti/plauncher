package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;


class FileMenuHandler extends PLauncher implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Create new")) {
            String[] datas = new String[5];
            AddProgramDialog dialog = new AddProgramDialog(frame.getFrame(), datas);
            if (datas[0] == null) return; // if the user has chosen "Cancel", is enough to check if the first mandatory field is null
            try {
                File outFile = new File(SysConst.getPrePath() + "programs" + File.separator + datas[0] + ".txt");
                outFile.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(outFile.getAbsolutePath()));
                bw.write(datas[0] + '\n');
                bw.write(datas[2] + '\n');
                bw.write(datas[1] + '\n');
                bw.write(datas[3] + '\n');
                bw.write(datas[4]);
                bw.close();
                frame.tPane.removeAll();
                frame.loadPrograms();
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramAddingError", lang), LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
                return;
            }

       
       
        } else if (e.getActionCommand().equals("Modify")) {
            if (frame.tPane.getSelectedIndex() == -1) return;
            String[] datas = new String[6];
            datas[5] = "false";
            ProgramTab current = frame.pTabs[frame.tPane.getSelectedIndex()];
            File conffile = new File(SysConst.getPrePath() + "programs\\" + current.getProgramName() + ".txt");
            try {
                Scanner scanner = new Scanner(conffile);
                for (int i = 0; i < 5; i++) {
                    if (scanner.hasNextLine() == false) {
                        datas[i] = "";
                        continue;
                    }
                    datas[i] = scanner.nextLine();
                }
                scanner.close();
                EditProgramDialog dialog = new EditProgramDialog(frame.getFrame(), datas);
                System.out.println(datas[5]);
                if (datas[5].equals("false")) return;
                BufferedWriter bw = new BufferedWriter(new FileWriter(conffile));
                bw.write(datas[0] + '\n');
                bw.write(datas[2] + '\n');
                bw.write(datas[1] + '\n');
                bw.write(datas[3] + '\n');
                bw.write(datas[4]);
                bw.close();
                conffile.renameTo(new File(SysConst.getPrePath() + "programs\\" + datas[0] + ".txt"));
                frame.tPane.removeAll();
                frame.loadPrograms();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


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