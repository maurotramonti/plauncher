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
            new AddProgramDialog(frame);
       
        } else if (e.getActionCommand().equals("Modify")) {
            if (frame.programsList.isSelectionEmpty()) return;
            File conffile = new File(SysConst.getPrePath() + "programs\\" + frame.rightPane[frame.programsList.getSelectedIndex()].getTitle() + ".txt");
            EditProgramDialog dialog = new EditProgramDialog(frame, conffile);
        } else if (e.getActionCommand().equals("Delete")) {
            if (frame.programsList.isSelectionEmpty()) return;
            File conffile = new File(SysConst.getPrePath() + "programs" + File.separator + frame.rightPane[frame.programsList.getSelectedIndex()].getTitle() + ".txt");
            if(conffile.delete()) {
                frame.loadPrograms();
            } else JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramDeletingError", lang), LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
        }

    }
    
}