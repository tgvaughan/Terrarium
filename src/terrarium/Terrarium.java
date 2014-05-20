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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class of objects representing terrarium simulations.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Terrarium implements Runnable {
    
    int width, height;

    /**
     * Length of time for a single tick (milliseconds)
     */
    int tickLength;

    InorganicCA inorganicCA;
    
    BufferedImage image;
    
    Color dirtCol = new Color(150, 40, 0);
    Color emptyCol = new Color(100, 100, 255);
    
    /**
     * Create a new terrarium simulation.
     * 
     * @param width
     * @param height 
     */
    public Terrarium(int width, int height) {
        this.width = width;
        this.height = height;
        
        inorganicCA = new InorganicCA(width, height);
        tickLength = 200;
        
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public Image render() {
        
        // Render inorganic
        for (int i=0; i<width; i++) {
            for (int j=0; j<width; j++) {
                int rgbCol;
                switch(inorganicCA.getCellState(i, j)) {
                    case DIRT:
                        rgbCol = dirtCol.getRGB();
                        break;
                    case EMPTY:
                        rgbCol = dirtCol.getRGB();
                        break;
                    default:
                        rgbCol = 0;
                }
                image.setRGB(height-j, i, rgbCol);
            }
        }
        
        return image;
    }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        
        while(true) {
            try {
                Thread.sleep(tickLength);
            } catch (InterruptedException ex) {
                return;
            }

            inorganicCA.updateStates();
        }
    }
}
