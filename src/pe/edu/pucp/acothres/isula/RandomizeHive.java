package pe.edu.pucp.acothres.isula;

import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;

import java.util.Collections;

import pe.edu.pucp.acosthres.image.ImagePixel;

public class RandomizeHive extends DaemonAction<ImagePixel> {

  public RandomizeHive() {
    super(DaemonActionType.INITIAL_CONFIGURATION);
  }

  @Override
  public void applyDaemonAction(ConfigurationProvider configurationProvider) {
    Collections.shuffle(getAntColony().getHive());
  }

}
