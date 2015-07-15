package pe.edu.pucp.acothres.isula;

import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.image.util.ImagePixel;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NodeSelectionForImageThresholding extends
    PseudoRandomNodeSelection<ImagePixel, EnvironmentForImageThresholding> {

  private static final double MAXIMUM_PROBABILITY = 1.0;

  @Override
  protected HashMap<ImagePixel, Double> doIfNoComponentsFound(
      EnvironmentForImageThresholding environment,
      AcsConfigurationProvider configurationProvider) {
    List<ImagePixel> neighbours = getAnt().getNeighbourhood(environment);
    ImagePixel imagePixel = neighbours.get(new Random().nextInt(neighbours
        .size()));

    HashMap<ImagePixel, Double> componentsWithProbabilities = new HashMap<ImagePixel, Double>();
    componentsWithProbabilities.put(imagePixel, MAXIMUM_PROBABILITY);

    return componentsWithProbabilities;
  }

}
