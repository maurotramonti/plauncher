import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;


class AddProgramDialog extends JDialog {
    private final JDialog dialog;

    private final PLauncherFrame frame;
    JPanel contents, buttons;
    GridBagConstraints c = new GridBagConstraints();

    JComponent[] firstColWidgets = new JComponent[5];
    DialogField[] secondColWidgets = new DialogField[5];

    AddProgramDialog(PLauncherFrame parent) {
        super(parent, LanguageManager.getTranslationsFromFile("NewProgram"), true);
        dialog = this; frame = parent;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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

        secondColWidgets[0] = new DialogField(true);
        secondColWidgets[1] = new DialogField(true);
        secondColWidgets[2] = new DialogField(true);
        secondColWidgets[3] = new DialogField(true);
        secondColWidgets[4] = new DialogField(false);

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

        browseExePath.setActionCommand("BrowseEP");
        browseWDPath.setActionCommand("BrowseWDP");
        browseIconPath.setActionCommand("BrowseIP");

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
            addActionListener(this); setBackground(Color.white);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 5; i++) {
                String t = secondColWidgets[i].getText();
                if (t.equals("") && secondColWidgets[i].isMandatory) {
                    JOptionPane.showMessageDialog(dialog, LanguageManager.getTranslationsFromFile("FieldsAreMandatory"), LanguageManager.getTranslationsFromFile("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!(new File(secondColWidgets[3].getText()).exists())) {
                    int r = JOptionPane.showConfirmDialog(dialog, LanguageManager.getTranslationsFromFile("ImageDoesntExist"), LanguageManager.getTranslationsFromFile("Warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (r == JOptionPane.NO_OPTION) return;
                }
            }
            try {
                File outFile = new File(SysConst.getPrePath() + "programs" + File.separator + secondColWidgets[0].getText() + ".txt");
                if(!outFile.createNewFile()) throw new IOException();
                BufferedWriter bw = new BufferedWriter(new FileWriter(outFile.getAbsolutePath()));
                bw.write(secondColWidgets[0].getText() + '\n');
                bw.write(secondColWidgets[2].getText() + '\n');
                bw.write(secondColWidgets[1].getText() + '\n');
                bw.write(secondColWidgets[3].getText() + '\n');
                bw.write(secondColWidgets[4].getText());
                bw.close();
                frame.loadPrograms();
                dialog.dispose();
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ProgramAddingError") + '\n' + exc.getMessage(), LanguageManager.getTranslationsFromFile("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }


    }

}

class DialogField extends JTextField {
    boolean isMandatory;
    DialogField(boolean im) {
        super(24);
        isMandatory = im;
    }

    DialogField(String text, boolean im) {
        super(24);
        this.setText(text);
        isMandatory = im;
    }
}

class AddProgramBL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("BrowseEP")) {
            BrowseButton a = (BrowseButton) e.getSource();
            DialogField[] textFields = a.tf;
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Executable files", "exe"));
            fc.setAcceptAllFileFilterUsed(false);
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





class BrowseButton extends JButton {
    DialogField[] tf;
    JDialog buttondialog;
    BrowseButton(DialogField[] textFieldsPassed, JDialog bd) {
        super(LanguageManager.getTranslationsFromFile("Browse"), new ImageIcon(SysConst.getPrePath() + "images\\" + "browse.png", LanguageManager.getTranslationsFromFile("Browse")));
        tf = textFieldsPassed;
        buttondialog = bd;
        this.setHorizontalTextPosition(SwingConstants.LEFT); addActionListener(new AddProgramBL());
    }
}
