/*
@author sarahyaw
worker class
 */
package sarahyaw_proj2;
public class Worker extends Thread
{
    static Widget w;
    public String name, position;
    static boolean isHolding, canUpdate=true;
    public static boolean isIdle;
    public static final int QUOTA = 24;
    public static Object stop;
    public static Worker[] workerArray = new Worker[4];

    Worker(String name, String position)
    {
        this.name = name;
        this.isIdle=false;
        if(name.equals("A"))    
            workerArray[0]=this;
        else if(name.equals("B"))    
            workerArray[1]=this;
        else if(name.equals("C"))    
            workerArray[2]=this;
        else if(name.equals("D"))    
            workerArray[3]=this;
        this.position = position;
        isHolding=false;
        this.stop = new Object();
        System.out.println("Worker "+name+" has clocked in as "+this);
    }  

    public static void produce(Worker A)
    //the variable is worker A because for this specific assignment worker A 
    //is the only one that can produce new widgits
    {
        for(int i = 0; i<QUOTA;i++)
        {
            A.isHolding=true;
            w = new Widget("widget"+(i+1));
            w.isHoldBy='A';
            w.isHold=true;
            System.out.println(w.id+" has been created");
            
            
            A.setPriority(8);
            while(!canUpdate)
            {        
                synchronized(stop)
                {
                    try
                    {           
                        A.isIdle=true;
                        System.out.println("WARNING: Worker "+A.name+" is idle!");
                        if(A.isIdle)
                            Factory.a.idleTime(A);
                        stop.wait(); 
                    }
                    catch(Exception e)
                    {
                        System.out.println(w.id+" caused "+e);
                    }
                }
            }
            w.isHoldBy='n';
            w.isHold=false;
            A.isIdle=false;
            ConveyorBelt.putOnBelt(w, Factory.ABConveyor, A);
            Factory.a.updateFrame(w);
            synchronized(A.stop){A.stop.notifyAll();}
            A.setPriority(5);
                        
            ConveyorBelt.napping();          
            ConveyorBelt.napping();

            A.isHolding=false;
        }
    }
    public static void pickUp(ConveyorBelt b, Worker p)
    {   
        ConveyorBelt belt = b;
        boolean finished = false;
        for(int i = 0; i < QUOTA; i++)
        {
            p.setPriority(9);
            while(belt.holding==0 && !finished)
            {
                synchronized(belt.lock)
                {
                    try 
                    {      
                        p.isIdle=true;
                        System.out.println("WARNING: Worker "+p.name+" is idle!");
                        if(p.isIdle)
                            Factory.a.idleTime(p);
                        p.isIdle=false;                        
                        belt.lock.wait();
                    }
                    catch (Exception e) 
                    {
                        System.out.println(belt.route+" threw "+e);
                    }
                }        
            }
            Widget w = ConveyorBelt.takeOffBelt(belt, p);
            w.isHold=true;
            w.isHoldBy=p.name.charAt(0);
            Factory.a.updateFrame(w);
            synchronized(belt.lock){belt.lock.notifyAll();}
            p.isHolding=true;

            
            w.handledBy = w.handledBy+", "+p.name;
            System.out.println("Worker "+p.name+" is working on "+w.id+"<handled by "+w.handledBy+">");
            //after pickup, before put down
            ConveyorBelt.napping();
            
            while(!canUpdate)//just to keep updating 
            {        
                synchronized(stop)
                {
                    try
                    {
                        p.isIdle=true;
                        System.out.println("WARNING: Worker "+p.name+" is idle!");
                        if(p.isIdle){Factory.a.idleTime(p); p.isIdle=false;}
                        p.isIdle=false;
                        stop.wait();
                    }
                    catch(Exception e)
                    {
                        System.out.println(p.name+" threw "+e);
                    }
                }
            }
            synchronized(p.stop){p.stop.notifyAll();}
            p.setPriority(5);

            if(w.id.equals("widget"+QUOTA))
                finished=true;

            Factory.a.updateFrame(w);

            if(p.name.equals("B"))
                putDown(Factory.BCConveyor, p, w); 
            else if(p.name.equals("C"))  
                putDown(Factory.CDConveyor, p, w);
            else
            {
                System.out.println("D looks around and pockets "+w.id);
                w.isHold=true;
                w.isHoldBy=p.name.charAt(0);
                
                p.setPriority(10);
                while(!canUpdate)
                {        
                    synchronized(stop)
                    {
                        try
                        {
                            p.isIdle=true; 
                            System.out.println("WARNING: Worker "+p.name+" is idle!");
                            if(p.isIdle)
                                Factory.a.idleTime(p);
                            p.isIdle=false;
                            stop.wait();
                        }
                        catch(Exception e)
                        {
                            System.out.println(p.name+" threw "+e);
                        }
                    }
                }
                Factory.a.updateFrame(w);
                synchronized(p.stop){p.stop.notifyAll();}
                p.setPriority(5);
            }
        }
    }
    public static void putDown(ConveyorBelt belt, Worker c, Widget w)
    {
        boolean finished = false; 
        c.setPriority(10);
        while(belt.holding==ConveyorBelt.BUFFER_SIZE && !finished)
        {
            synchronized(belt.lock)
            {
                try 
                {
                    c.isIdle=true;
                    System.out.println("WARNING: "+c.name+" is waiting to put " + w.id + " <handled by "+w.handledBy+"> on Full conveyor "+belt.route);
                    if(c.isIdle)
                        Factory.a.idleTime(c);
                    c.isIdle=false;                   
                    belt.lock.wait();
                }
                catch (Exception e) 
                {
                    System.out.println(c.name+" threw "+e);
                }
            }            
        }
        w.isHold=false;
        w.isHoldBy='n';
        ConveyorBelt.putOnBelt(w, belt, c);
        Factory.a.updateFrame(w);
        synchronized(belt.lock){belt.lock.notifyAll();}     

        if(w.id.equals("widget"+QUOTA))
            finished=true;

        while(!canUpdate)//just to keep updating 
        {        
            synchronized(stop)
            {
                try
                {
                    c.isIdle=true; 
                    System.out.println("WARNING: Worker "+c.name+" is idle!");
                    if(c.isIdle){Factory.a.idleTime(c); c.isIdle=false;}
                    c.isIdle=false;
                    stop.wait();                       
                }
                catch(Exception e)
                {
                    System.out.println(c.name+" threw "+e);
                }
            }
        }
        synchronized(c.stop){c.stop.notifyAll();}
        c.setPriority(5);

        c.isHolding=false;
    }   

    @Override
    public void run() 
    {
        if(this.position.equals("producer"))
        {
            System.out.println("Worker "+this.name+" is a "+position);
            produce(this);
        }
        else
        {
            System.out.println("Worker "+this.name+" is a "+position);

            if(this.name.equals("B"))
                pickUp(Factory.ABConveyor, this);
            else if(this.name.equals("C"))
                pickUp(Factory.BCConveyor, this);
            else        
                pickUp(Factory.CDConveyor, this);            
        }
    }
}
