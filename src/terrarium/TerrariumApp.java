/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package terrarium;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main application class for the terrarium simulator.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TerrariumApp extends JFrame {

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem fileNewMenuItem, fileSaveMenuItem, fileExitMenuItem;
    
    TerrariumCanvas canvas;
    Terrarium terrarium;

    public TerrariumApp() throws HeadlessException {
        createMenuBar();
        
        canvas = new TerrariumCanvas();
        getContentPane().add(canvas);
        
        setSize(640, 480);
    }

    final void createMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        fileNewMenuItem = new JMenuItem("New terrarium...", KeyEvent.VK_N);
        fileNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileNewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileNewAction();
            }
        });
        fileMenu.add(fileNewMenuItem);
        
        fileSaveMenuItem = new JMenuItem("Save terrarium", KeyEvent.VK_S);
        fileSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileSaveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSaveAction();
            }
        });
        fileSaveMenuItem.setEnabled(false);
        fileMenu.add(fileSaveMenuItem);

        fileMenu.addSeparator();
        
        fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        fileExitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        fileExitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileExitAction();
            }
        });
        fileMenu.add(fileExitMenuItem);

        setJMenuBar(menuBar);
    }

    /**
     * Bring up terrarium creation wizard.
     */
    void fileNewAction() {

        if (terrarium != null) {
            int res = JOptionPane.showConfirmDialog(this,
                    "This will discard existing terrarium.  Are you sure?",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            
            if (res != JOptionPane.YES_OPTION)
                return;
        }
        
        TerrariumWizard wizard = new TerrariumWizard(this);
        wizard.setVisible(true);
        if (wizard.getTerrarium() != null) {
            terrarium = wizard.getTerrarium();
            canvas.setTerrarium(terrarium);
            canvas.start();
            fileSaveMenuItem.setEnabled(true);
        }
    }
    
    /**
     * Handle saving displayed terrarium state to disk.
     */
    void fileSaveAction() {
        JFileChooser fc = new JFileChooser();
  
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                
                return f.getName().endsWith(".terrarium");
            }

            @Override
            public String getDescription() {
                return "Terrarium files (*.terrarium)";
            }
        });
        
        fc.setSelectedFile(new File("saved.terrarium"));
        
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (file.exists()) {
                int res = JOptionPane.showConfirmDialog(this,
                        "File " + file.getName() + " already exists. Overwrite?");
                if (res != JOptionPane.YES_OPTION)
                    return;
            }
            
            try (PrintStream pstream = new PrintStream(file)) {
                    pstream.println(terrarium.serialize());

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error writing to file " + file.getName() + ". Aborting.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles exiting the program.
     */
    void fileExitAction() {
        setVisible(false);
        System.exit(0);
    }

    /**
     * Main method for app.
     *
     * @param args
     */
    public static void main(String[] args) {

        // Enable anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings","on");
        
        // Look and feel:
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException e) {
            // Need to do anything here?
        }


        TerrariumApp app = new TerrariumApp();
        app.setVisible(true);
    }
}
