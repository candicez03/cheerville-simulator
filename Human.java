import java.util.Random;

/**
 * [Human.java]
 * Extends from the Animal class and
 * represents a human with properties of an animal, as well as and age and whether it has been infected.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

abstract class Human extends Animal {
  private int age;
  private boolean infected;
  static int humanMaturityAge;
  
  /**
   * Constructor: Requires specified health, x and y value, and the maturity age of human
   * @param health, an integer representing the health value of the human
   * @param x, an integer representing the x position of the human
   * @param y, an integer representing the y position of the human
   * @param humanMaturityAge, an integer representing the maturity age of human
   */
  Human( int health, int x, int y, int humanMaturityAge ){
    super( health, x, y );
    this.age = 0;
    this.infected = false;
    this.humanMaturityAge = humanMaturityAge;    
  }

  /**
   * collidesWith 
   * Overrides the method from the super class and 
   * deals with the collsion between this human and the other LivingBeing.
   * @param other, a LivingBeing object that this human collides with
   * @return nothing
   */
  void collidesWith( LivingBeing other ){
    if( other instanceof Plant ) {
      //eat plant and move to the location of the plant
      this.setHealth( this.getHealth()+other.getHealth() );
      other.setHealth(0);
      this.setX(other.getX());
      this.setY(other.getY());
      
    } else if( other instanceof Zombie ) {
      // let the zombie decide what it wants to do with this poor human!
      ((Zombie)other).collidesWith(this);
    }
  }

  
  /**
   * getAge 
   * Returns the age of the human.
   * @return age, an int value representing the age of the human
   */
  int getAge(){
    return this.age;
  }
  
  /**
   * increaseAge 
   * Increases the age of the human by 1.
   * @return nothing
   */
  void increaseAge(){
    this.age++;
  }
  
  /**
   * getInfected 
   * Returns the infected variable of the human.
   * @return infected, a boolean value representing whether the human is infected by a zombie
   */
  boolean getInfected(){
    return this.infected;
  }
  
  /**
   * getInfected 
   * Sets the infected variable of the human.
   * @param infected, a boolean value representing whether the human is infected by a zombie
   * @return nothing
   */
  void setInfected( boolean infected ){
    this.infected = infected;
  }
  
}