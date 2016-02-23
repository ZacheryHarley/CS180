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

        //run every loop, some minor bugs occured
        if(atShip() && getMemory(0) == 3){
            speakToCaptain();//delete from ship
            setMemory(0,0);//reset
            speakToCaptain();//update
        }
        if(getFriend() != null && getMemory(0) == 2){
            //tell freind cool place
            if(getFriend().getMemory(0) == 0){
                getFriend().setMemory(0, 2);
                getFriend().setMemory(2, getMemory(2));
                getFriend().setMemory(3, getMemory(3));
            }
        }
        
        
        if(getMemory(0) > 0 && getMemory(0) != 2){//set and not moveing between points
            switch(getMemory(0)){
                case 1:
                if(getTomatoes() != null){
                    if(numberOfFriends(false) > 0){

                        //get current tomatoes position
                        setMemory(2, getTomatoes().getX());
                        setMemory(3, getTomatoes().getY());
                        loadTomato();
                    }
                    else{
                        block();
                    }

                }
                else{
                    setMemory(0,3);//delete the record from the main
                }
                break;
                case 3:
                returnHome();
                if(atShip()){
                    speakToCaptain();
                }
            }
        }
        else {
            if (carryingTomato()) {
                if(atShip()) {

                    dropTomato();
                    speakToCaptain();
                }
                else {
                    //make sure data was set
                    if(getTomatoes() != null){
                        setMemory(0,2);
                        setMemory(2, getTomatoes().getX());
                        setMemory(3, getTomatoes().getY());
                    }

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
                                    //Set the memory
                                    setMemory(2, getTomatoes().getX());
                                    setMemory(3, getTomatoes().getY());
                                    setMemory(0,2);//found
                                    //////////////////////////////////////////System.out.println("Found food, setting to 2");
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
                                //check if there and if it has been emptied
                                if(distance(getMemory(2),getMemory(3)) < 4 && getTomatoes() == null){
                                    //the toms have gone :(
                                    setMemory(0,3);
                                }
                                else{
                                    turnTowards(getMemory(2),getMemory(3));
                                    move();
                                }
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

    public void vomit(){
        System.out.println("---------------------------");
        System.out.println("mem 0: " + getMemory(0));
        System.out.println("mem 2: " + getMemory(2));
        System.out.println("mem 3: " + getMemory(3));
    }

    public void speakToCaptain(){
        int myMind = getMemory(0);

        switch(myMind){
            case 0:
            if(getLocation()){setMemory(0,2);}
            break;
            case 1:
            setMemory(0,0);
            break;
            case 2:
            if(checkLocation() < 0){
                addLocation();
            }
            break;
            case 3:
            setMemory(0,0);
            if(checkLocation() > 0){
                deleteRecord();
            }
            if(getLocation()){setMemory(0,2);}
            break;
        }
    }

    public void deleteRecord(){
        int databank[] = getShipData();

        int i = checkLocation();
        if(i != -1){
            //the recod exsits i is its pointer
            databank[(i * 2) - 1] = 0;
            databank[(i * 2)] = 0;
            //record deleted clean up
            for(int r = (i + 1); r < databank[0]; r++){
                databank[((r-1)*2)-1] = databank[(r * 2) - 1];
                databank[((r-1)*2)] = databank[(r * 2)];
            }
            databank[0]--;//remove the added index
        }

    }

    public void addLocation(){
        int databank[] = getShipData();

        int counter = 0;
        if(databank[0] > 0){
            counter = databank[0];
        }
        counter++;

        //write the position
        databank[(counter * 2) + 1] = getMemory(2);//write x
        databank[(counter * 2)] = getMemory(3);//write y
    }

    public int checkLocation(){
        int myX = getMemory(2);
        int myY = getMemory(3);

        int databank[] = getShipData();
        int pointer = 0;

        if(databank[0] > 0){
            int counter = databank[0];

            for(int i = 1; i < counter; i++){
                if(databank[(i * 2) - 1] == myX){
                    if(databank[(i * 2)] == myY){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public boolean getLocation(){
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
            return true;
        }
        return false;
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
