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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Wizard for creating terrariums.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TerrariumWizard extends JDialog {
    
    JFormattedTextField widthField, heightField;
    JRadioButton solidRadioButton, imageRadioButton;
    Color backgroundColour = new Color(200, 200, 255);
    Image backgroundImage;
    boolean useBackgroundImage = false;
    
    Terrarium terrarium;
    
    final Frame appFrame;
    
    public TerrariumWizard(Frame frame) {
        super(frame, true);
        
        appFrame = frame;
        
        setTitle("Terrarium Wizard");
        
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
        sizePanel.setBorder(BorderFactory.createTitledBorder("Terrarium dimension"));
        
        JLabel widthLabel = new JLabel("Width:");
        sizePanel.add(widthLabel);
        
        widthField = new JFormattedTextField(640);
        widthField.setColumns(6);
        widthField.setMaximumSize(widthField.getPreferredSize());
        sizePanel.add(widthField);
        
        sizePanel.add(Box.createHorizontalGlue());
        
        JLabel heightLabel = new JLabel("Height:");
        sizePanel.add(heightLabel);
        
        heightField = new JFormattedTextField(480);
        heightField.setColumns(6);
        heightField.setMaximumSize(heightField.getPreferredSize());
        sizePanel.add(heightField);

        add(sizePanel);
        
        ButtonGroup group = new ButtonGroup();
        solidRadioButton = new JRadioButton("Solid");
        group.add(solidRadioButton);
        imageRadioButton = new JRadioButton("Image");
        group.add(imageRadioButton);
        
        final BackgroundPreviewPanel bgPreview = new BackgroundPreviewPanel(backgroundColour);
        
        solidRadioButton.setSelected(true);
        solidRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundColourChooser chooser = new BackgroundColourChooser(
                        appFrame, backgroundColour);
                chooser.setVisible(true);
                
                backgroundColour = chooser.getColour();
                bgPreview.setSolid(backgroundColour);
                useBackgroundImage = false;
            }
        });
        
        imageRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select background image file...");
                fc.setFileFilter(new FileNameExtensionFilter(
                        "Images", "jpeg", "jpg", "gif", "png"));
                if (fc.showOpenDialog(appFrame) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
            
                    try {
                        backgroundImage = ImageIO.read(file);
                        bgPreview.setImage(backgroundImage);
                        useBackgroundImage = true;
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(appFrame,
                                "Error loading file " + file.getName() + ". Aborting.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    solidRadioButton.setSelected(true);
                }
            }
        });
        
        JPanel bgStylePanel = new JPanel();
        bgStylePanel.setLayout(new BoxLayout(bgStylePanel, BoxLayout.X_AXIS));
        bgStylePanel.setBorder(BorderFactory.createTitledBorder("Background style"));
        
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
        radioButtonPanel.add(solidRadioButton);
        radioButtonPanel.add(imageRadioButton);
        bgStylePanel.add(radioButtonPanel);
        bgStylePanel.add(Box.createHorizontalGlue());
        bgStylePanel.add(bgPreview);
        add(bgStylePanel);

        add(Box.createVerticalGlue());
        
        JPanel buttonPanel = new JPanel();
        add(buttonPanel);
        
        JButton okButton = new JButton("Create");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int width = (int)widthField.getValue();
                int height = (int)heightField.getValue();
                
                terrarium = new Terrarium(width, height);
                if (useBackgroundImage)
                    terrarium.setBackgroundImage(backgroundImage);
                else
                    terrarium.setBackgroundColour(backgroundColour);
                
                setVisible(false);
            }
        });
        buttonPanel.add(okButton);
        
        JButton abortButton = new JButton("Abort");
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(abortButton);
        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(300,250);
        setLocationRelativeTo(frame);
        
    }
  
    /**
     * @return Terrarium object, if created.  Null otherwise.
     */
    public Terrarium getTerrarium() {
        return terrarium;
    }
    
    class BackgroundPreviewPanel extends JPanel {
        
        Image image;
        Color solidCol;

        public BackgroundPreviewPanel(Color initialSolid) {
            image = null;
            solidCol = initialSolid;
        }
        
        public void setImage(Image image) {
            this.image = image;
            solidCol = null;
            repaint();
        }
        
        public void setSolid(Color col) {
            solidCol = col;
            image = null;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            } else {
                if (solidCol != null) {
                    g.setColor(solidCol);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
      
        }
    }
    
    class BackgroundColourChooser extends JDialog {

        JColorChooser chooser;

        public BackgroundColourChooser(Frame frame, Color currentColour) {
            super(frame, true);
            
            setLayout(new BorderLayout());
            
            chooser = new JColorChooser(currentColour);
            add(chooser, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel();
            
            JButton okButton = new JButton("Ok");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ok();
                }
            });
            buttonPanel.add(okButton);
            
            add(buttonPanel, BorderLayout.SOUTH);
            
            setSize(getLayout().preferredLayoutSize(getContentPane()));
            setLocationRelativeTo(frame);
            
            setTitle("Choose terrarium background colour...");
        }
        
        public Color getColour() {
            return chooser.getColor();
        }
        
        public void ok() {
            setVisible(false);
        }
    }
}
