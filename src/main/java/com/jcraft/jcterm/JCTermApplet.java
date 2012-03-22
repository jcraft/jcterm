/* -*-mode:java; c-basic-offset:2; -*- */
/* JCTerm
 * Copyright (C) 2002,2007 ymnk, JCraft,Inc.
 *  
 * Written by: ymnk<ymnk@jcaft.com>
 *   
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
   
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.jcraft.jcterm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JCTermApplet extends JApplet {
  JDesktopPane desktop=new JDesktopPane();

  private int font_size = 14;

  public void init(){
    JCTermSwingFrame.resetCounter();
    String s;

    s = getParameter("jcterm.destinations");
    if(s!=null){
      JCTermSwingFrame.setDestinations(s);
    }

    s = getParameter("jcterm.font_size");
    if(s!=null){
      try{
        font_size = Integer.parseInt(s);
      }
      catch(NumberFormatException e){
        System.err.println("jcterm.font_size: "+s);
      }
    }

    setVisible(true);

    if(Toolkit.getDefaultToolkit()
        .getDesktopProperty("win.mdi.backgroundColor")!=null)
      desktop.setBackground((Color)Toolkit.getDefaultToolkit()
          .getDesktopProperty("win.mdi.backgroundColor"));

    Container content=getContentPane();
    content.add(desktop, BorderLayout.CENTER);
    desktop.setVisible(true);

    JButton addButton = new JButton("New window");
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openFrame(Frame.SHELL);
      }
    });
    content.add(addButton, BorderLayout.NORTH);
    addButton.setLocation(0, 0);
    addButton.setVisible(true);

    setFocusable(true);
  }

  public void start(){
    requestFocus();
    setFocusable(true);
    openFrame(Frame.SHELL);
  }

  public void openFrame(int mode){
    final JCTermSwing term = new JCTermSwing();
    JCTermSwingFrame jctermsf=new JCTermSwingFrame();
    final MyFrame frame = new MyFrame(jctermsf);

    frame.setTitle("JCTerm");
    frame.setIconifiable(true);

    frame.getContentPane().add("Center", term);

    jctermsf.setCloseOnExit(true);
    jctermsf.setTerm(term);
    jctermsf.setFrame(frame);

    frame.setJMenuBar(jctermsf.getJMenuBar());

    frame.pack();

    desktop.add(frame);

    ComponentAdapter l = new ComponentAdapter(){
      public void componentResized(ComponentEvent e){
        Component c = e.getComponent();
        Container cp = ((JInternalFrame)c).getContentPane();
        int cw=c.getWidth();
        int ch=c.getHeight();
        int cwm=c.getWidth()-cp.getWidth();
        int chm=c.getHeight()-cp.getHeight();
        cw-=cwm;
        ch-=chm;
        term.setSize(cw, ch);
      }
    };
    frame.addComponentListener(l);
    addKeyListener(term);

    term.setVisible(true);
    frame.setVisible(true);

    frame.setResizable(true);
    frame.setMaximizable(true);

    jctermsf.setFontSize(font_size);

    frame.setLocation((getWidth()-frame.getWidth())/2,
                      (getHeight()-frame.getHeight())/2);

    jctermsf.openSession();
  }

  class MyFrame extends JInternalFrame implements Frame {
    JCTermSwingFrame jctermsf;
    InternalFrameAdapter l = new InternalFrameAdapter(){
      public void internalFrameClosing(InternalFrameEvent e){
        jctermsf.dispose_connection();
      }
    };
    MyFrame(JCTermSwingFrame jctermsf){
      this.jctermsf = jctermsf;
      setClosable(true);
      addInternalFrameListener(l);
    }
    public void openFrame(int mode){
      JCTermApplet.this.openFrame(mode);
    }
  }
}
