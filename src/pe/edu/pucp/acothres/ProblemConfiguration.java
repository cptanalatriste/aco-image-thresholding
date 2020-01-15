package pe.edu.pucp.acothres;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.acs.AcsConfigurationProvider;

public class ProblemConfiguration implements ConfigurationProvider,
        AcsConfigurationProvider {

    // Credits:
    // https://www.eecs.berkeley.edu/Research/Projects/CS/vision/bsds/BSDS300/html/dataset/images/gray/296059.html
    // http://brainweb.bic.mni.mcgill.ca/brainweb/anatomic_normal_20.html
    public static final String INPUT_DIRECTORY = "inputImg/";
    public static final String OUTPUT_DIRECTORY = "";

    public static final String IMAGE_FILE = "19952transverse2_64.gif";
    public static final String OUTPUT_IMAGE_FILE = "output.bmp";
    public static final String PHEROMONE_IMAGE_FILE = "pheromone.bmp";
    public static final String ORIGINAL_IMAGE_FILE = "original.bmp";
    public static final String CLUSTER_IMAGE_FILE = "cluster.bmp";
    public static final int NUMBER_OF_STEPS = 15;
    public static final double EXTRA_WEIGHT = 0.6;

    public static final int COST_FUNCTION_PARAMETER_A = 500;
    public static final int COST_FUNCTION_PARAMETER_B = 10;
    public static final int NUMBER_OF_CLUSTERS = 2;
    public static final int ABSENT_PIXEL_CLUSTER = -1;
    public static final int OPENING_REPETITION_PARAMETER = 1;

    private static final double EVAPORATION = 0.5;

    // This features were disabled as it affects the quality of solutuion.
    private static final double BEST_CHOICE_PROBABILITY = 0.0;

    // This are values from the original paper
    private static final int MAX_ITERATIONS = 5;
    private static final int PHEROMONE_IMPORTANCE = 1;
    private static final int HEURISTIC_IMPORTANCE = 5;
    private static final double INITIAL_PHEROMONE_VALUE = Float.MIN_VALUE;

    /**
     * Returns the current configuration as a String.
     *
     * @return String representing the current configuration.
     */
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
        result = result + "Number of Clusters: " + NUMBER_OF_CLUSTERS + "\n";

        return result;
    }

    public int getNumberOfAnts() {
        // This value is calculated at runtime.
        return 0;
    }

    public double getEvaporationRatio() {
        return EVAPORATION;
    }

    public int getNumberOfIterations() {
        return MAX_ITERATIONS;
    }

    public double getInitialPheromoneValue() {
        return INITIAL_PHEROMONE_VALUE;
    }

    public double getBestChoiceProbability() {
        return BEST_CHOICE_PROBABILITY;
    }

    public double getHeuristicImportance() {
        return HEURISTIC_IMPORTANCE;
    }

    public double getPheromoneImportance() {
        return PHEROMONE_IMPORTANCE;
    }
}
