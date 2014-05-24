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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Canvas on which Terrarium representation is drawn.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TerrariumCanvas extends JPanel implements ActionListener, MouseListener {
    
    private Terrarium terrarium;

    private final Timer timer;
    
    public TerrariumCanvas() {
        timer = new Timer(10, this);
    }
    
    public void setTerrarium(Terrarium terrarium) {
        this.terrarium = terrarium;
        addMouseListener(this);
    }

    @Override
    public void paintAll(Graphics g) {
        super.paintAll(g);
        
        if (terrarium != null)
            g.drawImage(terrarium.render(), 0, 0, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (terrarium != null)
            g.drawImage(terrarium.render(), 0, 0, getWidth(), getHeight(), null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (terrarium != null) {
            terrarium.tick();
            repaint();
        }
    }
    
    public void start() {
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (terrarium != null)
            terrarium.addDirt(e.getX()*terrarium.width/getWidth(),
                    e.getY()*terrarium.height/getHeight(), 20);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
