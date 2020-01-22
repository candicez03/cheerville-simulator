import java.util.Scanner;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * [CheervilleSimulator.java]
 * Simulates Town Cheerville regarding the behaviours and interactions between zobies, humans and plants
 *   Features: - Human reproduction is determined based on maturity(age) and gender
 *             - Human babies are spawned at one of the possible locations beside the mother
 *             - Zombies can be placed via mouseclick on the grid
 *             - Humans have a life expectancy; after this age, they have an increasing possibility to die
 *             - Better movements are made so that humans and zombies have different priorities
 *             - Beings are displayed according to their health; the lower it is, the paler the color
 *             - Population data and bar graph is shown on matrix display
 *             - Can select weather by clicking buttons on the screen,
 *               which affects the spawning rate, nutritional value and decay rate of all plants
 *             - Can pause/resume and restart the simulation by clicking buttons on the screen
 * @date      2019/11/23
 * @version   5.0
 * @author    Candice Zhang
 */

class CheervilleSimulator {
  
  public static void main(String[] args) {
    
    Scanner input = new Scanner(System.in);
    
    int rows, cols;
    int humanNum;
    int maxHealth;
    int initPlantNum;
    int plantSpawningRate;
    int plantNutriValue;
    int plantDecayRate;
    int humanMaturityAge;
    int humanLifeExpectancy;
    Town cheerville;
    
    System.out.println("Welcome to Cheerville Simulator! In this simulator, each turn represents 1 year.\nPlease enter the initial configurations so we can start our simulation:");
    System.out.println("size of town(# of rows):");
    rows = input.nextInt();
    System.out.println("size of town(# of columns):");
    cols = input.nextInt();
    System.out.println("number of humans:");
    humanNum = input.nextInt();
    System.out.println("maximum health of humans and zombies:");
    maxHealth = input.nextInt();
    System.out.println("initial number of plants:");
    initPlantNum = input.nextInt();
    System.out.println("plant spawning rate\n(maximum # of plants that can be spawned each turn, on a sunny day):");
    plantSpawningRate= input.nextInt();
    System.out.println("plant nutritional value (on a sunny day):");
    plantNutriValue= input.nextInt();
    System.out.println("plant decay rate (on a sunny day):");
    plantDecayRate = input.nextInt();
    System.out.println("maturity age for human to spawn:");
    humanMaturityAge = input.nextInt();
    System.out.println("life expectancy of human\n(the older the human after it reaches life expectancy, the greater the chance to die):");
    humanLifeExpectancy = input.nextInt();
    
    cheerville = new Town( rows, cols, humanNum, maxHealth, initPlantNum, plantSpawningRate,
                          plantNutriValue, plantDecayRate, humanMaturityAge, humanLifeExpectancy );
    
    System.out.println("Initializing Simulation...");
    
    MatrixDisplayWithMouse display = new MatrixDisplayWithMouse("Cheerville Simulator 5.0", cheerville );
    
    while(true) {
      if( (cheerville.numberOf('H')==0) && (cheerville.getPlayStatus()!=false) ){
        System.out.println("Uh oh... Humans got extinct after " + cheerville.getTurns() + " turn(s)!");
        cheerville.setPlayStatus(false);
      }
      
      if(cheerville.getPlayStatus()==true){ // play
        cheerville.simulate();
      }
      
      display.refresh();
      
      try{ Thread.sleep(120); }catch(Exception e) {}; // Small delay
    }
    
  }
}
