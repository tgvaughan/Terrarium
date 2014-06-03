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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    
    /**
     * Construct a new empty terrarium canvas.
     */
    public TerrariumCanvas() {
        timer = new Timer(20, this);
    }
    
    /**
     * Set the terrarium object the canvas will display.
     * 
     * @param terrarium 
     */
    public void setTerrarium(Terrarium terrarium) {
        this.terrarium = terrarium;
        addMouseListener(this);
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (terrarium != null)
            terrarium.render(g, getWidth(), getHeight());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (terrarium != null) {
            terrarium.tick();
            repaint();
        }
    }
    
    /**
     * Start the terrarium simulation.
     */
    public void start() {
        timer.start();
    }
    
    /**
     * Stop/pause the simulation.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Set the frame rate of the simulation.  The default is 50 frames
     * per second.
     * 
     * @param fps frames per second
     */
    public void setFrameRate(double fps) {
        int millis = (int)(1000.0/fps);
        timer.setDelay(millis);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (terrarium != null) {
            switch(e.getButton()) {
                case MouseEvent.BUTTON1:
                    terrarium.addDirt(e.getX()*terrarium.width/getWidth(),
                            e.getY()*terrarium.height/getHeight(), 20);
                    break;
                    
                case MouseEvent.BUTTON3:
                    terrarium.addWater(e.getX()*terrarium.width/getWidth(),
                            e.getY()*terrarium.height/getHeight(), 20);
            }

        }
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
