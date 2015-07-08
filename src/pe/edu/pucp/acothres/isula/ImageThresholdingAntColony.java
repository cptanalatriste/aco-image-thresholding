package pe.edu.pucp.acothres.isula;

import isula.aco.Ant;
import isula.aco.AntColony;
import isula.aco.Environment;

import pe.edu.pucp.acosthres.image.ImageFileHelper;
import pe.edu.pucp.acosthres.image.ImagePixel;

import java.util.logging.Logger;

public class ImageThresholdingAntColony extends AntColony<ImagePixel> {

  private static Logger logger = Logger
      .getLogger(ImageThresholdingAntColony.class.getName());

  public ImageThresholdingAntColony() {
    // The number of ants is calculated later.
    super(0);
  }

  @Override
  public void buildColony(Environment environment) {
    int antCounter = 0;
    double[][] problemGraph = environment.getProblemGraph();

    for (int i = 0; i < problemGraph.length; i++) {
      for (int j = 0; j < problemGraph[0].length; j++) {

        if (problemGraph[i][j] != ImageFileHelper.ABSENT_PIXEL_FLAG) {
          Ant<ImagePixel> ant = this.createAnt(environment);
          ant.getSolution()[0] = new ImagePixel(i, j,
              environment.getProblemGraph());
          this.getHive().add(ant);

          antCounter += 1;
        }
      }
    }

    this.setNumberOfAnts(antCounter);
  }

  @Override
  protected Ant<ImagePixel> createAnt(Environment environment) {
    EnvironmentForImageThresholding env = (EnvironmentForImageThresholding) environment;
    return new AntForImageThresholding(env.getNumberOfSteps());
  }

  @Override
  public void clearAntSolutions() {
    logger.info("CLEARING ANT SOLUTIONS");

    for (Ant<ImagePixel> ant : getHive()) {
      ImagePixel initialPixel = ant.getSolution()[0];
      ant.clear();
      ant.setCurrentIndex(0);
      ant.visitNode(initialPixel);
    }
  }

}
