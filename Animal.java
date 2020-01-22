/**
 * [Animal.java]
 * Extends from the LivingBeing class and
 * represents an animal with properties of a living being and the state of activeness;
 * is able to perform collsion and get possible destinations for movement.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

abstract class Animal extends LivingBeing implements Moveable {
  private boolean activeness;
  
  /**
   * Constructor: Requires specified health, x and y value
   * @param health, an integer representing the health value of the animal
   * @param x, an integer representing the x position of the animal
   * @param y, an integer representing the y position of the animal
   */
  Animal( int health, int x, int y ){
    super( health, x, y );
    this.activeness = true;
  }
  
  
  /**
   * getActiveness 
   * Returns the activeness of the animal.
   * @return activeness, a boolean value representing the activeness(ability to make moves) of the animal
   */
  boolean getActiveness() {
    return this.activeness;
  }
  
  /**
   * setActiveness 
   * Sets the activeness of the animal.
   * @param activeness, a boolean value that determines the activeness(ability to make moves) of the animal
   * @return nothing
   */
  void setActiveness( boolean activeness ) {
    this.activeness = activeness;
  }
  
  /**
   * collidesWith
   * Deals with the collsion between this animal and the other LivingBeing
   * @param other, a LivingBeing object that this animal collides with
   * @return nothing
   */
  public void collidesWith( LivingBeing other ) {
    return;
  }
  
  /**
   * possibleDests
   * Returns all coordinates of destinations that the animal can move to.
   * @return int[][], a 2D int array that contains coordinates of the possible destinations in 4 directions
   */
  public int[][] possibleDests(){
    int curX = this.getX(), curY = this.getY();
    return new int[][]{ {curX-1, curY}, {curX+1, curY}, {curX, curY-1}, {curX, curY+1} };
  }
  
}