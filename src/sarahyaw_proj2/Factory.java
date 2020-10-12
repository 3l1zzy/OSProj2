/*
@author sarahYaw
deiver class
*/
package sarahyaw_proj2;

public class Factory extends Thread
{
    static ConveyorBelt ABConveyor, BCConveyor, CDConveyor;
    static Animation a; 
    static Thread workerA, workerB, workerC, workerD;

    public static void main(String[] args) 
    {
        a = new Animation();
        startProduction();
    }
    public static void startProduction()
    {
        System.out.println("The factory is open");

        ABConveyor = new ConveyorBelt("AB");
        BCConveyor = new ConveyorBelt("BC");
        CDConveyor = new ConveyorBelt("CD");

        workerA = new Thread(new Worker("A", "producer"));
        workerB = new Thread(new Worker("B", "consumer/producer"));
        workerC = new Thread(new Worker("C", "consumer/producer"));
        workerD = new Thread(new Worker("D", "consumer"));
        
        workerA.setPriority(6);
        workerB.setPriority(5);
        workerC.setPriority(4);
        workerD.setPriority(3);
            
        workerA.start();
        workerB.start();
        workerC.start();
        workerD.start();
        
        try
        {
            workerA.join();
            workerB.join();
            workerC.join();
            workerD.join();
            System.out.println("Shift is over, all done!");
            System.exit(0);
        }
        catch (Exception e)
        {
            System.out.println("Could not join: "+e.getCause());
        }
    }

}
