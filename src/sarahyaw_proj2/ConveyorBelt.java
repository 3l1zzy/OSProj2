/*
 @author sarahyaw - code based on 
    BoundedBuffer.java
    This program implements the bounded buffer using Java synchronization.
    @author Greg Gagne, Peter Galvin, Avi Silberschatz
    @version 1.0 - July 15, 1999
    Copyright 2000 by Greg Gagne, Peter Galvin, Avi Silberschatz
    Applied Operating Systems Concepts - John Wiley and Sons, Inc.
conveyor belt class
conveyor color: GRAY
 */
package sarahyaw_proj2;
import java.util.*;
import java.nio.*;
public class ConveyorBelt
{
    public int holding, incoming, outgoing;
    public String route;
    public Widget[] arr;
    public static Object lock;
    public static final int NAP_TIME = 6000, BUFFER_SIZE = 3;
    ConveyorBelt(String route)
    {
        this.route = route;
        this.lock = new Object();
        this.holding=0;
        this.incoming=0;
        this.outgoing=0;
        arr=new Widget[BUFFER_SIZE];
        System.out.println(route+" belt is initialized");
    }
 
    public static void napping() 
    {
        int sleepTime = (int) (NAP_TIME * Math.random());
        try 
        { 
            Thread.sleep(sleepTime); 
        }
        catch(InterruptedException e) 
        { 
            System.out.println("Worker did not nap!");
        }
    }

    public static synchronized void putOnBelt(Widget w, ConveyorBelt ibelt, Worker p) 
    {
        while (ibelt.holding == BUFFER_SIZE) 
        {
            synchronized(ibelt.lock)
            {
                try 
                {
                    System.out.println("WARNING: "+p.name+" is waiting to put " + w.id + " <handled by "+w.handledBy+"> on Full conveyor "+ibelt.route);
                    ibelt.lock.wait();
                }
                catch (Exception e) 
                {
                    System.out.println(ibelt.route+" threw "+e);
                }
            }
        }

        ibelt.holding=ibelt.holding+1;
        w.beltOn = ibelt.route;
        w.slot=0;
        w.isNew=true;
        w.isHold=false;
        ibelt.arr[ibelt.incoming] = w;
        ibelt.incoming = (ibelt.incoming + 1) % BUFFER_SIZE;
        System.out.println("Worker "+p.name+" is placing "+w.id+" <handled by "+w.handledBy+"> on the belt "+ibelt.route);
        
        synchronized(ibelt.lock){ ibelt.lock.notifyAll(); }
    }
   
    public static synchronized Widget takeOffBelt(ConveyorBelt obelt, Worker c) 
    {
        while(obelt.holding==0)
        {
            synchronized(obelt.lock)
            {
                try 
                {
                    System.out.println("WARNING: Worker "+c.name+" is idle!");
                    obelt.lock.wait();
                }
                catch (Exception e)
                {
                    System.out.println(obelt.route+" threw "+e);
                }
            }
        }

        obelt.holding=obelt.holding-1;
        Widget wi = obelt.arr[obelt.outgoing];
        wi.isHold = true;
        obelt.outgoing = (obelt.outgoing + 1) % BUFFER_SIZE;
        System.out.println("Worker "+c.name+" is retrieving "+wi.id+" <handled by "+wi.handledBy+"> from the belt "+obelt.route);

        synchronized(obelt.lock) { obelt.lock.notifyAll(); }
      
        return wi;
    }
    
}