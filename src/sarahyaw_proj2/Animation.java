/**
@author sarahyaw
Class to set up and control the GUI
Credits to InnerSloth for the worker sprites; 
    they are crew members from the game Among Us
 */
package sarahyaw_proj2;
import java.util.*;
import java.awt.*;
import javax.swing.*;
public class Animation extends JPanel
{
    static JFrame frame;
    static Graphics gra;
    static Widget arr[] = new Widget[Worker.QUOTA];
    static JLabel A, B, C, D;
    Refresh ref;
    Worker[] workArr = Worker.workerArray;
    
    Animation()
    {
        initializeWindow();
    }
    public void initializeWindow()
    {
        frame = new JFrame("Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        
        Base base = new Base();
        frame.getContentPane().add(base);

        frame.setVisible(true);
    }
    public void idleTime(Worker e)
    {
        Refresh ref = new Refresh(arr, workArr);
        frame.getContentPane().add(ref);
        frame.setVisible(true);
    }
    public void updateFrame(Widget wi)
    {
        Worker.canUpdate=false;

        if(wi.handledBy.equals("A, B, C, D"))
            arr[Integer.parseInt(wi.id.substring(6))-1]=null;
        else        
            arr[Integer.parseInt(wi.id.substring(6))-1] = wi;

        for(int i = 0; i<arr.length; i++)
        {
            if(arr[i]!=null)
            {
                Widget w = arr[i];

                if(!w.isNew)
                    w.slot = (w.slot + 1) % ConveyorBelt.BUFFER_SIZE;
                else
                    w.isNew=false;

                if(w.beltOn.equals("AB"))
                {
                    if(w.slot==0)
                    {
                        w.xcord=200;
                        w.ycord=85;
                    }
                    else if(w.slot==1)
                    {
                        w.xcord=375;
                        w.ycord=85;
                    }
                    else if(w.slot==2)
                    {
                        w.xcord=550;
                        w.ycord=85;
                    }
                }
                else if(w.beltOn.equals("BC"))
                {
                    if(w.slot==0)
                    {
                        w.xcord=690;
                        w.ycord=250;
                    }
                    else if(w.slot==1)
                    {
                        w.xcord=690;
                        w.ycord=350;
                    }
                    else if(w.slot==2)
                    {
                        w.xcord=690;
                        w.ycord=500;
                    }
                }
                else if (w.beltOn.equals("CD"))
                {
                    if(w.slot==0)
                    {
                        w.xcord=550;
                        w.ycord=650;
                    }
                    else if(w.slot==1)
                    {
                        w.xcord=375;
                        w.ycord=650;
                    }
                    else if(w.slot==2)
                    {
                        w.xcord=200;
                        w.ycord=650;
                    }
                }
                else if (w.isHold)
                {
                    System.out.println(w.id+" IS HOLD BY "+w.isHoldBy);
                    if( w.isHoldBy==('A'))
                    {
                        w.xcord=400;
                        w.ycord=400;
                    }
                    else if(w.isHoldBy==('B'))
                    {
                        w.xcord=400;
                        w.ycord=20;
                    }
                    else if(w.isHoldBy==('C'))
                    {
                        w.xcord=400;
                        w.ycord=400;
                    }
                    else if(w.isHoldBy==('D'))
                    {
                        w.xcord=400;
                        w.ycord=400;
                    }
                }
                else
                {
                    w.xcord=400;
                    w.ycord=400;
                }
            }
        }
        ref = new Refresh(arr, workArr);
        frame.getContentPane().add(ref);

        frame.setVisible(true);
        Worker.canUpdate=true;
    }
    private static class Refresh extends JPanel
    {
        Widget array[];
        Worker employ[];
        Refresh(Widget[] arr, Worker[] e)
        {
            array = arr;
            employ=e;   
                System.out.println("UPDATE---------------"); 
            for(int i = 0; i<arr.length; i++)
            {
                if(arr[i]!=null)
                {
                System.out.println(arr[i].id+" SLOT "+arr[i].slot+" BELT "+arr[i].beltOn);
                }
            }
                System.out.println("---------------------");

            try{Thread.sleep(500);}
            catch(Exception error){System.out.println(error);}
        }
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            gra = g;
            System.out.println("****************");
            if(employ[0].isIdle)
            {   System.out.println(employ[0].name+" idle "+employ[0].isIdle);
                gra.setColor(Color.RED.darker());
                gra.fillOval(10, 10, 140, 140);
            }
            else 
            {   System.out.println(employ[0].name+" idle "+employ[0].isIdle);
                gra.setColor(Color.RED);
                gra.fillOval(10, 10, 140, 179);
            }

            if (employ[1].isIdle)
            {   System.out.println(employ[1].name+" idle "+employ[1].isIdle);
                gra.setColor(Color.ORANGE.darker());
                gra.fillOval(650, 10, 140, 140);
            }
            else
            {   System.out.println(employ[1].name+" idle "+employ[1].isIdle);
                gra.setColor(Color.ORANGE);
                gra.fillOval(650, 10, 140, 179);
            }

            if (employ[2].isIdle)
            {   System.out.println(employ[2].name+" idle "+employ[2].isIdle);
                gra.setColor(Color.MAGENTA.darker());
                gra.fillOval(650, 590, 140, 140);
            }
            else
            {   System.out.println(employ[2].name+" idle "+employ[2].isIdle);
                gra.setColor(Color.MAGENTA);
                gra.fillOval(650, 590, 140, 179);
            }
            if (employ[3].isIdle)
            {   System.out.println(employ[3].name+" idle "+employ[3].isIdle);
                gra.setColor(Color.BLUE.darker());
                gra.fillOval(10, 590, 140, 140);
            }
            else
            {   System.out.println(employ[3].name+" idle "+employ[3].isIdle);
                gra.setColor(Color.BLUE);
                gra.fillOval(10, 590, 140, 179);
            }
            System.out.println("****************");

            gra.setColor(Color.GRAY);  
            gra.fillRect(175, 75, 450, 75); 
            gra.fillRect(680, 200, 75, 375); 
            gra.fillRect(175, 640, 450, 75);
 
            for(int i = 0; i<array.length; i++)
            {
                if(arr[i]!=null)
                {
                    gra.setColor(Color.decode(array[i].color));
                    gra.fillOval(array[i].xcord, array[i].ycord, 50, 50);
                } 
            }
        }
    }
    private static class Base extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            gra = g;

            gra.setColor(Color.RED);
            gra.fillOval(10, 10, 140, 179);
            gra.setColor(Color.ORANGE);
            gra.fillOval(650, 10, 140, 179);
            gra.setColor(Color.MAGENTA);
            gra.fillOval(650, 590, 140, 179);
            gra.setColor(Color.BLUE);
            gra.fillOval(10, 590, 140, 179);

            gra.setColor(Color.GRAY);  
            gra.fillRect(175, 75, 450, 75); 
            gra.fillRect(680, 200, 75, 375); 
            gra.fillRect(175, 640, 450, 75);
        }
    }
}
