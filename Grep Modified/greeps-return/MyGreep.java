import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class 'MyGreep'. No other classes may be modified or created. 
 *
 * Rule 2 
 * You cannot extend the Greeps' memory. That is: you are not allowed to add 
 * fields (other than final fields) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method defined in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-defined classes, such as MyGreep). Greeps have no magic 
 * powers - they cannot create things out of nothing. 
 * 
 * Rule 6 
 * You are not allowed to call any methods (other than those listed in Rule 4)
 * of any other class in this scenario (including Actor and World). 
 *  
 * If you change the name of this class you should also change it in
 * Ship.createGreep().
 * 
 * Please do not publish your solution anywhere. We might want to run this
 * competition again, or it might be used by teachers to run in a class, and
 * that would be ruined if solutions were available.
 * 
 * 
 * @author (your name here)
 * @version 0.1
 */
public class MyGreep extends Greep
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    
    /**
     * Default constructor. Do not remove.
     */
    public MyGreep(Ship ship)
    {
        super(ship);
    }
    
    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        
        if(getMemory(0) > 0){
            switch(getMemory(0)){
                case 1:
                    if(getTomatoes() != null){
                        if(getFriend() != null){
                            loadTomato();
                        }
                        else{
                            block();
                        }
                    }
                    else{
                        setMemory(0,0);//collector
                    }
                    break;
            }
        }
        else {
           if (carryingTomato()) {
               if(atShip()) {
                   dropTomato();
                   getLocation();
               }
               else {
                   returnHome();
               }
            }
            else {
                if(moveWasBlocked() || atWater()){
                    bounce();
                }
                else {
                //Check if we can see tomatoes
                    if(getTomatoes() != null){
                    //how close are we
                        if(onTomatos(getTomatoes())){
                        //Check if there is a grep already blocking this
                            if(getFriend() != null){
                                if(getFriend().getMemory(0) == 1){
                                    //then there is a blocker and it will also load up others
                                    //This will work like a wait period allowing the blocker to
                                    //give the collector tomatos
                                }
                                else{
                                    setMemory(0,1);//Im BLOCKING
                                    block();
                                }
                            }
                            else {
                                setMemory(0,1);//Im BLOCKING
                                block();
                            }
                        }
                        else{
                            turnTowards(getTomatoes().getX(),getTomatoes().getY());
                            move();
                        }
                    }
                    else{
                        if(numberOfOpponents(false) > numberOfFriends(false) + 1){
                            kablam();
                        }
                        if(getMemory(0) == 2){
                           //Know where to look
                           if(moveWasBlocked() || atWater()){
                                bounce();
                            }
                            else{
                                turnTowards(getMemory(2),getMemory(3));
                                move();
                            }
                        }
                        else{
                            //no idea where its going
                            randomWalk();
                        }
                    }
                }
           }
        }
    }
    
    public void checkPosition(){
    
    public void getLocation(){
        int databank[] = getShipData();
        if(databank[0] > 0){
            int counter = databank[0];
            int pointer = 0;
            
            //check wihich place is the closes
            int x = 999;
            int y = 999;
            for(int i = 1; i < counter; i++){
                if(distance(x,y) > distance(databank[(i * 2) -1],databank[(i * 2)])){
                   x = databank[(i * 2) -1];
                   y = databank[(i * 2)]; 
                }
            }
            //set the fastest poition
            setMemory(0,2);
            setMemory(2,x);
            setMemory(3,y);
        }
    }
    
    /** 
     * Move forward, with a slight chance of turning randomly
     */
    public void randomWalk()
    {
        // there's a 3% chance that we randomly turn a little off course
        if (randomChance(3)) {
            turn((Greenfoot.getRandomNumber(3) - 1) * 100);
        }
        
        move();
    }
    
    public void returnHome(){
        //First check im not stuck at water
        if(moveWasBlocked() || atWater()){
            bounce();
        }
        else{
            turnHome();
            move();
        }
    }
    
    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null) {
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
        }
    }
    
    public int distance(int x, int y){
        int distX = getX() - x;
        int distY = getY() - y;
        return (int) Math.sqrt(distX * distX + distY * distY);
    }
    
    public boolean onTomatos(TomatoPile pile){
        int distX = getX() - pile.getX();
        int distY = getY() - pile.getY();
        if((int) Math.sqrt(distX * distX + distY * distY) < 6){
            return true;
        }
        return false;
    }
    
    /**
     * This method specifies the name of the greeps (for display on the result board).
     * Try to keep the name short so that it displays nicely on the result board.
     */
    public String getName()
    {
        return "HMS Apple";  // write your name here!
    }
    
    
    public void bounce()
    {
        turn(45);
        move();
    }
}
