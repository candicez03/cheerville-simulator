/**
 * [LivingBeing.java]
 * Represents a living being with health, x and y value
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

abstract class LivingBeing {
  private int health;
  private int x,y;
  
  /**
   * Constructor: Requires specified health, x and y value
   * @param health, an integer representing the health value of the living being
   * @param x, an integer representing the x position of the living being
   * @param y, an integer representing the y position of the living being
   */
  LivingBeing(int health, int x, int y) {
    this.health = health;
    this.x = x;
    this.y = y;
  }

  /**
   * getHealth 
   * Returns the health of the living being.
   * @return health, an int value representing the health of the living being
   */
  int getHealth() {
    return this.health;
  }
  
  /**
   * setHealth 
   * Sets the health of the living being.
   * @param health, an int value representing the health of the living being
   * @return nothing
   */
  void setHealth(int health) {
    this.health = health;
  }
  
  /**
   * getX
   * Returns the x position of the living being.
   * @return x, an int value representing the x position of the living being
   */
  int getX() {
    return this.x;
  }
  
  /**
   * setX
   * Sets the x position of the living being.
   * @param x, an int value representing the x position of the living being
   * @return nothing
   */
  void setX(int x) {
    this.x = x;
  }
  
  /**
   * getY
   * Returns the y position of the living being.
   * @return y, an int value representing the x position of the living being
   */
  int getY() {
    return this.y;
  }
  
  /**
   * setY
   * Sets the y position of the living being.
   * @param y, an int value representing the y position of the living being
   * @return nothing
   */
  void setY(int y) {
    this.y = y;
  }
  
}