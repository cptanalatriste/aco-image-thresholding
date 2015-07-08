package pe.edu.pucp.acothres.isula;

import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;
import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acothres.ProblemConfiguration;

public class OnlinePheromoneUpdateForThresholding extends
    OnlinePheromoneUpdate<ImagePixel> {

  @Override
  protected double getNewPheromoneValue(ImagePixel solutionComponent,
      Environment environment, ConfigurationProvider configurationProvider) {

    AntForImageThresholding ant = (AntForImageThresholding) getAnt();

    double contribution = 1 
        / (ProblemConfiguration.COST_FUNCTION_PARAMETER_A 
            + ProblemConfiguration.COST_FUNCTION_PARAMETER_B
        * ant.getSolutionQuality(environment));

    double newValue = ant.getPheromoneTrailValue(solutionComponent,
        ant.getCurrentIndex(), environment)
        * ProblemConfiguration.EXTRA_WEIGHT + contribution;
    return newValue;
  }
}
