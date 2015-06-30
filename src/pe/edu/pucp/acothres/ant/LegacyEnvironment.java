package pe.edu.pucp.acothres.ant;

import pe.edu.pucp.acothres.ProblemConfiguration;

public class LegacyEnvironment {

  private int numberOfColumns;
  private int numberOfRows;

  private int[][] imageGraph;
  private double pheromoneTrails[][] = null;

  public LegacyEnvironment(int[][] imageGraph) {
    super();
    this.numberOfRows = imageGraph.length;
    this.numberOfColumns = imageGraph[0].length;
    System.out.println("Number of Rows: " + numberOfRows);
    System.out.println("Number of Columns: " + numberOfColumns);
    this.imageGraph = imageGraph;
    this.pheromoneTrails = new double[numberOfRows][numberOfColumns];
  }

  public int[][] getImageGraph() {
    return imageGraph;
  }

  public double[][] getPheromoneTrails() {
    return pheromoneTrails;
  }

  public int getNumberOfColumns() {
    return numberOfColumns;
  }

  public int getNumberOfRows() {
    return numberOfRows;
  }

  public void initializePheromoneMatrix() {
    System.out.println("INITIALIZING PHEROMONE MATRIX");
    double initialPheromoneValue = ProblemConfiguration.INITIAL_PHEROMONE_VALUE;
    if (ProblemConfiguration.MMAS_PHEROMONE_UPDATE) {
      initialPheromoneValue = ProblemConfiguration.MAXIMUM_PHEROMONE_VALUE;
    }

    System.out.println("Initial pheromone value: " + initialPheromoneValue);
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        pheromoneTrails[i][j] = initialPheromoneValue;
      }
    }
  }

  public void performEvaporation() {
    System.out.println("Performing evaporation on all edges");
    System.out
        .println("Evaporation ratio: " + ProblemConfiguration.EVAPORATION);
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        double newValue = pheromoneTrails[i][j]
            * ProblemConfiguration.EVAPORATION;
        if (ProblemConfiguration.MMAS_PHEROMONE_UPDATE
            && newValue < ProblemConfiguration.MINIMUM_PHEROMONE_VALUE) {
          newValue = ProblemConfiguration.MINIMUM_PHEROMONE_VALUE;
        }
        pheromoneTrails[i][j] = newValue;
      }
    }
  }

  public int[][] getNormalizedPheromoneMatrix(int expectedMaximum) {
    System.out.println("Normalizing pheromone matrix");

    int[][] normalizedPheromoneMatrix = new int[numberOfRows][numberOfColumns];
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

    System.out.println("currentMin: " + currentMin);
    System.out.println("currentMax: " + currentMax);

    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        normalizedPheromoneMatrix[i][j] = (int) ((pheromoneTrails[i][j] - currentMin)
            * expectedMaximum / (currentMax - currentMin));
      }
    }
    return normalizedPheromoneMatrix;
  }

}
