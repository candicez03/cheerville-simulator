import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Font;

/**
 * [MatrixDisplayWithMouse.java]
 * Displays the grid of a town and population data and enables mouseclick interations.
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class MatrixDisplayWithMouse extends JFrame {
  private int maxX, maxY;
  private int GridToScreenRatio;
  private Town town;
  private LivingBeing[][] matrix;
  
  private int sunnyX, sunnyY;
  private int cloudyX, cloudyY;
  private int rainyX, rainyY;
  private int snowyX, snowyY;
  private int playPauseX, playPauseY;
  private int restartX, restartY;
  private int buttonSize;
  
  private static Image playImg  = Toolkit.getDefaultToolkit().getImage("images/play.png");
  private static Image pauseImg  = Toolkit.getDefaultToolkit().getImage("images/pause.png");
  private static Image restartImg  = Toolkit.getDefaultToolkit().getImage("images/restart.png");
  
  private static Image sunnyImg  = Toolkit.getDefaultToolkit().getImage("images/sunny.png");
  private static Image cloudyImg  = Toolkit.getDefaultToolkit().getImage("images/cloudy.png");
  private static Image rainyImg  = Toolkit.getDefaultToolkit().getImage("images/rainy.png");
  private static Image snowyImg  = Toolkit.getDefaultToolkit().getImage("images/snowy.png");
  
  private static Image zombieImg = Toolkit.getDefaultToolkit().getImage("images/zombie.jpg");
  private static Image humanImg = Toolkit.getDefaultToolkit().getImage("images/human.jpg");
  private static Image plantImg  = Toolkit.getDefaultToolkit().getImage("images/plant.jpg");

  /**
   * Constructor: Requires title and the Town object that is used to create a matrix display with mouse
   * @param title, a String representing the title that will be displayed
   * @param town, a Town object that is used for display and mouseclick interactions
   */
  MatrixDisplayWithMouse(String title, Town town) {
    super(title);
    this.town = town;
    this.matrix = town.getMap();
    this.maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
    this.maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    this.sunnyX = maxX/9*5;
    this.sunnyY = maxY/15*9;
    
    this.cloudyX = maxX/18*11;
    this.cloudyY = maxY/15*9;
    
    this.rainyX = maxX/9*5;
    this.rainyY = maxY/10*7;
    
    this.snowyX = maxX/18*11;
    this.snowyY = maxY/10*7;
    
    this.buttonSize = maxX/25;
    
    this.playPauseX = maxX-2*buttonSize-70;
    this.playPauseY = 30;
    
    this.restartX = maxX-buttonSize-50;
    this.restartY = 30;
    
    //ratio to fit in screen as square map
    GridToScreenRatio = Math.min( (maxX/2 / (matrix[0].length+1)), (maxY / (matrix.length+1)) );
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    
    this.add(new MatrixPanel());
    
    this.setVisible(true);
    
  }
  
  /**
   * refresh 
   * Repaints the matrix to the display.
   * @return nothing
   */
  public void refresh() { 
    this.repaint();
  }
  
  
  /**
   * Class: MatrixPanel
   * Extends from the JPanel class; adds the mouse listener and overrides the paintComponent method.
   */
  class MatrixPanel extends JPanel {
    /**
     * Constructor: Requires no parameters
     * Adds a MatrixPanelMouseListener object.
     */
    MatrixPanel() { 
      addMouseListener(new MatrixPanelMouseListener());
    }
    
    /**
     * paintComponent 
     * Draws graphics to the display window.
     * @param g, a Graphics object that is used to draw graphics contexts.
     */
    public void paintComponent(Graphics g) {
      String curWeatherName = town.getCurrentWeather().getName();
      // Displays the martix accoring to the type of object in matrix[i][j]
      for(int i = 0; i<matrix.length;i++)  { 
        for(int j = 0; j<matrix[0].length;j++)  {
          if ( matrix[i][j] == null ){
            g.setColor(Color.WHITE);
            g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          } else {
            // the transparency of the being is ralated to its health
            int maxHealth;
            Color beingColor;
            double healthPercent;
            int colorValue;
            
            if( ( matrix[i][j] instanceof Plant )){
              maxHealth = town.getCurrentWeather().getPlantNutriValue();
            } else {
              maxHealth = town.getMaxHealth();
            }
            
            healthPercent = (((LivingBeing)matrix[i][j]).getHealth())*1.0/maxHealth;
            colorValue = (int) Math.round(255*healthPercent);
            if(colorValue>255){
              colorValue = 255;
            }
            
            if ( matrix[i][j] instanceof Zombie ) {
              beingColor = new Color(255,(255-colorValue),(255-colorValue)); // red
            } else if ( matrix[i][j] instanceof Male ) {
              beingColor = new Color((255-colorValue),(255-colorValue),255); // blue
            } else if ( matrix[i][j] instanceof Female ) {
              beingColor = new Color(255,(255-colorValue),255); // purple
            } else {
              beingColor = new Color((255-colorValue),255,(255-colorValue)); // green
            }
            g.setColor(beingColor);
            g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          }
          
          
          // Draw Outline
          g.setColor(Color.BLACK);
          g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
      
      // Display: buttons (weathers, play/pause/restart)
      if(town.getPlayStatus()==true){
        g.drawImage(pauseImg, playPauseX, playPauseY, buttonSize, buttonSize, null);
      } else {
        g.drawImage(playImg, playPauseX, playPauseY, buttonSize, buttonSize, null);
      }
      g.drawImage(restartImg, restartX, restartY, buttonSize, buttonSize, null);
      
      g.drawImage(sunnyImg, sunnyX, sunnyY, buttonSize, buttonSize, null);
      g.drawImage(cloudyImg, cloudyX, cloudyY, buttonSize, buttonSize, null);
      g.drawImage(rainyImg, rainyX, rainyY, buttonSize, buttonSize, null);
      g.drawImage(snowyImg, snowyX, snowyY, buttonSize, buttonSize, null);
      g.setColor(Color.RED);
      if( curWeatherName.equals("sunny") ){
        g.drawRect(sunnyX, sunnyY, buttonSize, buttonSize);
      } else if( curWeatherName.equals("cloudy") ){
        g.drawRect(cloudyX, cloudyY, buttonSize, buttonSize);
      } else if( curWeatherName.equals("rainy") ){
        g.drawRect(rainyX, rainyY, buttonSize, buttonSize);
      } else {
        g.drawRect(snowyX, snowyY, buttonSize, buttonSize);
      }
      
      
      // Display: number of turns, population / percent composition / bar graph of each species,
      //          effects of weather (plant spawning rate, plant nutritional value, health lost per turn)
      g.setColor(Color.BLACK);
      g.setFont( g.getFont().deriveFont(g.getFont().getSize() * 2.0F) );
      g.drawString("Cheerville Simulator", maxX/9*5, maxY/15);
      g.setFont( g.getFont().deriveFont(g.getFont().getSize() * 0.6F) );
      g.drawString("Number of turns(years): "+town.getTurns(), maxX/9*5, maxY/15*2);
      
      g.drawString("Number of human(s): "+town.numberOf('H'), maxX/9*5, maxY/15*3);
      g.drawImage(humanImg, maxX/9*5, maxY/30*7, maxY/15, maxY/15, null);
      g.drawString(town.percentOf('H')+"%", maxX/5*3, maxY/30*8);
      g.setColor(Color.BLUE);
      g.fillRect(maxX/20*13, maxY/30*7, town.percentOf('H')*3, maxY/15);
      
      g.setColor(Color.BLACK);
      g.drawString("Number of zombie(s): "+town.numberOf('Z'), maxX/9*5, maxY/15*5);
      g.drawImage(zombieImg, maxX/9*5, maxY/30*11, maxY/15, maxY/15, null); 
      g.drawString(town.percentOf('Z')+"%", maxX/5*3, maxY/30*12);
      g.setColor(Color.RED);
      g.fillRect(maxX/20*13, maxY/30*11, town.percentOf('Z')*3, maxY/15);
      
      g.setColor(Color.BLACK);
      g.drawString("Number of plant(s): "+town.numberOf('P'), maxX/9*5, maxY/15*7);
      g.drawImage(plantImg, maxX/9*5, maxY/2, maxY/15, maxY/15, null);
      g.drawString(town.percentOf('P')+"%", maxX/5*3, maxY/15*8);
      g.setColor(Color.GREEN);
      g.fillRect(maxX/20*13, maxY/2, town.percentOf('P')*3, maxY/15);
      
      g.setColor(Color.BLACK);
      g.drawString("Plant spawning rate: "+town.getCurrentWeather().getPlantSpawningRate(), maxX/9*6, sunnyY+20);
      g.drawString("Plant nutritional value: "+town.getCurrentWeather().getPlantNutriValue(), maxX/9*6, (sunnyY+rainyY)/2+20);
      g.drawString("Health lost per turn (for animals): "+town.getCurrentWeather().getHealthLostPerTurn(), maxX/9*6, rainyY+20);
      g.drawString("Health lost per turn (for plants): "+town.getCurrentWeather().getPlantDecayRate(), maxX/9*6, rainyY+50);
      super.repaint(); // refresh screen to update
    }
  }
  
  
  /**
   * Inner Class: MatrixPanelMouseListener
   * Listens to and handles mouse events.
   */
  class MatrixPanelMouseListener implements MouseListener{
    
    /**
     * mouseClicked 
     * Handles mouseclick events.
     * @param e, a MouseEvent object that represents a mouseclick action
     */
    public void mouseClicked(MouseEvent e) {
    }
    
    /**
     * mousePressed 
     * Handles mouse pressed events.
     * @param e, a MouseEvent object that represents a mouse pressed action
     */
    public void mousePressed(MouseEvent e) {
      int screenX = e.getPoint().x, screenY = e.getPoint().y;
      int gridX = screenX/GridToScreenRatio, gridY = screenY/GridToScreenRatio;
      
      if( town.isValid(gridX, gridY) ){
        town.spawn('Z', gridX, gridY);
      }
      
      if ( isInside(screenX, screenY, sunnyX, sunnyY, buttonSize) ){
        town.setCurrentWeather("sunny");
      } else if ( isInside(screenX, screenY, cloudyX, cloudyY, buttonSize) ){
        town.setCurrentWeather("cloudy");
      } else if ( isInside(screenX, screenY, rainyX, rainyY, buttonSize) ){
        town.setCurrentWeather("rainy");
      } else if ( isInside(screenX, screenY, snowyX, snowyY, buttonSize) ){
        town.setCurrentWeather("snowy");
      } else if ( isInside(screenX, screenY, playPauseX, playPauseY, buttonSize) ){
        if(town.getPlayStatus()==true){
          town.setPlayStatus(false);
        } else if(town.numberOf('H')>0) {
          town.setPlayStatus(true);
        }
      } else if ( isInside(screenX, screenY, restartX, restartY, buttonSize) ){
        System.out.println("Restarting with current configurations...");
        town.reset();
      }
    }
    
    /**
     * mouseReleased 
     * Handles mouse released events.
     * @param e, a MouseEvent object that represents a mouse released action
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * mouseEntered 
     * Handles mouse entered events.
     * @param e, a MouseEvent object that represents a mouse entered action
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * mouseExited 
     * Handles mouse exited events.
     * @param e, a MouseEvent object that represents a mouse exited action
     */
    public void mouseExited(MouseEvent e) {
    }
    
   /**
    * isInside 
    * Determines whether the given pair of coordinates is inside the given location of a button.
    * @param curX, the x position of the mouse event
    * @param curY, the y position of the mouse event
    * @param buttonX, the x position of the button
    * @param buttonY, the y position of the button
    * @param sideLength, the side length of the button
    * @return boolean, ture if curX and curY is inside the button, false if any of them are not
    */
    boolean isInside(int curX, int curY, int buttonX, int buttonY, int sideLength){
      return ( (curX >= buttonX) && (curX <= buttonX+sideLength) &&
               (curY >= buttonY) && (curY <= buttonY+sideLength) );
    }
    
  }
  
}