import java.util.Random;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.ArrayList; // used only to decide better movement

/**
 * [Town.java]
 * Represents a town with a grid, number of turns, weathers and other properties.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class Town {
  private LivingBeing[][] map;
  
  private int turnCount;
  
  private int initPlantNum;
  
  private int maxHealth;
  
  private int humanNum;
  private int humanMaturityAge;
  private int humanLifeExpectancy;
  
  private Weather currentWeather;
  private Weather[] weathers;
  
  private boolean playStatus; // true: play, false: pause
    
  Random rand = new Random();
  
  /**
   * Constructor: Requires specified rows, cols, humanNum, maxHealth, initPlantNum, plantSpawningRate,
   * plantNutriValue, humanMaturityAge and humanLifeExpectancy value.
   * @param rows, an int value that represents the number of rows of the grid
   * @param cols, an int value that represents the number of columns of the grid
   * @param humanNum, an int value that represents the initial number of humans of the town
   * @param maxHealth, an int value that represents the maximum health value of humans and zombies
   * @param initPlantNum, an int value that represents the initial number of plants of the town
   * @param plantSpawningRate, an int value that represents the maximum number of plants
   *                           that can be spawned each turn (when the weather is sunny)
   * @param plantNutriValue, an int value representing the nutritional value of plants (when the weather is sunny)
   * @param plantDecayRate, an int value representing the health lost per turn for plants (when the weather is sunny)
   * @param humanMaturityAge, an int value that represents the humans' maturity age
   * @param humanLifeExpectancy, an int value that represents the life expectancy of humans
   */
  Town( int rows, int cols, int humanNum, int maxHealth, int initPlantNum, int plantSpawningRate,
        int plantNutriValue, int plantDecayRate, int humanMaturityAge, int humanLifeExpectancy ) {
    this.map = new LivingBeing[rows][cols];
    this.turnCount = 0;
    this.initPlantNum = initPlantNum;
    this.humanNum = humanNum;
    this.maxHealth = maxHealth;
    this.humanMaturityAge = humanMaturityAge;
    this.humanLifeExpectancy = humanLifeExpectancy;
    this.weathers = new Weather[] { new Weather("sunny", 1, plantSpawningRate, plantNutriValue, plantDecayRate),
                                    new Weather("cloudy", 2, plantSpawningRate/2, plantNutriValue, plantDecayRate+2),
                                    new Weather("rainy", 2, plantSpawningRate+3, plantNutriValue+10, plantDecayRate/2),
                                    new Weather("snowy", 3, plantSpawningRate/3, plantNutriValue+5, plantDecayRate*2) };
    this.currentWeather = this.weathers[0];
    this.playStatus = true;
    this.randomize('H', humanNum);
  }
  
  /**
   * reset 
   * Resets the town with the same user configurations.
   * @return nothing
   */
  void reset(){
    for(int i=0; i<this.getRows(); i++ ){
        for(int j=0; j<this.getCols(); j++ ){
          map[i][j] = null;
        }
    }
    this.turnCount = 0;
    this.currentWeather = this.weathers[0];
    this.playStatus = true;
    this.randomize('H', humanNum);
    this.randomize('P', initPlantNum);
  }
  
  /**
   * simulate 
   * Performs 1 turn of simulation of the town.
   * @return nothing
   */
  void simulate() {
    resetActiveness(); // reset humans and zombies to be active
    randomize('P', this.currentWeather.getPlantSpawningRate()); // grow plants
    ArrayList<int[]> moveOrder = generateOrder();
    
    for(int x=0; x<moveOrder.size(); x++ ){
      int i = moveOrder.get(x)[0], j = moveOrder.get(x)[1];
      if( map[i][j] instanceof Animal) { // only animals can move
        Animal animal = (Animal)map[i][j];
        // if obj is active, perform a move action and check collision
        if ( animal.getActiveness()==true ){ 
          // controls the possibility to move (0: don't move; any other number leads to a move)
          int moveChoice = rand.nextInt(5);
          if( moveChoice!=0 ){
            int[] dest = bestDestOf(animal, decidePriority(animal));
            if (isValid(dest[0],dest[1])){
              if ( map[dest[1]][dest[0]] == null){ // empty spot, move to the destinated location
                map[dest[1]][dest[0]] = animal;
                map[animal.getY()][animal.getX()] = null;
                animal.setX(dest[0]);
                animal.setY(dest[1]);
              } else {
                // check collision: human-plant, human-human, zombie-plant, zombie-human
                animal.collidesWith( map[dest[1]][dest[0]] );
                // updates the status of the being
                updateStatus(animal, j, i);
              }
            }
          }
          animal.setActiveness(false); // set the activeness to false so that the being wont move >1 times in 1 cycle
        }
          
        // if is a human, increase age by 1
        if( animal instanceof Human ) {
          ((Human)animal).increaseAge();
        }
          
        // animals decrease health each turn
        animal.setHealth(animal.getHealth()-this.currentWeather.getHealthLostPerTurn());
          
        updateStatus(animal, j, i);
        
      } else if ( map[i][j] instanceof Plant ){
        // plants decay each turn
        Plant plant = (Plant)map[i][j];
        plant.setHealth(plant.getHealth()-this.currentWeather.getPlantDecayRate());
        updateStatus(plant, j, i);
      }
      
    }
    count();
  }
  
  /**
   * randomize 
   * Randomly places a specified type of LivingBeing with a specified number of instances.
   * @param type, a char value indicating the type of LivingBeing that is to be spawned
   *              ('H': Human, 'P': Plant, 'Z': Zombie)
   * @param instances, an int value representing the numebr of instances that will be spawned
   * @return nothing
   */
  void randomize( char type, int instances ){ 
    if(instances == 0){
      return;
    }
    int count = 0;
    int available;
    while(true){
      available = 0;
      for(int i=0; i<this.getRows(); i++ ){
        for(int j=0; j<this.getCols(); j++ ){
          if( map[i][j]==null ){
            available++;
          }
        }
      }
      if(available==0){
        break;
      }
      // if there are still available spots, generate a possible location to spawn
      while(true){
        int randX = rand.nextInt(this.getCols());
        int randY = rand.nextInt(this.getRows());
        if(this.map[randY][randX]==null){
          this.spawn(type, randX, randY);
          count++;
          break;
        }
      }
      if(count==instances){
        break;
      }
    }
  }
  
  /**
   * updateStatus 
   * Updates the status of a LivingBeing in case the properties of it has changed after collsion. 
   * @param being, a LivingBeing object that is used to check for updates
   * @param mapX, an int value representing the x position of the being on the map
   * @param mapY, an int value representing the y position of the being on the map
   * @return nothing
   */
  void updateStatus( LivingBeing being, int mapX, int mapY ){
    if( being==null ){
      return;
    }
    
    // checks whether a being be removed from the town
    if( being.getHealth()<=0 ){
      this.remove( being );
      return;
    }
    
    // if the being has moved, update its location on the map
    if( (being.getX()!=mapX) || (being.getY()!=mapY) ){
      this.map[being.getY()][being.getX()] = being;
      this.map[mapY][mapX] = null;
    }
    
    // check for humans only
    if ( being instanceof Human ){
      Human humanBeing = (Human)being;
      if( humanBeing.getAge() >= this.getHumanLifeExpectancy() ){
        // each year older than life expectancy increases the death possibility by 1%
        int deathPossibility = humanBeing.getAge()-getHumanLifeExpectancy();
        int deathChoice;
        if( deathPossibility>=100 ){
          deathChoice = 0;
        } else {
          deathChoice = rand.nextInt( 100-deathPossibility );
        }
        if(deathChoice == 0){
          this.remove( being );
          return;
        }
      }
      
      if (humanBeing.getInfected() == true){
        // defines a new zombie with the data of the human and overrides it on the map;
        this.map[being.getY()][being.getX()] = new Zombie(humanBeing);
      }
      
      if (humanBeing instanceof Female){
        if (((Female)humanBeing).getSpawnable() == true ){
        // spawn a new human being at the nearest spot to the human, and set the human to be unspawnable again
        spawnHumanBeside( humanBeing.getX(), humanBeing.getY() );
        ((Female)humanBeing).setSpawnable(false);
        ((Female)humanBeing).setBirthCount(0);
        } else {
          ((Female)humanBeing).setBirthCount(((Female)humanBeing).getBirthCount()+1);
        }
      }
    }
  }
  
  
  /**
   * bestDestOf 
   * Returns the best move under current situation for a certain type of animal.
   * @param animal, an Animal object that is used to decide a best move
   * @param priority, a char array representing priority of the animal,
   *                  which contains the 4 types in the grid in descending order
   * @return int[], an int array that represents the destination(x and y position) of the animal
   */
  int[] bestDestOf( Animal animal, char[] priority ){
    int[][] dests = animal.possibleDests();
    ArrayList<ArrayList<int[]>> priorDests = new ArrayList<ArrayList<int[]>>();
    int plantOrder = indexOf(priority, 'P');
    int humanOrder = indexOf(priority, 'H');
    int zombieOrder = indexOf(priority, 'Z');
    int nullOrder = indexOf(priority, 'N');
    int choice;
    
    for(int i=0;i<priority.length;i++){
      priorDests.add(new ArrayList<int[]>());
    }
    
    for(int i=0;i<dests.length;i++){
      if(isValid(dests[i][0], dests[i][1])){
        if( this.getMap()[dests[i][1]][dests[i][0]] instanceof Plant ){
          (priorDests.get(plantOrder)).add(dests[i]);
        } else if( this.getMap()[dests[i][1]][dests[i][0]] instanceof Human ){
          if(animal instanceof Male){ // if this is a human, only add to list if they can make child
            if( ((Male)animal).canMakeChildWith((Human)this.getMap()[dests[i][1]][dests[i][0]]) ){
              (priorDests.get(humanOrder)).add(dests[i]);
            }
          } else if (animal instanceof Female) {
            if( ((Female)animal).canMakeChildWith((Human)this.getMap()[dests[i][1]][dests[i][0]]) ){
              (priorDests.get(humanOrder)).add(dests[i]);
            }
          } else {
            (priorDests.get(humanOrder)).add(dests[i]);
          }
        } else if( this.getMap()[dests[i][1]][dests[i][0]] instanceof Zombie ){
          (priorDests.get(zombieOrder)).add(dests[i]);
        } else {
          (priorDests.get(nullOrder)).add(dests[i]);
        }
      }
    }
   
    for(int i=0;i<priority.length;i++){
      if( (priorDests.get(i)).size() != 0 ){
        choice = rand.nextInt( (priorDests.get(i)).size() );
        return (priorDests.get(i)).get(choice);
      }
    }
    return new int[]{-1,-1};
  }
  
  /**
   * indexOf 
   * Returns the index of the specified letter in the given char array.
   * @param charArr, a char array that (possibly) contains the specified letter
   * @param letter, a char value that is used to determine the index for
   * @return int, an int value that represents the index of the letter in the char array
   */
  int indexOf( char[] charArr, char letter ){
    for(int i=0; i<charArr.length; i++){
      if(charArr[i]==letter){
        return i;
      }
    }
    return -1;
  }
  
  /**
   * decidePriority 
   * Returns the priority for destination of the animal in descending order
   * @param animal, an Animal object that is used to decide priority for
   * @return char[], a char array that represents priority of the animal in descending order
   */
  char[] decidePriority( Animal animal ) {
    char[] order = new char[4]; // rate plant, human, zombie, null with priority in descending order
    if (animal instanceof Zombie){
      // zombies will always be: collide with humans > destroy plants > other spots since they want humans to be extinct
      order = new char[]{ 'H','P','N','Z' }; // they want to move, so null spot > colliding with a zombie
    } else { // human
      Human human = (Human)animal;
      if( human.getHealth()<(this.getMaxHealth()/20) ){ // lower than 30% health, look for plants
        // do not prioritize reproduction if is minor or overpopulated 
        if( (human.getAge()<this.getHumanMaturityAge()) ||  (this.percentOf('H')>=50) ){
          order = new char[]{ 'P','N','H','Z' };
        } else {
          order = new char[]{ 'P','H','N','Z' };
        }
      } else { // leave plants for other humans, take a walk!
        // do not prioritize reproduction if is minor or overpopulated 
        if( (human.getAge()<this.getHumanMaturityAge()) ||  (this.percentOf('H')>=50) ){
          order = new char[]{ 'N','P','H','Z' };
        } else {
          order = new char[]{ 'N','H','P','Z' };
        }
      }
    }
    return order;
  }
  
  /**
   * percentOf 
   * Returns the percent composition of the specified type of being in the grid.
   * @param type, a char value representing the specified type of being
   * @return int, an int value that represents the percent composition of the specified type
   */
  int percentOf( char type ) {
    int total = this.numberOf('P')+this.numberOf('Z')+this.numberOf('H');
    if (total>0){
      return this.numberOf(type)*100/total;
    } else {
      return 0;
    }
  }
  
  /**
   * numberOf 
   * Returns the number of the specified type of being in the grid.
   * @param type, a char value representing the specified type of being
   * @return int, an int value that represents the number of the specified type of being in the grid
   */
  int numberOf( char type ) {
    int count = 0;
    for(int i=0; i<this.getRows(); i++ ){
      for(int j=0; j<this.getCols(); j++ ){
        if( (type == 'P') && (this.map[i][j] instanceof Plant) ){
          count++;
        } else if( (type == 'H') && (this.map[i][j] instanceof Human) ){
          count++;
        } else if( (type == 'Z') && (this.map[i][j] instanceof Zombie) ){
          count++;
        }
      }
    }
    return count;
  }
  
  /**
   * generateOrder 
   * Returns the list of all corrdinates of the grid in a random order.
   * @return ArrayList<int[]>, an int[] ArrayList containing the coordinates in a random order
   */
  ArrayList<int[]> generateOrder() {
    ArrayList<int[]> order = new ArrayList<>();
    for(int i=0; i<this.getRows(); i++){
      for (int j=0; j<this.getCols(); j++){
        order.add( new int[]{ i,j } );
      }
    }
    Collections.shuffle(order);
    return order;
  }

  void resetActiveness(){
    for(int i=0; i<this.getRows(); i++ ){
      for(int j=0; j<this.getCols(); j++ ){
        if( this.map[i][j] instanceof Animal ){
          ((Animal)map[i][j]).setActiveness(true);
        }
      }
    }
  }
  
  /**
   * spawnHumanBeside 
   * Randomly chooses to spawn at one of the 8 blocks around the parent human that is not taken by an animal
   * @param parentX, an int value representing the x position of the parent human
   * @param parentY, an int value representing the y position of the parent human
   * @return nothing
   */
  void spawnHumanBeside( int parentX, int parentY ){
    ArrayList<int[]> spawnLocations = new ArrayList<int[]>();
    int[] targetLocation = new int[2];
    for( int y=parentY-1; y<=parentY+1; y++){
      for( int x=parentX-1; x<=parentX+1; x++){
        if( isValid(x, y) ){
          if( this.map[y][x] == null ){
            spawnLocations.add(new int[]{x,y});
          } else if ( !(this.map[y][x] instanceof Animal) ){
            spawnLocations.add(new int[]{x,y});
          }
        }
      }
    }
    if( spawnLocations.size()==0 ){ // cannot spawn cuz spots are all taken :(
      return;
    } else {
      targetLocation = spawnLocations.get(rand.nextInt(spawnLocations.size()));
      spawn('H', targetLocation[0], targetLocation[1]);
      return;
    }
  }
  
  /**
   * spawn 
   * Spawn a specified type of LivingBeing at a given location
   * @param type, a char value representing the type of being that will be spawned
   * @param x, an int value representing the x position of the destinated location
   * @param y, an int value representing the y position of the destinated location
   * @return nothing
   */
  void spawn( char type, int x, int y ) {
    if(type == 'H'){
      int genderChoice = rand.nextInt(2);
      if(genderChoice==0){
        this.map[y][x] = new Male( maxHealth, x, y, humanMaturityAge );
      } else {
        this.map[y][x] = new Female( maxHealth, x, y, humanMaturityAge, maxHealth/8 );
      }
    } else if (type=='Z'){
      this.map[y][x] = new Zombie( maxHealth, x, y );
    } else {
      this.map[y][x] = new Plant( this.currentWeather.getPlantNutriValue(), x, y );
    }
    return;
  }
  
  /**
   * remove 
   * Removes a being from the map
   * @param being, a LivingBeing object that will be removed
   * @return nothing
   */
  void remove( LivingBeing being ){ 
    this.map[being.getY()][being.getX()] = null;
  }
  
  /**
   * count 
   * Increases the total turn count by 1.
   * @return nothing
   */
  void count(){
    this.turnCount++;
  }
  
  /**
   * isValid 
   * Returns whether the given pair of coordinates is a valid position on the map.
   * @param x, an int value representing the x position
   * @param y, an int value representing the y position
   * @return boolean, true if 0<=x<=cols and 0<=y<=rows, false if fails to satisfy either of the condition
   */
  boolean isValid( int x, int y ){
    return ( (x>=0)&&(x<this.getCols()) && (y>=0)&&(y<this.getRows()) );
  }
  
  /**
   * getMap 
   * Returns the map of the town.
   * @return map, a LivingBeing 2D array representing the map of the town.
   */
  LivingBeing[][] getMap() {
    return this.map;
  }
  
  /**
   * getRows 
   * Returns the number of rows of the map grid.
   * @return int, an int value representing the number of rows of the map grid
   */
  int getRows(){
    return this.map.length;
  }
  
  /**
   * getCols 
   * Returns the number of columns of the map grid.
   * @return int, an int value representing the number of columns of the map grid
   */
  int getCols(){
    return this.map[0].length;
  }
  
  /**
   * getCols 
   * Returns the number of turns of simulation that has been done.
   * @return int, an int value representing the number of turns passed in the town
   */
  int getTurns(){
    return this.turnCount;
  }
  
  /**
   * getMaxHealth 
   * Returns the maximum health of animals.
   * @return maxHealth, an int value representing the maximum health of animals
   */
  int getMaxHealth(){
    return this.maxHealth;
  }
  
  /**
   * getHumanLifeExpectancy 
   * Returns the life expectancy of humans.
   * @return humanLifeExpectancy, an int value representing the life expectancy of humans
   */
  int getHumanLifeExpectancy(){
    return this.humanLifeExpectancy;
  }
  
  /**
   * getHumanMaturityAge 
   * Returns the maturity age of humans.
   * @return humanMaturityAge, an int value representing the maturity age of humans
   */
  int getHumanMaturityAge(){
    return this.humanMaturityAge;
  }
  
  /**
   * getPlayStatus 
   * Returns the play status of the town.
   * @return playStatus, a boolean value representing the play status of the town.(0: play, 1:pause, 2:restart)
   */
  boolean getPlayStatus(){
    return this.playStatus;
  }
  
  /**
   * setPlayStatus 
   * Sets the play status of the town.
   * @param status, a boolean value that determines the play status of the town.(0: play, 1:pause, 2:restart)
   * @return nothing
   */
  void setPlayStatus( boolean status ){
    this.playStatus = status;
  }
  
  /**
   * getWeathers 
   * Returns all possible weathers the town has.
   * @return weathers, an Weather[] representing possible weathers the town has
   */
  Weather[] getWeathers(){
    return this.weathers;
  }
  
  /**
   * getCurrentWeather 
   * Returns the current weather of the town
   * @return currentWeather, an Weather object representing the current weather of the town
   */
  Weather getCurrentWeather(){
    return this.currentWeather;
  }
  
  /**
   * setCurrentWeather 
   * Sets the current weather of the town
   * @param weatherName, an String object that indicates the name of the weather
   * @return nothing
   */
  void setCurrentWeather(String weatherName){
    for(int i=0; i<this.weathers.length;i++){
      if( this.weathers[i].getName().equals(weatherName) ) {
        this.currentWeather = this.weathers[i];
      }
    }
    // update plant nutritional value
    for(int i=0; i<this.getRows(); i++ ){
      for(int j=0; j<this.getCols(); j++ ){
        if( this.map[i][j] instanceof Plant ){
          ((Plant)this.map[i][j]).setHealth( Math.min( ((Plant)this.map[i][j]).getHealth(), this.currentWeather.getPlantNutriValue() ) );
        }
      }
    }
    
  }
  
  /**
   * Inner Class: Weather
   * Represents a weather that determines
   * the health lost per turn, plant spawning rate and plant nutritional value.
   */
  class Weather {
    private String name;
    private int healthLostPerTurn;
    private int plantSpawningRate;
    private int plantNutriValue;
    private int plantDecayRate;
    
    /**
     * Constructor: Requires specified name, health lost per turn, plant spawning rate and plant nutritional value.
     * @param name, a String representing the name of the weather
     * @param healthLostPerTurn, an int value representing the health lost per turn for animals
     * @param plantSpawningRate, an int value representing the plant spawning rate
     * @param plantNutriValue, an int value representing the plant nutritional value
     */
    Weather(String name, int healthLostPerTurn, int plantSpawningRate, int plantNutriValue, int plantDecayRate){
      this.name = name;
      this.healthLostPerTurn = healthLostPerTurn;
      this.plantSpawningRate = plantSpawningRate;
      this.plantNutriValue = plantNutriValue;
      this.plantDecayRate = plantDecayRate;
    }
    
    /**
     * getHealth 
     * Returns the name of the weather.
     * @return name, a String representing the name of the weather
     */
    String getName(){
      return this.name;
    }
    
    /**
     * getHealthLostPerTurn 
     * Returns the health lost per turn for animals.
     * @return healthLostPerTurn, an int value representing the health lost per turn
     */
    int getHealthLostPerTurn(){
      return this.healthLostPerTurn;
    }
    
    /**
     * getPlantSpawningRate 
     * Returns the plant spawning rate.
     * @return plantSpawningRate, an int value representing the plant spawning rate
     */
    int getPlantSpawningRate(){
      return this.plantSpawningRate;
    }
    
    /**
     * getPlantNutriValue 
     * Returns the plant nutritional value.
     * @return plantNutriValue, an int value representing the plant nutritional value
     */
    int getPlantNutriValue(){
      return this.plantNutriValue;
    }
    
    /**
     * getPlantDecayRate 
     * Returns the plant decay rate.
     * @return plantDecayRate, an int value representing the health lost per turn for plants
     */
    int getPlantDecayRate(){
      return this.plantDecayRate;
    }
  }
  
}