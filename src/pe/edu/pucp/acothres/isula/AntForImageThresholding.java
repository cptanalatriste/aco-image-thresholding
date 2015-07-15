package pe.edu.pucp.acothres.isula;

import isula.aco.Ant;
import isula.image.util.ImageFileHelper;
import isula.image.util.ImagePixel;

import java.util.List;

public class AntForImageThresholding extends
    Ant<ImagePixel, EnvironmentForImageThresholding> {

  private static final double DELTA = Float.MIN_VALUE;

  public AntForImageThresholding(int solutionLenght) {
    this.setSolution(new ImagePixel[solutionLenght]);
  }

  @Override
  public boolean isNodeValid(ImagePixel imagePixel) {
    return imagePixel.getGreyScaleValue() != ImageFileHelper.ABSENT_PIXEL_FLAG;
  }

  @Override
  public List<ImagePixel> getNeighbourhood(
      EnvironmentForImageThresholding environment) {
    ImagePixel currentPosition = this.getSolution()[this.getCurrentIndex() - 1];
    return currentPosition.getNeighbourhood(environment.getProblemGraph());
  }

  @Override
  public Double getHeuristicValue(ImagePixel solutionComponent,
      Integer positionInSolution, EnvironmentForImageThresholding environment) {
    double heuristicValue = 1 / Math.abs(solutionComponent.getGreyScaleValue()
        - getSolutionQuality(environment) + DELTA);
    return heuristicValue;
  }

  @Override
  public Double getPheromoneTrailValue(ImagePixel solutionComponent,
      Integer positionInSolution, EnvironmentForImageThresholding environment) {
    double[][] pheromoneTrails = environment.getPheromoneMatrix();
    double pheromoneTrailValue = pheromoneTrails[solutionComponent
        .getxCoordinate()][solutionComponent.getyCoordinate()] + DELTA;
    return pheromoneTrailValue;
  }

  @Override
  public void setPheromoneTrailValue(ImagePixel solutionComponent,
      EnvironmentForImageThresholding environment, Double value) {
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    pheromoneMatrix[solutionComponent.getxCoordinate()][solutionComponent
        .getyCoordinate()] = value;
  }

  @Override
  public double getSolutionQuality(EnvironmentForImageThresholding environment) {
    double grayScaleSum = 0.0;
    for (int i = 0; i < this.getCurrentIndex(); i++) {
      ImagePixel currentPixel = getSolution()[i];
      grayScaleSum = grayScaleSum + currentPixel.getGreyScaleValue();
    }
    return grayScaleSum / this.getCurrentIndex();
  }

  @Override
  public boolean isSolutionReady(EnvironmentForImageThresholding environment) {
    int currentIndex = getCurrentIndex();

    return currentIndex == environment.getNumberOfSteps();
  }

}
