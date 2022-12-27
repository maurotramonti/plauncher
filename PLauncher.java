import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;
import java.net.*;
import org.apache.commons.io.FileUtils;


class SysConst {
    public static String getPrePath() {
        return System.getenv("LOCALAPPDATA") +  "\\plauncher\\";
    }
    public static String getLogoPath() {
        return "plauncher.png";
    }
    public static String getJavaLogoPath() {
        return "javalogo.png";
    }
}

class PLauncherFrame extends JFrame {
    private final JFrame frame;
    private int lang = 1;

    protected JMenuItem modifyButton, deleteButton;

    protected JList<ImageIcon> programsList = new JList<>();

    private final JSplitPane splitPane = new JSplitPane();

    protected ProgramInformationPanel[] rightPane;


    PLauncherFrame() {
        super("P-Launcher");
        frame = this;
        frame.setMinimumSize(new Dimension(735, 390));
        frame.setPreferredSize(new Dimension(735, 390));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(SysConst.getLogoPath()));

        loadLanguage();

        try {
            BufferedReader br = new BufferedReader(new FileReader(SysConst.getPrePath() + "conf" + File.separator + "fb.txt"));
            if (br.readLine().equals("1")) {
                loadChangelog();
                br.close();
                BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "fb.txt"));
                bw.write("0");
                bw.close();
            } 
        } catch (IOException ignored) {}

        try {
            BufferedReader br = new BufferedReader(new FileReader(SysConst.getPrePath() + "conf" + File.separator + "lastcheck.txt"));
            String lc = br.readLine();
            final long millis = Long.parseLong(lc);
            br.close();
            if ((System.currentTimeMillis() - millis) >= 172800000) { // sono passati almeno 2 giorni
                checkUpdates(true);
            }
        } catch (IOException ignored) {}


        loadPrograms();

        JMenu fileMenu = new JMenu("File");
        JMenu prefMenu = new JMenu(LanguageManager.getTranslationsFromFile("Preferences", lang));
        JMenu aboutMenu = new JMenu(LanguageManager.getTranslationsFromFile("Help", lang));

        JMenuItem createNewButton = new JMenuItem(LanguageManager.getTranslationsFromFile("NewProgram", lang));
        createNewButton.addActionListener(new FileMenuHandler());
        createNewButton.setActionCommand("Create new");
        createNewButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(createNewButton);

        modifyButton = new JMenuItem(LanguageManager.getTranslationsFromFile("ModifyCurrent", lang));
        modifyButton.addActionListener(new FileMenuHandler());
        modifyButton.setActionCommand("Modify");
        modifyButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(modifyButton);

        deleteButton = new JMenuItem(LanguageManager.getTranslationsFromFile("DeleteProgram", lang));
        deleteButton.addActionListener(new FileMenuHandler());
        deleteButton.setActionCommand("Delete");
        deleteButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(deleteButton);


        fileMenu.addSeparator();

        JMenuItem exitButton = new JMenuItem(LanguageManager.getTranslationsFromFile("Exit", lang));
        exitButton.addActionListener(new FileMenuHandler());
        exitButton.setActionCommand("Exit");
        exitButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(exitButton);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        JMenuItem langButton = new JMenuItem(LanguageManager.getTranslationsFromFile("Language", lang));
        langButton.addActionListener(new PreferencesMenuHandler());
        langButton.setActionCommand("Language");

        prefMenu.add(langButton);

        menuBar.add(prefMenu);

        JMenuItem changelogButton = new JMenuItem("Changelog");
        changelogButton.addActionListener(new AboutMenuHandler());
        changelogButton.setActionCommand("Changelog");

        aboutMenu.add(changelogButton);

        JMenuItem checkUpdatesButton = new JMenuItem(LanguageManager.getTranslationsFromFile("CheckUpdates", lang));
        checkUpdatesButton.addActionListener(new AboutMenuHandler());
        checkUpdatesButton.setActionCommand("Check updates");

        aboutMenu.add(checkUpdatesButton);

        JMenuItem aboutJavaButton = new JMenuItem(LanguageManager.getTranslationsFromFile("AboutJava", lang));
        aboutJavaButton.addActionListener(new AboutMenuHandler());
        aboutJavaButton.setActionCommand("About Java");

        aboutMenu.add(aboutJavaButton);

        JMenuItem aboutProgramButton = new JMenuItem(LanguageManager.getTranslationsFromFile("InfoAboutPLauncher", lang));
        aboutProgramButton.addActionListener(new AboutMenuHandler());
        aboutProgramButton.setActionCommand("About PLauncher");

        aboutMenu.add(aboutProgramButton); 

        menuBar.add(aboutMenu);

        frame.setJMenuBar(menuBar);

        // Window Design


        programsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        programsList.setSelectedIndex(0); programsList.addListSelectionListener(new ProgramsListListener());

        splitPane.setBackground(Color.white); splitPane.setLeftComponent(new JScrollPane(programsList)); splitPane.setRightComponent(rightPane[programsList.getSelectedIndex()]);
        splitPane.getLeftComponent().setMinimumSize(new Dimension(64, 0));  // to avoid the "disappearance" of the JList because of stupid users
        ((JScrollPane) splitPane.getLeftComponent()).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(splitPane);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getLang() {
        return lang;
    }


    class ProgramInformationPanel extends JPanel {

        private String titleString, exePathString, workingDirString;

        ProgramInformationPanel(String name, String executablePath, String workingDirPath, String description, String imagePath) {
            super(new GridBagLayout()); setBackground(Color.white);
            GridBagConstraints gbc = new GridBagConstraints();

            exePathString = executablePath; workingDirString = workingDirPath; titleString = name;

            JLabel title = new JLabel("<html><h2>" + name + "</h2></html>", new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(38, 38, Image.SCALE_DEFAULT)), SwingConstants.LEFT);
            JLabel exePath = new JLabel("<html><b>" + LanguageManager.getTranslationsFromFile("ExecutablePath", lang) + ": </b>" + executablePath);
            JLabel workingDir = new JLabel("<html><b>" + LanguageManager.getTranslationsFromFile("WorkingDir", lang) + ": </b>" + workingDirPath);
            JTextPane description1 = new JTextPane();
            description1.setText(description); description1.setEditable(false);

            gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10);
            add(title, gbc); gbc.gridy++; gbc.anchor = GridBagConstraints.LINE_START; add(exePath, gbc); gbc.gridy++; add(workingDir, gbc);
            gbc.gridy++; add(description1, gbc); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridy++;
            add(new LaunchButton(), gbc);

        }

        ProgramInformationPanel() {
            super(new FlowLayout()); setBackground(Color.white); add(new JLabel(LanguageManager.getTranslationsFromFile("NoProgramAdded")));
        }

        public String getTitle() {
            return titleString;
        }

        public String getExePath() {
            return exePathString;
        }

        public String getWorkingDir() {
            return workingDirString;
        }
    }

    class LaunchButton extends JButton implements ActionListener {
        LaunchButton() {
            super(LanguageManager.getTranslationsFromFile("Launch", lang));
            addActionListener(this); setBackground(Color.white);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ProcessBuilder pb = new ProcessBuilder(rightPane[programsList.getSelectedIndex()].getExePath());
                pb.directory(new File(rightPane[programsList.getSelectedIndex()].getWorkingDir()));
                pb.start();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("LaunchError", lang), LanguageManager.getTranslationsFromFile("Error", lang), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ProgramsListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (programsList.isSelectionEmpty()) {
                modifyButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
            else {
                modifyButton.setEnabled(true);
                deleteButton.setEnabled(true);
                splitPane.setRightComponent(rightPane[programsList.getSelectedIndex()]);
            }
        }
    }

    protected void loadLanguage() {
        LanguageManager lm = new LanguageManager();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt");
            Scanner scanner = new Scanner(file);
            String l;
            if (!scanner.hasNextLine()) {
                lm.actionPerformed(null);
                loadLanguage();
            }
            l = scanner.nextLine();
            scanner.close();
            if (l.equals("Italiano")) lang = 1;
            else if (l.equals("English")) lang = 0;
            else {
                lm.actionPerformed(null);
                loadLanguage();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang) + '\n' + e.getMessage());
            lm.actionPerformed(null);
            loadLanguage();
        }
    }

    protected void loadChangelog() {
        JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("Changelog", lang), "Changelog", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void checkUpdates(boolean showOnlyIfPositive) {
        final int internalVersion = 120;
        try {
            URL url = new URL("https://raw.githubusercontent.com/maurotramonti/plauncher/main/conf/latest.txt");
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
            FileUtils.copyURLToFile(url, file, 10000, 10000);


            Scanner scanner = new Scanner(file);
            String l = scanner.nextLine();
            final int internalVersionRead = Integer.parseInt(l);
            if (internalVersionRead > internalVersion) {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CUTxtY", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (!showOnlyIfPositive) JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CUTxtN", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
            }
            scanner.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "lastcheck.txt"));
            bw.write(Long.toString(System.currentTimeMillis()));
            bw.close();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CheckUpdatesErr", lang) + "\nMessage:\n" + exception.getMessage(), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void loadPrograms() {
        File pDir = new File(SysConst.getPrePath() + "programs");
        String[] pList = pDir.list();
        DefaultListModel<ImageIcon> lm = new DefaultListModel<>();
        int oldSelectedIndex;
        if (programsList.isSelectionEmpty()) oldSelectedIndex = 0;
        else oldSelectedIndex = programsList.getSelectedIndex();
        if (pList != null) {
            rightPane = new ProgramInformationPanel[pList.length];
            int i, j = 0;
            try {
                for (String pPath : pList) {
                    i = 0;
                    File file = new File(SysConst.getPrePath() + "programs" + File.separator + pPath);
                    Scanner scanner = new Scanner(file);
                    String[] pAttrs = new String[5];
                    while (scanner.hasNextLine()) {
                        pAttrs[i] = scanner.nextLine();
                        i++;
                    }
                    rightPane[j] = new ProgramInformationPanel(pAttrs[0], pAttrs[2], pAttrs[1], pAttrs[4], pAttrs[3]);
                    lm.addElement(new ImageIcon(new ImageIcon(pAttrs[3]).getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT)));
                    j++;
                    scanner.close();
                }
            } catch (FileNotFoundException ignored) {}
        } else
            {
                rightPane = new ProgramInformationPanel[1];
                rightPane[0] = new ProgramInformationPanel();
            }
        programsList.setModel(lm);
        programsList.setSelectedIndex(oldSelectedIndex);
    }

}


public class PLauncher {
    static PLauncherFrame frame;
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        frame = new PLauncherFrame();
    }
}