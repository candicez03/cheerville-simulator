/**
 * [Female.java]
 * Extends from the Human class and
 * represents a female human with properties of a human and the state of spawnable, and the birth count.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class Female extends Human {
  private boolean spawnable;
  private int birthCount;
  private static int birthGap;
  
  /**
   * Constructor: Requires specified health, x and y value, human maturity age, and "the birth gap"
   * ( the female can only spawn no more than 1 child in this time period )
   * @param health, an integer representing the health value of the human
   * @param x, an integer representing the x position of the human
   * @param y, an integer representing the y position of the human
   * @param humanMaturityAge, an integer representing the maturity age of human
   */
  Female( int health, int x, int y, int humanMaturityAge, int birthGap ){
    super( health, x, y, humanMaturityAge );
    this.spawnable = false;
    this.birthGap = birthGap;
    this.birthCount = birthGap;
  }
  
  /**
   * collidesWith 
   * Overrides the method from the super class and 
   * deals with the collsion between this female and the other LivingBeing.
   * @param other, a LivingBeing object that this human collides with
   * @return nothing
   */
  void collidesWith( LivingBeing other ){
    if( other instanceof Male ) {
      if( this.canMakeChildWith((Male)other) ) {
        this.setSpawnable(true);
      }
    } else {
    super.collidesWith(other);
    }
  }
  
  /**
   * canMakeChildWith 
   * Returns whether the female can make child with another specified human
   * @param otherHuman, a Human object
   * @return boolean, a boolean value representing whether the female can make child with the other human
   */
  boolean canMakeChildWith( Human otherHuman ){
    // true only if the two humans have different genders, are both mature
    // the female can only give birth to a child every specified years(turns)
    if(!(otherHuman instanceof Male)){
      return false;
    } else {
      return ((this.getAge()>=this.humanMaturityAge) &&
              (otherHuman.getAge()>=otherHuman.humanMaturityAge) &&
              (this.birthCount>=this.birthGap));
    }
  }
  
  /**
   * getBirthCount 
   * Returns the birth count of female.
   * @return birthCount, an int value that represents the number of year passed
   *                     since the female has given birth to a child
   */
  int getBirthCount(){
    return this.birthCount;
  }
  
  /**
   * setBirthCount 
   * Sets the birth count of female.
   * @return birthCount, an int value that represents the number of year passed
   *                     since the female has given birth to a child
   * @return nothing
   */
  void setBirthCount( int value ){
    this.birthCount = value;
  }
  
  /**
   * getBirthGap 
   * Returns the birth gap of female.
   * @return birthGap, an int value which the female can only spawn no more than 1 child in this time period.
   */
  int getBirthGap(){
    return this.birthGap;
  }
  
  /**
   * getSpawnable 
   * Returns whether the female is spawnable(able to spawn a child).
   * @return spawnable, a boolean value representing whether the female is spawnable
   */
  boolean getSpawnable(){
    return this.spawnable;
  }
  
  /**
   * setSpawnable 
   * Sets the spawnable variable of the female.
   * @param spawnable, a boolean value representing whether the female is spawnable
   * @return nothing
   */
  void setSpawnable( boolean spawnable ){
    this.spawnable = spawnable;
  }
}