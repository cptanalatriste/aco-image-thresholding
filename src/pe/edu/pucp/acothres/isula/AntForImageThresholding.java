package pe.edu.pucp.acothres.isula;

import isula.aco.Ant;
import isula.aco.Environment;

import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acothres.ProblemConfiguration;

import java.util.List;

public class AntForImageThresholding extends Ant<ImagePixel> {

  public AntForImageThresholding(int solutionLenght) {
    this.setSolution(new ImagePixel[solutionLenght]);
  }

  @Override
  public boolean isNodeValid(ImagePixel imagePixel) {
    return imagePixel.getGreyScaleValue() != ProblemConfiguration.ABSENT_PIXEL_FLAG;
  }

  @Override
  public List<ImagePixel> getNeighbourhood(Environment environment) {
    ImagePixel currentPosition = this.getSolution()[this.getCurrentIndex() - 1];
    return currentPosition.getNeighbourhood(environment.getProblemGraph());
  }

  @Override
  public Double getHeuristicValue(ImagePixel solutionComponent,
      Integer positionInSolution, Environment environment) {
    double heuristicValue = 1 / Math.abs(solutionComponent.getGreyScaleValue()
        - getSolutionQuality(environment) + ProblemConfiguration.DELTA);
    return heuristicValue;
  }

  @Override
  public Double getPheromoneTrailValue(ImagePixel solutionComponent,
      Integer positionInSolution, Environment environment) {
    double[][] pheromoneTrails = environment.getPheromoneMatrix();
    double pheromoneTrailValue = pheromoneTrails[solutionComponent
        .getxCoordinate()][solutionComponent.getyCoordinate()]
        + ProblemConfiguration.DELTA;
    return pheromoneTrailValue;
  }

  @Override
  public void setPheromoneTrailValue(ImagePixel solutionComponent,
      Environment environment, Double value) {
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    pheromoneMatrix[solutionComponent.getxCoordinate()][solutionComponent
        .getyCoordinate()] = value;
  }

  @Override
  public double getSolutionQuality(Environment environment) {
    double grayScaleSum = 0.0;
    for (int i = 0; i < this.getCurrentIndex(); i++) {
      ImagePixel currentPixel = getSolution()[i];
      grayScaleSum = grayScaleSum + currentPixel.getGreyScaleValue();
    }
    return grayScaleSum / this.getCurrentIndex();
  }

  @Override
  public boolean isSolutionReady(Environment env) {
    EnvironmentForImageThresholding environment = (EnvironmentForImageThresholding) env;
    return getCurrentIndex() == environment.getNumberOfSteps();
  }

}
