package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;

class ProgramTab extends JPanel {
    String programName, workingDir, executablePath, iconPath, optionalDescription;
    
    ProgramTab(String programname, String workingdir, String executablepath, String iconpath, String optionaldescr, int lang) {
        super(new BorderLayout(20,20));
        programName = programname;
        workingDir = workingdir;
        executablePath = executablepath;
        iconPath = iconpath;
        optionalDescription = optionaldescr;
        JLabel title = new JLabel("   " + programName, new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(64, 64,  Image.SCALE_DEFAULT)), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));

        JButton launchButton = new JButton(LanguageManager.getTranslationsFromFile("Launch", lang), new ImageIcon(SysConst.getPrePath() + "images" + File.separator + "right_arrow.png"));
        launchButton.setHorizontalTextPosition(SwingConstants.LEFT);
        launchButton.addActionListener(new LaunchButtonListener());

        JLabel description = new JLabel(optionalDescription);
        JTextArea usefulInfoText = new JTextArea(LanguageManager.getTranslationsFromFile("WorkingDir", lang) + "\n\n" + workingDir + "\n\n" + LanguageManager.getTranslationsFromFile("ExecutablePath", lang) + "\n\n" + executablePath);
        usefulInfoText.setEditable(false);
        JScrollPane usefulInformations = new JScrollPane(usefulInfoText);
        usefulInformations.setPreferredSize(new Dimension(200, 0));

        this.add(title , BorderLayout.NORTH);
        this.add(launchButton, BorderLayout.SOUTH);
        this.add(usefulInformations, BorderLayout.WEST);
        this.add(description, BorderLayout.CENTER);
        
    }

    public String getProgramName() {
        return programName;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public String getWorkingDirectory() {
        return workingDir;
    }

    public String getOptionalDescription() {
        return optionalDescription;
    }

    public String getIconPath() {
        return iconPath;
    }
}

class LaunchButtonListener extends PLauncher implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        try {
            ProgramTab current = frame.pTabs[frame.tPane.getSelectedIndex()];
            ProcessBuilder pb = new ProcessBuilder(current.getExecutablePath());
            pb.directory(new File(current.getWorkingDirectory()));
            Process p = pb.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("LaunchError", lang), LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
        }
    }
} 