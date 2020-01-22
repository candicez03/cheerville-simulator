/**
 * [Male.java]
 * Extends from the Human class and represents a male human with properties of a human.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class Male extends Human {
  
  /**
   * Constructor: Requires specified health, x and y value, and the maturity age of human
   * @param health, an integer representing the health value of the human
   * @param x, an integer representing the x position of the human
   * @param y, an integer representing the y position of the human
   * @param humanMaturityAge, an integer representing the maturity age of human
   */
  Male( int health, int x, int y, int humanMaturityAge ){
    super( health, x, y, humanMaturityAge );
  }
  
  /**
   * collidesWith 
   * Overrides the method from the super class and 
   * deals with the collsion between this male and the other LivingBeing.
   * @param other, a LivingBeing object that this human collides with
   * @return nothing
   */
  void collidesWith( LivingBeing other ){
    if( other instanceof Female ) {
      if( this.canMakeChildWith((Female)other) ) {
        ((Female)other).setSpawnable(true);
      }
    } else {
    super.collidesWith(other);
    }
  }
  
  /**
   * canMakeChildWith 
   * Returns whether the male can make child with another specified human
   * @param otherHuman, a Human object
   * @return boolean, a boolean value representing whether the human can make child with the other human
   */
  boolean canMakeChildWith( Human otherHuman ){
    // true only if the two humans have different genders, are both mature
    // the female can only give birth to a child every specified years(turns)
    if(!(otherHuman instanceof Female)){
      return false;
    } else {
      return ((this.getAge()>=this.humanMaturityAge) &&
             (otherHuman.getAge()>=otherHuman.humanMaturityAge) &&
             (((Female)otherHuman).getBirthCount()>=((Female)otherHuman).getBirthGap()));
    }
  }

}