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

public class JCTermApplet extends JApplet{
  JCTermSwing term;
  JInternalFrame frame;

  public JCTermApplet(){
    term=new JCTermSwing();
  }

  public void init(){
    setVisible(true);
    JDesktopPane desktop=new JDesktopPane();

    if(Toolkit.getDefaultToolkit()
        .getDesktopProperty("win.mdi.backgroundColor")!=null)
      desktop.setBackground((Color)Toolkit.getDefaultToolkit()
          .getDesktopProperty("win.mdi.backgroundColor"));

    Container content=getContentPane();
    content.add(desktop, BorderLayout.CENTER);
    desktop.setVisible(true);

    frame=new JInternalFrame();
    frame.setIconifiable(true);
    setFocusable(true);

    frame.getContentPane().add("Center", term);

    JCTermSwingFrame jctermsf=new JCTermSwingFrame();
    jctermsf.setTerm(term);
    frame.setJMenuBar(jctermsf.getJMenuBar());

    frame.pack();

    desktop.add(frame);

    term.setVisible(true);
    frame.setVisible(true);

    frame.setResizable(true);
    {
      int foo=term.getTermWidth();
      int bar=term.getTermHeight();
      foo+=(frame.getWidth()-frame.getContentPane().getWidth());
      bar+=(frame.getHeight()-frame.getContentPane().getHeight());
      frame.setSize(foo, bar);
    }
    frame.setResizable(false);

    frame.setLocation((getWidth()-frame.getWidth())/2, (getHeight()-frame
        .getHeight())/2);

    ComponentListener l=new ComponentListener(){
      public void componentHidden(ComponentEvent e){
      }

      public void componentMoved(ComponentEvent e){
      }

      public void componentResized(ComponentEvent e){
        Component c=e.getComponent();
        int cw=c.getWidth();
        int ch=c.getHeight();
        int cwm=(c.getWidth()-((JInternalFrame)c).getContentPane().getWidth());
        int chm=(c.getHeight()-((JInternalFrame)c).getContentPane().getHeight());
        cw-=cwm;
        ch-=chm;
        JCTermApplet.this.term.setSize(cw, ch);
      }

      public void componentShown(ComponentEvent e){
      }
    };
    frame.addComponentListener(l);
    addKeyListener(term);
    frame.setResizable(true);
    frame.setMaximizable(true);

    //jctermsf.openSession();
  }

  public void start(){
    requestFocus();
    //    frame.requestFocus();
    //term.kick();
  }
}
