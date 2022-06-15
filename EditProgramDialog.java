package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;


class EditProgramDialog extends JDialog {
    private JDialog dialog;
    JPanel contents, buttons;
    SubmitButton submit;
    CancelButton cancel;
    GridBagConstraints c = new GridBagConstraints();

    JComponent[] firstColWidgets = new JComponent[5];
    DialogField[] secondColWidgets = new DialogField[5];

    EditProgramDialog(JFrame parent, String[] dataArray) {
        super(parent, LanguageManager.getTranslationsFromFile("EditProgram", LanguageManager.getCurrentLang()), true);
        dialog = this;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        contents = new JPanel(new GridBagLayout());
        
        contents.setBackground(Color.white);
        contents.setBorder(new EmptyBorder(10, 10, 10, 10));

        buttons = new JPanel(new FlowLayout());
        buttons.setBackground(Color.white);

        submit = new SubmitButton(this, dataArray, secondColWidgets);
        submit.setActionCommand("Submit");
        submit.addActionListener(new EditProgramBL());

        cancel = new CancelButton(this);
        cancel.setActionCommand("Cancel");
        cancel.addActionListener(new EditProgramBL());


        buttons.add(cancel); buttons.add(submit);


        firstColWidgets[0] = new JLabel(LanguageManager.getTranslationsFromFile("ProgramName", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[1] = new JLabel(LanguageManager.getTranslationsFromFile("ExecutablePath", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[2] = new JLabel(LanguageManager.getTranslationsFromFile("WorkingDir", LanguageManager.getCurrentLang()) + " *");
        firstColWidgets[3] = new JLabel(LanguageManager.getTranslationsFromFile("IconPath", LanguageManager.getCurrentLang()));
        firstColWidgets[4] = new JLabel(LanguageManager.getTranslationsFromFile("OptionalDescription", LanguageManager.getCurrentLang()));          
        
        for (int i = 0; i < 5; i++) {
            c.gridx = 0;            
            c.gridy = i;

            c.ipady = 10;
            c.ipadx = 6;

            c.fill = GridBagConstraints.HORIZONTAL;
            contents.add(firstColWidgets[i], c);
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

}

class EditProgramBL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Cancel")) {
            CancelButton a = (CancelButton) e.getSource();

            a.buttondialog.dispose();
        } else if (e.getActionCommand().equals("Submit")) {
            SubmitButton a = (SubmitButton) e.getSource();
            DialogField[] textFields = a.tf;
            String[] da = a.datas;
            for (int i = 0; i < 5; i++) {
                String t = textFields[i].getText();
                if (t.equals("") && textFields[i].isMandatory) {
                    JOptionPane.showMessageDialog(a.buttondialog, "Fields with \"*\" are mandatory!", "Error", JOptionPane.ERROR_MESSAGE); 
                    return;
                }
                da[i] = t;
            }

            da[5] = "true";
            a.buttondialog.dispose();

        } else if (e.getActionCommand().equals("BrowseEP")) {
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
        } else if (e.getActionCommand().equals("BrowseEP")) {
            BrowseButton a = (BrowseButton) e.getSource();
            DialogField[] textFields = a.tf;
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int r = fc.showOpenDialog(a.buttondialog);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                textFields[3].setText(path);                               
            }
        }
    }
}

