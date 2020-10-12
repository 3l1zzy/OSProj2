/*
@author sarahyaw
widget class
 */

package sarahyaw_proj2;

public class Widget 
{
    public String id, color, handledBy, beltOn;
    public char isHoldBy;
    public int xcord, ycord, slot;
    public boolean isNew, isHold;
    Widget(String id)
    {
        this.slot=0;
        this.id=id;
        this.xcord=200;
        this.ycord=85; 
        this.isHold=true;
        this.isNew = true;
        this.handledBy="A";
        this.beltOn="N/A";
        int number = Integer.parseInt(this.id.substring(6));
        switch(number%6)
        {
            case 0:
                this.color="#FFC2B6";  
                break;
            case 1:
                this.color="#FFE2B6";  
                break;
            case 2:
                this.color="#FDFFB6";  
                break;
            case 3:
                this.color="#BAFFB6";  
                break;
            case 4:
                this.color="#B6E6FF";  
                break;
            case 5:
                this.color="#E3B6FF";  
                break;
        }
    }   
}