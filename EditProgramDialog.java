import org.apache.commons.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;


class EditProgramDialog extends JDialog {
    private PLauncherFrame frame;
    private JDialog dialog;

    private File confFile;
    JPanel contents, buttons;
    GridBagConstraints c = new GridBagConstraints();

    JComponent[] firstColWidgets = new JComponent[5];
    DialogField[] secondColWidgets = new DialogField[5];

    EditProgramDialog(PLauncherFrame parent, File conffile) {
        super(parent, LanguageManager.getTranslationsFromFile("EditProgram", LanguageManager.getCurrentLang()), true);
        dialog = this; frame = parent; confFile = conffile;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        contents = new JPanel(new GridBagLayout());
        
        contents.setBackground(Color.white);
        contents.setBorder(new EmptyBorder(10, 10, 10, 10));

        buttons = new JPanel(new FlowLayout());
        buttons.setBackground(Color.white);

        buttons.add(new CancelButton()); buttons.add(new SubmitButton());


        firstColWidgets[0] = new JLabel(LanguageManager.getTranslationsFromFile("ProgramName", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[1] = new JLabel(LanguageManager.getTranslationsFromFile("ExecutablePath", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[2] = new JLabel(LanguageManager.getTranslationsFromFile("WorkingDir", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[3] = new JLabel(LanguageManager.getTranslationsFromFile("IconPath", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[4] = new JLabel(LanguageManager.getTranslationsFromFile("OptionalDescription", LanguageManager.getCurrentLang()));          
        
        for (int i = 0; i < 5; i++) {
            c.gridx = 0;            
            c.gridy = i;

            c.ipady = 10;
            c.ipadx = 6;

            c.fill = GridBagConstraints.HORIZONTAL;
            contents.add(firstColWidgets[i], c);
        }

        String[] dataArray = new String[5];
        Scanner s = null;
        try {
            s = new Scanner(conffile);
            for (int i = 0; i < 5; i++) {
                dataArray[i] = s.nextLine();
            }
            s.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, LanguageManager.getTranslationsFromFile("CantReadConffile") + '\n' + ex.getMessage(), LanguageManager.getTranslationsFromFile("Error"), JOptionPane.ERROR_MESSAGE);
            s.close();
            
        }


        secondColWidgets[0] = new DialogField(dataArray[0], true);
        secondColWidgets[1] = new DialogField(dataArray[1], true);
        secondColWidgets[2] = new DialogField(dataArray[2], true);
        secondColWidgets[3] = new DialogField(dataArray[3], false);
        secondColWidgets[4] = new DialogField(dataArray[4], false);

        for (int i = 0; i < 5; i++) {
            c.gridx = 1;
            c.gridy = i;

            c.ipady = 10;
            c.ipadx = 6;

            c.fill = GridBagConstraints.HORIZONTAL;
            
            contents.add(secondColWidgets[i], c);
        }

        BrowseButton browseExePath = new BrowseButton(secondColWidgets, this);
        BrowseButton browseWDPath = new BrowseButton(secondColWidgets, this);
        BrowseButton browseIconPath = new BrowseButton(secondColWidgets, this);

        browseExePath.setActionCommand("BrowseEP"); browseExePath.addActionListener(new EditProgramBL());
        browseWDPath.setActionCommand("BrowseWDP"); browseWDPath.addActionListener(new EditProgramBL());
        browseIconPath.setActionCommand("BrowseIP"); browseIconPath.addActionListener(new EditProgramBL());        

        c.gridx = 2;
        c.gridy = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        contents.add(browseExePath, c);

        c.gridy = 2;
        contents.add(browseWDPath, c);
        
        c.gridy = 3;
        contents.add(browseIconPath, c);


        this.add(contents, BorderLayout.NORTH);
        this.add(buttons, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    class CancelButton extends JButton implements ActionListener {
        CancelButton() {
            super(LanguageManager.getTranslationsFromFile("Cancel"));
            setBackground(Color.white); addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }

    class SubmitButton extends JButton implements ActionListener {
        SubmitButton() {
            super(LanguageManager.getTranslationsFromFile("Submit"));
            setBackground(Color.white); addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 5; i++) {
                if (secondColWidgets[i].getText().equals("") && secondColWidgets[i].isMandatory) {
                    JOptionPane.showMessageDialog(dialog, LanguageManager.getTranslationsFromFile("FieldsAreMandatory"), LanguageManager.getTranslationsFromFile("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!(new File(secondColWidgets[3].getText()).exists())) {
                int r = JOptionPane.showConfirmDialog(dialog, LanguageManager.getTranslationsFromFile("ImageDoesntExist"), LanguageManager.getTranslationsFromFile("Warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (r == JOptionPane.NO_OPTION) return;
            }

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(confFile));
                for (int i = 0; i < 5; i++) {
                    bw.write(secondColWidgets[i].getText() + '\n');
                }
                bw.close();
                FileUtils.moveFile(confFile, new File(SysConst.getPrePath() + "programs\\" + secondColWidgets[0].getText() + ".txt"));
                frame.loadPrograms();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, LanguageManager.getTranslationsFromFile("EditProgramError") + '\n' + ex.getMessage(), LanguageManager.getTranslationsFromFile("Error"), JOptionPane.ERROR_MESSAGE);
            } finally {
                dialog.dispose();
            }
        }
    }

}

class EditProgramBL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("BrowseEP")) {
            BrowseButton a = (BrowseButton) e.getSource();
            DialogField[] textFields = a.tf;
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int r = fc.showOpenDialog(a.buttondialog);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                textFields[1].setText(path);                               
            }

        } else if (e.getActionCommand().equals("BrowseWDP")) {
            BrowseButton a = (BrowseButton) e.getSource();
            DialogField[] textFields = a.tf;
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);
            int r = fc.showOpenDialog(a.buttondialog);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                textFields[2].setText(path);                               
            }
        } else if (e.getActionCommand().equals("BrowseIP")) {
            BrowseButton a = (BrowseButton) e.getSource();
            DialogField[] textFields = a.tf;
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "gif", "png"));
            fc.setAcceptAllFileFilterUsed(false);
            int r = fc.showOpenDialog(a.buttondialog);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                textFields[3].setText(path);                               
            }
        }
    }
}

