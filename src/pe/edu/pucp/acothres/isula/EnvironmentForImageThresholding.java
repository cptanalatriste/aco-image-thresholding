package pe.edu.pucp.acothres.isula;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;

public class EnvironmentForImageThresholding extends Environment {

  private int numberOfSteps;

  public EnvironmentForImageThresholding(double[][] problemGraph,
      int numberOfSteps) throws InvalidInputException {
    super(problemGraph);
    this.numberOfSteps = numberOfSteps;
  }

  @Override
  protected double[][] createPheromoneMatrix() {
    double[][] problemGraph = this.getProblemGraph();
    return new double[problemGraph.length][problemGraph[0].length];
  }

  public int getNumberOfSteps() {
    return numberOfSteps;
  }

}
