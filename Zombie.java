/**
 * [Zombie.java]
 * Extends from the Animal class and
 * represents a zombie with properties of an animal.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class Zombie extends Animal {
  /**
   * Constructor: Requires specified health, x and y value.
   * @param health, an integer representing the health value of the plant
   * @param x, an integer representing the x position of the plant
   * @param y, an integer representing the y position of the plant
   */
  Zombie(int health, int x, int y) {
      super( health, x, y );
  }
    
  /**
   * Constructor: Requires a Human object
   * @param humanBeing, a Human object that is used to determine the properties of the zombie
   */
  Zombie( Human humanBeing ) { // 
      super( humanBeing.getHealth(), humanBeing.getX(), humanBeing.getY() );
  }
    
  /**
   * collidesWith 
   * Overrides the method from the super class and 
   * deals with the collsion between this zombie and the other LivingBeing.
   * @param other, a LivingBeing object that this zombie collides with
   * @return nothing
   */
  void collidesWith( LivingBeing other ){
    if( other instanceof Plant ) {
      //destroy plant and move to the location of the plant
      other.setHealth(0);
      this.setX(other.getX());
      this.setY(other.getY());
        
    } else if( other instanceof Human ) {
      if( this.getHealth()>other.getHealth() ){ //1) If the zombie has a health > human then human is killed
        this.setHealth( this.getHealth()+other.getHealth() );
        other.setHealth(0);
        this.setX(other.getX());
        this.setY(other.getY());
      } else { //2) If the zombie has a health <= human then human is infected and becomes a zombie
        ((Human)other).setInfected(true);
      }
        
    } else if( other instanceof Zombie ) {
      return; // nothing happens!
    }
  }
}