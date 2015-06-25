package pe.edu.pucp.acothres.isula;

import isula.aco.Ant;
import isula.aco.AntColony;

public class ImageThresholdingAntColony extends AntColony {

  private int numberOfSteps;

  public ImageThresholdingAntColony(
      ImageThresholdingConfigurationProvider provider) {
    super(provider.getNumberOfAnts());
    this.numberOfSteps = provider.getNumberOfSteps();
  }

  @Override
  public Ant createAnt() {
    ImageThresholdinAnt ant = new ImageThresholdinAnt();
    ant.setNumberOfSteps(numberOfSteps);

    return ant;
  }

}
