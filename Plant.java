/**
 * [Plant.java]
 * Extends from the LivingBeing class and represents a plant with properties of a living being.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class Plant extends LivingBeing {
  
  /**
   * Constructor: Requires specified health, x and y value.
   * @param health, an integer representing the health value of the plant
   * @param x, an integer representing the x position of the plant
   * @param y, an integer representing the y position of the plant
   */
  Plant(int health, int x, int y){
    super( health, x, y );
  }
  
}