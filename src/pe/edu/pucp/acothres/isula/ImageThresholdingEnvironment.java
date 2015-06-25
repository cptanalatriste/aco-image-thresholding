package pe.edu.pucp.acothres.isula;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;

public class ImageThresholdingEnvironment extends Environment {

  public ImageThresholdingEnvironment(double[][] problemGraph)
      throws InvalidInputException {
    super(problemGraph);
  }

  @Override
  protected double[][] createPheromoneMatrix() {
    int numberOfRows = getProblemGraph().length;
    int numberOfColumns = getProblemGraph()[0].length;

    return new double[numberOfRows][numberOfColumns];
  }

}
