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

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

/**
 * Wizard for creating terrariums.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TerrariumWizard extends JDialog {
    
    JFormattedTextField widthField, heightField;
    
    Terrarium terrarium;

    public TerrariumWizard(Frame frame) {
        super(frame, true);

        setTitle("Terrarium Wizard");
        
        JLabel directionsLabel = new JLabel("<html>Create new terrarium with the<br>"
                + "following properties:");
        add(directionsLabel);
        
        JLabel widthLabel = new JLabel("Width:");
        add(widthLabel);
        
        widthField = new JFormattedTextField(640);
        widthField.setColumns(6);
        add(widthField);
        
        JLabel heightLabel = new JLabel("Height:");
        add(heightLabel);
        
        heightField = new JFormattedTextField(480);
        heightField.setColumns(6);
        add(heightField);

        JButton okButton = new JButton("Create");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int width = (int)widthField.getValue();
                int height = (int)heightField.getValue();
                
                terrarium = new Terrarium(width, height);
                
                setVisible(false);
            }
        });
        add(okButton);
        
        JButton abortButton = new JButton("Abort");
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(abortButton);
        
        Container cp = getContentPane();
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.NORTH, directionsLabel, 10, SpringLayout.NORTH, cp);
        layout.putConstraint(SpringLayout.WEST, directionsLabel, 15, SpringLayout.WEST, cp);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, widthLabel, 0, SpringLayout.VERTICAL_CENTER, widthField);
        layout.putConstraint(SpringLayout.WEST, widthLabel, 15, SpringLayout.WEST, cp);
        
        layout.putConstraint(SpringLayout.NORTH, widthField, 10, SpringLayout.SOUTH, directionsLabel);
        layout.putConstraint(SpringLayout.WEST, widthField, 5, SpringLayout.EAST, widthLabel);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, heightLabel, 0, SpringLayout.VERTICAL_CENTER, heightField);
        layout.putConstraint(SpringLayout.EAST, heightLabel, 0, SpringLayout.EAST, widthLabel);
        
        layout.putConstraint(SpringLayout.NORTH, heightField, 5, SpringLayout.SOUTH, widthField);
        layout.putConstraint(SpringLayout.WEST, heightField, 5, SpringLayout.EAST, widthLabel);
        
        layout.putConstraint(SpringLayout.EAST, okButton, -5, SpringLayout.WEST, abortButton);
        layout.putConstraint(SpringLayout.SOUTH, okButton, -5 , SpringLayout.SOUTH, cp);
        
        layout.putConstraint(SpringLayout.EAST, abortButton, -5, SpringLayout.EAST, cp);
        layout.putConstraint(SpringLayout.SOUTH, abortButton, -5, SpringLayout.SOUTH, cp);
        
        setLayout(layout);

        setSize(300, 200);
        setLocationRelativeTo(frame);
        
    }
    
    /**
     * @return Terrarium object, if created.  Null otherwise.
     */
    public Terrarium getTerrarium() {
        return terrarium;
    }
}
