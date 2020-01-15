package pe.edu.pucp.acothres.isula;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;

import java.util.logging.Logger;

public class EnvironmentForImageThresholding extends Environment {

  private static Logger logger = Logger
      .getLogger(EnvironmentForImageThresholding.class.getName());

  private int numberOfSteps;

  public EnvironmentForImageThresholding(double[][] problemGraph,
      int numberOfSteps) throws InvalidInputException {
    super(problemGraph);
    this.numberOfSteps = numberOfSteps;
  }

  @Override
  protected double[][] createPheromoneMatrix() {
    double[][] problemGraph = this.getProblemRepresentation();
    return new double[problemGraph.length][problemGraph[0].length];
  }

  public int getNumberOfSteps() {
    return numberOfSteps;
  }

  public int getNumberOfRows() {
    return getProblemRepresentation().length;
  }

  public int getNumberOfColumns() {
    return getProblemRepresentation()[0].length;
  }

  /**
   * Normalizes the pheromone matrix.
   * 
   * @param expectedMaximum
   *          Expected maximum.
   * @return Normalized matrix.
   */
  public int[][] getNormalizedPheromoneMatrix(int expectedMaximum) {
    logger.info("Normalizing pheromone matrix");

    int numberOfRows = this.getNumberOfRows();
    int numberOfColumns = this.getNumberOfColumns();
    int[][] normalizedPheromoneMatrix = new int[numberOfRows][numberOfColumns];
    double[][] pheromoneTrails = this.getPheromoneMatrix();

    double currentMin = pheromoneTrails[0][0];
    double currentMax = pheromoneTrails[0][0];
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        if (pheromoneTrails[i][j] < currentMin) {
          currentMin = pheromoneTrails[i][j];
        } else if (pheromoneTrails[i][j] > currentMax) {
          currentMax = pheromoneTrails[i][j];
        }
      }
    }

    logger.info("currentMin: " + currentMin);
    logger.info("currentMax: " + currentMax);

    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        normalizedPheromoneMatrix[i][j] = (int) ((pheromoneTrails[i][j] - currentMin)
            * expectedMaximum / (currentMax - currentMin));
      }
    }
    return normalizedPheromoneMatrix;
  }

}
