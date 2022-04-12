package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;

class ProgramTab extends JPanel {
    String programName, workingDir, executablePath, iconPath, optionalDescription;

    ProgramTab(String programname, String workingdir, String executablepath, String iconpath, String optionaldescr) {
        super(new BorderLayout(20,20));
        programName = programname;
        workingDir = workingdir;
        executablePath = executablepath;
        iconPath = iconpath;
        optionalDescription = optionaldescr;
        JLabel title = new JLabel("   " + programName, new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(64, 64,  Image.SCALE_DEFAULT)), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));

        JButton launchButton = new JButton("Launch", new ImageIcon(SysConst.getPrePath() + "images" + File.separator + "right_arrow.png"));
        launchButton.setHorizontalTextPosition(SwingConstants.LEFT);
        launchButton.addActionListener(new LaunchButtonListener());

        JLabel description = new JLabel(optionalDescription);
        JTextArea usefulInfoText = new JTextArea("Working directory: \n\n" + workingDir + "\n\n" + "Executable path: \n\n" + executablePath);
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
    public void actionPerformed(ActionEvent e) {
        try {
            ProgramTab current = frame.pTabs[frame.tPane.getSelectedIndex()];
            ProcessBuilder pb = new ProcessBuilder(current.getExecutablePath());
            pb.directory(new File(current.getWorkingDirectory()));
            Process p = pb.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame.getFrame(), "Impossibile lanciare il programma! (hai i privilegi?)\nAssicurati che i dati inseriti siano corretti: puoi modificarli andando su File > Modifica scheda.", "Errore!!!", JOptionPane.ERROR_MESSAGE);
        }
    }
} 