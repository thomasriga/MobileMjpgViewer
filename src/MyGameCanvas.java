/*
MobileMjpgViewer Copyright (C) 2010 Thomas Riga

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

Contact the author at thomasriga@gmail.com (http://www.thomasriga.com)
*/

package com.thomasriga.thr.video.mobile.player;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class MyGameCanvas extends GameCanvas {

    public MyGameCanvas() {
	super(true);
    }

    public void setImage(Image img) {
	Graphics g = getGraphics();
	g.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT);
	flushGraphics();
    }

    public void setStatusBar(int level) {
	Graphics g = getGraphics();
	int part = getWidth() / 10;
	g.setColor(255, 255, 255);
	g.fillRect(0, 0, getWidth(), getHeight());	
	g.setColor(255, 0, 0);
	g.fillRect(0, 0, (part * level), 10);
	flushGraphics();
    }	
}