/**
 * [Moveable.java]
 * An interface for moveable beings.
 * The user of this interface is expected to deal with collisions and potential movements.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */
public interface Moveable {
    /**
     * collidesWith
     * Deals with the collsion between this and the other LivingBeing
     * @param other, a LivingBeing object that this animal collides with
     * @return nothing
     */
    void collidesWith( LivingBeing other );

    /**
     * possibleDests
     * Returns all coordinates of destinations that the moveable being can move to.
     * @return int[][], a 2D int array that contains coordinates of the possible destinations in 4 directions
     */
    int[][] possibleDests();
}