package pe.edu.pucp.acothres.isula;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;
import isula.image.util.ImagePixel;
import pe.edu.pucp.acothres.ProblemConfiguration;

public class OnlinePheromoneUpdateForThresholding extends
    OnlinePheromoneUpdate<ImagePixel, EnvironmentForImageThresholding> {

  @Override
  protected double getNewPheromoneValue(ImagePixel solutionComponent,
	  Integer positionInSolution,
      EnvironmentForImageThresholding environment,
      ConfigurationProvider configurationProvider) {

    AntForImageThresholding ant = (AntForImageThresholding) getAnt();

    double contribution = 1 
        / (ProblemConfiguration.COST_FUNCTION_PARAMETER_A 
            + ProblemConfiguration.COST_FUNCTION_PARAMETER_B
        * ant.getSolutionCost(environment));

    double newValue = ant.getPheromoneTrailValue(solutionComponent,
        ant.getCurrentIndex(), environment)
        * ProblemConfiguration.EXTRA_WEIGHT + contribution;
    return newValue;
  }
}
