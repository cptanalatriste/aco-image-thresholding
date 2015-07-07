package pe.edu.pucp.acothres.isula;

import java.util.List;

import isula.aco.Ant;
import isula.aco.Environment;
import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acothres.ProblemConfiguration;

public class AntForImageThresholding extends Ant<ImagePixel> {

  public AntForImageThresholding(int solutionLenght) {
    this.setSolution(new ImagePixel[solutionLenght]);
  }

  @Override
  public List<ImagePixel> getNeighbourhood(Environment environment) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Double getPheromoneTrailValue(ImagePixel solutionComponent,
      Integer positionInSolution, Environment environment) {
    // TODO Auto-generated method stub
    return 0.0;
  }

  @Override
  public void setPheromoneTrailValue(ImagePixel solutionComponent,
      Environment environment, Double value) {
    // TODO Auto-generated method stub

  }

  @Override
  public double getSolutionQuality(Environment environment) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isSolutionReady(Environment env) {
    EnvironmentForImageThresholding environment = (EnvironmentForImageThresholding) env;
    return getCurrentIndex() == environment.getNumberOfSteps();
  }

  @Override
  public Double getHeuristicValue(ImagePixel solutionComponent,
      Integer positionInSolution, Environment environment) {
    // TODO Auto-generated method stub
    return null;
  }

}
