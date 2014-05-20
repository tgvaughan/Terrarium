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
import javax.swing.JPanel;

/**
 * Canvas on which Terrarium representation is drawn.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TerrariumCanvas extends JPanel {
    
    Terrarium terrarium;

    public TerrariumCanvas(Terrarium terrarium) {
        this.terrarium = terrarium;
    }

    @Override
    public void paintAll(Graphics g) {
        g.drawImage(terrarium.render(), 0, 0, null);
    }
    
}
