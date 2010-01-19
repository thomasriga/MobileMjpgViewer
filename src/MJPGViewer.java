/*
MobileMjpgViewer Copyright (C) 2010 Thomas Riga

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

Contact the author at thomasriga@gmail.com (http://www.thomasriga.com)
*/

package com.thomasriga.thr.video.mobile.player;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

public class MJPGViewer extends MIDlet implements CommandListener, Runnable {
    private DataInputStream is = null;
    private Command setCommand;
    private Command playCommand;
    private TextField textField; 
    private Form form;
    private MyGameCanvas gcan;
    private String defaultUrl = "http://www.vsr.it/live_mjpg?concam=0&format=2&rate=5"; 
    //private String defaultUrl = "http://192.168.0.224/live_mjpg?concam=2&format=2&rate=5"; 
    private boolean running = true;
    private Thread baseThread = null;

    public MJPGViewer() {
	gcan = new MyGameCanvas();
	form = new Form("MJPG Viewer");
	textField = new TextField("Video Url", defaultUrl, 100, TextField.ANY); 
	form.append(textField);	
	setCommand = new Command("Set Url", Command.SCREEN, 1);	
	playCommand = new Command("Play", Command.SCREEN, 1);
	form.addCommand(playCommand);
	gcan.addCommand(setCommand);
	form.setCommandListener(this);
	gcan.setCommandListener(this);
   }

    public void commandAction(Command c, Displayable s) {		
	if(c == playCommand) playVideo();	
	else if(c == setCommand) setUrl();
    }

    public void startApp() {
	setUrl();
    }

    private void setUrl() {
	running = false;
	Display.getDisplay(this).setCurrent(form);
    }

    private void playVideo() {
	Display.getDisplay(this).setCurrent(gcan);
	running = true;
	baseThread = new Thread(this);
	baseThread.start();
    }

    public void pauseApp() {
	
    }

    public void destroyApp(boolean unconditional) {
	running = false;
    }

    public void run() {
	 byte [] imgData;
	 String mediaUrl = textField.getString();
	 gcan.setStatusBar(2);
         int actual = 0;
         int bytesread = 0 ;
	 imgData = new byte[30000];
	 Image img = null;
         HttpConnection c = null;
         int rc, size;
	 String boundaryMarker = "";
         try {
	     gcan.setStatusBar(4);
             c = (HttpConnection) Connector.open(mediaUrl);	
             rc = c.getResponseCode();
             if (rc != HttpConnection.HTTP_OK) {
                 throw new IOException("HTTP response code: " + rc);
             }
	     gcan.setStatusBar(6);
             is = c.openDataInputStream();
	     gcan.setStatusBar(8);
	     String cType = c.getType();
	     if(cType.startsWith("multipart")) {
		int bid = cType.indexOf(";boundary=");
		if(bid != -1) {
			boundaryMarker = cType.substring((bid + 10));
			if(!boundaryMarker.startsWith("--")) boundaryMarker = "--" + boundaryMarker;
		}
	        gcan.setStatusBar(10);
		while(running && (Thread.currentThread() == baseThread)) {
			size = readSize(boundaryMarker);
			imgData = new byte[size];
			is.readFully(imgData, 0, size);
			try {
				img = Image.createImage(imgData, 0, size);
				gcan.setImage(img);
			}
			catch(NullPointerException e) {}
                 }			
			
	     } 
         } catch (Exception ex) {
		setUrl();
         } finally {
		try {
             		if (is != null)
                 		is.close();
             		if (c != null)
                 		c.close();
		}
		catch(IOException ioe) {
		}
         }
     }

    public int readSize(String boundaryMarker) throws Exception {
	int size = 0;
	String boundary = "";
	while(!boundary.endsWith(boundaryMarker)) {
		boundary = readLine(is);
	}
	String cLength = "";
	while(!cLength.startsWith("Content-Length")) {
		cLength = readLine(is);
	}
	int ind = cLength.indexOf(":");
	if(ind >= 0) {
		try {
			size = Integer.parseInt(cLength.substring((ind + 2))); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	is.read();
	is.read();
	return size;
    }

    private String readLine(DataInputStream in) throws IOException {
        int ch;
	String str = "";
        while ((ch = in.read()) != -1) {
		if(((char) ch) == '\n') return str;
		else if(((char) ch) == '\r') continue;
		else str +=  ((char) ch);
        }
	return str;
    }
}