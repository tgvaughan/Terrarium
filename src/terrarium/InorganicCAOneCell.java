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

import java.awt.Point;
import java.util.*;

/**
 * CA used to handle inorganic aspects of simulation.
 * 
 * This is a single-cell CA which allows all cells to update once per frame, in some order,
 * even if they were initially unable to update.
 *
 * @author Glen Robertson
 */
public class InorganicCAOneCell extends InorganicCA {
    
    public InorganicCAOneCell(int width, int height) {
        super(width, height);
    }
    
    private boolean pushState(int i1, int j1, int i2, int j2) {
        if (getCellState(i2, j2).isEmptyFor(getCellState(i1, j1))) {
            swapStates(i1, j1, i2, j2);
            return true;
        } else
            return false;
    }
    
    /**
     * Updates the state of a single cell.
     * 
     * @param i
     * @param j 
     * @param angle the angle at which to try to move the cell
     */
    public Point updateCell(int i, int j, Angle angle) {
        if (angle.isBiggerThan(getCellState(i, j).maxAngle)) {
            return null;
        }
        int nextI = i + angle.dy;
        int dX = angle.dx;
        
        // Randomise left/right movement (doesn't make much difference)
        // if (random.nextBoolean()) {
        // dX = -dX;
        // }
        if (pushState(i,j, nextI,j+dX)) {
            return new Point(nextI,j+dX);
        }
        if (pushState(i,j, nextI,j-dX)) {
            return new Point(nextI, j-dX);
        }
        
        return null;

    }
    
    /** Returns a list of the 8 neighbouring points (or fewer if they are off the edge) */
    public List<Point> neighbours(Point p) {
        List<Point> neighbours = new ArrayList<>();
        for (int i = Math.max(p.x - 1, 0); i < Math.min(p.x + 2, height); i++) {
            for (int j = Math.max(p.y - 1, 0); j < Math.min(p.y + 2, width); j++) {
                if (!(i == p.x && j == p.y)) {
                    neighbours.add(new Point(i,j));
                }
            }
        }
        return neighbours;
    }
    
    public void updateStates() {
        // Each cell can be updated only once per updateStates call
        Set<Point> updatedCells = new HashSet<>();
        for (Angle angle : Angle.values()) {
            if (angle == Angle.NONE) {
                continue;
            }
            List<Point> toCheck = new LinkedList<>();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (!angle.isBiggerThan(getCellState(i, j).maxAngle)) { // speed optimisation
                        toCheck.add(new Point(i,j));
                    }
                }
            }
            while (!toCheck.isEmpty()) {
                Point p = toCheck.remove(0);
                if (!updatedCells.contains(p)) {
                    Point updated = updateCell(p.x, p.y, angle);
                    if (updated != null) {
                        updatedCells.add(updated);
                        toCheck.addAll(neighbours(p));
                    }
                }
            }
        }
    }
}
