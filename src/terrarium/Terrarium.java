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
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Class of objects representing terrarium simulations.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Terrarium {
    
    int width, height;

    InorganicCA inorganicCA;
    
    BufferedImage image;
    
    Color dirtCol = new Color(139, 69, 19, 255);
    Color emptyCol = new Color(100, 100, 255, 0);
    
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
        
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Image render() {
        
        // Render inorganic
        for (int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                int rgbCol;
                switch(inorganicCA.getCellState(i, j)) {
                    case DIRT:
                        rgbCol = dirtCol.getRGB();
                        break;
                    case EMPTY:
                        rgbCol = emptyCol.getRGB();
                        break;
                    default:
                        rgbCol = 0;
                }
                image.setRGB(j, i, rgbCol);
            }
        }
        
        return image;
    }

    /**
     * Step terrarium state forward by one time unit.
     */
    public void tick() {
        inorganicCA.updateStates();
    }
    
    public void addDirt(int x, int y, int radius) {
        
        for (int i=y-radius; i<y+radius; i++) {
            if (i<0 || i>=height)
                continue;
            
            for (int j=x-radius; j<x+radius; j++) {
                if (j<=0 || j>=width)
                    continue;
                
                if ((i-y)*(i-y) + (j-x)*(j-x) < radius*radius)
                    inorganicCA.setCellStateNow(i, j, InorganicCA.CellState.DIRT);
            }
        }
    }
    
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"inorganic\": ").append(inorganicCA.serialize());
        sb.append("}");
        
        return sb.toString();
    }
}
