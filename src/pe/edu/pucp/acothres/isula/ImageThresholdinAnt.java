package pe.edu.pucp.acothres.isula;

import isula.aco.Ant;
import isula.aco.Environment;

public class ImageThresholdinAnt extends Ant {

  private int numberOfSteps;

  @Override
  public double getSolutionQuality(Environment environment) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isSolutionReady(Environment environment) {
    // TODO Auto-generated method stub
    return false;
  }

  public int getNumberOfSteps() {
    return numberOfSteps;
  }

  public void setNumberOfSteps(int numberOfSteps) {
    this.numberOfSteps = numberOfSteps;
  }

}
