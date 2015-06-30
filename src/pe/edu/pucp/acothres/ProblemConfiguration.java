package pe.edu.pucp.acothres;

import isula.aco.ConfigurationProvider;

public class ProblemConfiguration implements ConfigurationProvider {

  // Credits:
  // https://www.eecs.berkeley.edu/Research/Projects/CS/vision/bsds/BSDS300/html/dataset/images/gray/296059.html
  // http://brainweb.bic.mni.mcgill.ca/brainweb/anatomic_normal_20.html
  public static final String INPUT_DIRECTORY = "C:/Users/CarlosG/Documents/GitHub/ACOImageThresholdingWithIsula/inputImg/";
  public static final String OUTPUT_DIRECTORY = "C:/Users/CarlosG/Documents/GitHub/ACOImageThresholdingWithIsula/outputImg/";

  public static final String IMAGE_FILE = "19952transverse2_64.gif";
  public static final String OUTPUT_IMAGE_FILE = "output.bmp";
  public static final String PHEROMONE_IMAGE_FILE = "pheromone.bmp";
  public static final String ORIGINAL_IMAGE_FILE = "original.bmp";
  public static final String CLUSTER_IMAGE_FILE = "cluster.bmp";

  public static final double EVAPORATION = 0.5;

  // This features were disabled as it affects the quality of solutuion.
  public static final double BEST_CHOICE_PROBABILITY = 0.0;
  public static final boolean ONLY_BEST_CAN_UPDATE = false;
  public static boolean MMAS_PHEROMONE_UPDATE = false;
  public static final boolean ALLOW_VISITED_PIXELS = true;
  public static final boolean DEPOSITE_PHEROMONE_ONLINE = true;
  public static final boolean RANDOMIZE_BEFORE_BUILD = true;

  // Max-Min Ant System Pheromone parameters
  public static final double MAXIMUM_PHEROMONE_VALUE = 0.0001;
  public static final double MINIMUM_PHEROMONE_VALUE = MAXIMUM_PHEROMONE_VALUE / 5;

  // This are values from the original paper
  public static final int MAX_ITERATIONS = 5;
  public static final int NUMBER_OF_STEPS = 15;
  public static final int PHEROMONE_IMPORTANCE = 1;
  public static final int HEURISTIC_IMPORTANCE = 5;
  public static final double EXTRA_WEIGHT = 0.6;

  public static final int COST_FUNCTION_PARAMETER_A = 500;
  public static final int COST_FUNCTION_PARAMETER_B = 10;

  public static final double INITIAL_PHEROMONE_VALUE = Float.MIN_VALUE;
  public static final int NUMBER_OF_CLUSTERS = 2;
  public static final boolean USE_PHEROMONE_FOR_CLUSTERING = true;
  public static final boolean USE_GREYSCALE_FOR_CLUSTERING = true;

  public static final double DELTA = Float.MIN_VALUE;

  public static final int GRAYSCALE_MIN_RANGE = 0;
  public static final int GRAYSCALE_MAX_RANGE = 255;
  public static final int ABSENT_PIXEL_FLAG = -1;
  public static final int ABSENT_PIXEL_CLUSTER = -1;
  public static final int GRAYSCALE_DELTA = 10;
  public static final int[][] DEFAULT_STRUCTURING_ELEMENT = {
      { 255, 255, 255 }, { 255, 255, 255 }, { 255, 255, 255 } };
  public static final int OPENING_REPETITION_PARAMETER = 1;

  public static final String currentConfigurationAsString() {
    String result = "Input file: " + IMAGE_FILE + "\n";
    result = result + "Evaporation parameter: " + EVAPORATION + "\n";
    result = result + "Number of steps: " + NUMBER_OF_STEPS + "\n";
    result = result + "Number of iterations: " + MAX_ITERATIONS + "\n";
    result = result + "Pheromone importance: " + PHEROMONE_IMPORTANCE + "\n";
    result = result + "Heuristic importance: " + HEURISTIC_IMPORTANCE + "\n";

    result = result + "Extra weight: " + EXTRA_WEIGHT + "\n";
    result = result + "Parameter A for Cost Function: "
        + COST_FUNCTION_PARAMETER_A + "\n";
    result = result + "Parameter B for Cost Function: "
        + COST_FUNCTION_PARAMETER_B + "\n";
    result = result + "Initial Pheromone Value: " + INITIAL_PHEROMONE_VALUE
        + "\n";
    result = result + "Number of Clústers: " + NUMBER_OF_CLUSTERS + "\n";

    return result;
  }

  public int getNumberOfAnts() {
    return 0;
  }

  public double getEvaporationRatio() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int getNumberOfIterations() {
    // TODO Auto-generated method stub
    return 0;
  }

  public double getInitialPheromoneValue() {
    // TODO Auto-generated method stub
    return 0;
  }
}
