package pe.edu.pucp.acothres.isula;

import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;
import isula.image.util.ImagePixel;

import java.util.Collections;

public class RandomizeHive extends
    DaemonAction<ImagePixel, EnvironmentForImageThresholding> {

  public RandomizeHive() {
    super(DaemonActionType.INITIAL_CONFIGURATION);
  }

  @Override
  public void applyDaemonAction(ConfigurationProvider configurationProvider) {
    Collections.shuffle(getAntColony().getHive());
  }

}
