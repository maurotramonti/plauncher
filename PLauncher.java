package plauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;
import java.net.*;


class SysConst {
    static final String system = System.getProperty("os.name");
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
    private JFrame frame;
    private int lang = 1;
    public JTabbedPane tPane = new JTabbedPane();
    public ProgramTab[] pTabs;
    private JMenu fileMenu, prefMenu, aboutMenu;
    private JMenuBar menuBar = new JMenuBar();


    PLauncherFrame() {
        super("P-Launcher");
        frame = this;
        frame.setSize(800, 600);
        frame.setResizable(false);
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
        } catch (IOException e) {}

        try {
            BufferedReader br = new BufferedReader(new FileReader(SysConst.getPrePath() + "conf" + File.separator + "lastcheck.txt"));
            String lc = br.readLine();
            final long millis = Long.parseLong(lc);
            br.close();
            if ((System.currentTimeMillis() - millis) >= 172800000) { // sono passati almeno 2 giorni
                checkUpdates(true);
            }
        } catch (IOException ex) {}


        loadPrograms();
        
        fileMenu = new JMenu("File");
        prefMenu = new JMenu(LanguageManager.getTranslationsFromFile("Preferences", lang));
        aboutMenu = new JMenu(LanguageManager.getTranslationsFromFile("Help", lang));

        JMenuItem createNewButton = new JMenuItem(LanguageManager.getTranslationsFromFile("NewProgram", lang));
        createNewButton.addActionListener(new FileMenuHandler());
        createNewButton.setActionCommand("Create new");
        createNewButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(createNewButton);

        JMenuItem modifyButton = new JMenuItem(LanguageManager.getTranslationsFromFile("ModifyCurrent", lang));
        modifyButton.addActionListener(new FileMenuHandler());
        modifyButton.setActionCommand("Modify");
        modifyButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));

        fileMenu.add(modifyButton);

        JMenuItem deleteButton = new JMenuItem(LanguageManager.getTranslationsFromFile("DeleteProgram", lang));
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

        frame.add(tPane);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getLang() {
        return lang;
    }

    public JTabbedPane getTabbedPane() {
        return tPane;
    }

    public ProgramTab[] getProgramTabs() {
        return pTabs;
    }

    protected void loadLanguage() {
        LanguageManager lm = new LanguageManager();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
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
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            lm.actionPerformed(null);
            loadLanguage();
        }
    }

    protected void loadChangelog() {
        JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("Changelog", lang), "Changelog", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void checkUpdates(boolean showOnlyIfPositive) {
        final int internalVersion = 101;
        final String internalVersionString = new String("101");
        try {
            URL url = new URL("https://raw.githubusercontent.com/maurotramonti/plauncher/main/conf/latest.txt");
            InputStream is = url.openStream();
            // Stream to the destionation file
            FileOutputStream fos = new FileOutputStream(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
            // Read bytes from URL to the local file
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
      	        fos.write(buffer, 0, bytesRead);
            }

            // Close destination stream
            fos.close();
            // Close URL stream
            is.close();
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            l = scanner.nextLine();
            final int internalVersionRead = Integer.parseInt(l);
            if (internalVersionRead > internalVersion) {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CUTxtY", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (!showOnlyIfPositive) JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CUTxtN", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
            }
            scanner.close();            
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(internalVersionString);
            bw.close();
            bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "lastcheck.txt"));
            bw.write(Long.toString(System.currentTimeMillis()));
            bw.close();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CheckUpdatesErr", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    protected void loadPrograms() {
        File pDir = new File(SysConst.getPrePath() + "programs");
        String[] pList = pDir.list();
        if (pList != null) {
        
            pTabs = new ProgramTab[pList.length];
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
                    pTabs[j] = new ProgramTab(pAttrs[0], pAttrs[1], pAttrs[2], pAttrs[3], pAttrs[4], lang);
                    tPane.addTab(pTabs[j].getProgramName(), new ImageIcon(new ImageIcon(pAttrs[3]).getImage().getScaledInstance(24, 24,  Image.SCALE_DEFAULT)), pTabs[j]);
                    j++;
                    scanner.close();  // libera l'accesso al file
                }
            } catch (FileNotFoundException e) {}
        } else pTabs = new ProgramTab[16];
    }

}


public class PLauncher {
    static PLauncherFrame frame;
    public static void main(String[] args) {
        frame = new PLauncherFrame();
    }
}