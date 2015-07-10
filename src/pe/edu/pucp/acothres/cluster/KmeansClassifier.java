package pe.edu.pucp.acothres.cluster;

import isula.aco.AcoProblemSolver;
import isula.image.util.ImageFileHelper;
import isula.image.util.ImagePixel;
import pe.edu.pucp.acothres.ProblemConfiguration;
import pe.edu.pucp.acothres.isula.EnvironmentForImageThresholding;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class KmeansClassifier {

  private static Logger logger = Logger.getLogger(AcoProblemSolver.class
      .getName());

  private static final int INITIAL_CAPACITY = 0;
  private static final String DATASET_NAME = "PHEROMONE_GRAYSCALE_INFO";
  private static final String GREYSCALE_VALUE_ATTRIBUTE = "greyscaleValue";
  private static final String PHEROMONE_VALUE_ATTRIBUTE = "pheromoneValue";

  private int numberOfClusters;
  private EnvironmentForImageThresholding environment;

  private List<ImagePixel> pixelPositions = new ArrayList<ImagePixel>();
  private double[] clusterAssignments;

  public KmeansClassifier(EnvironmentForImageThresholding environment,
      int numberOfClusters) {
    this.environment = environment;
    this.numberOfClusters = numberOfClusters;
  }

  public void doCluster() throws Exception {
    Instances instances = getInstancesFromMatrix();
    clusterAssignments = getClusterAssignments(instances);
  }

  /**
   * Generates a representation of the image containing the identified clusters.
   * 
   * @return Image represented as an integer array.
   * @throws Exception In case file reading/writing fails.
   */
  public int[][] generateSegmentedImage() throws Exception {
    if (clusterAssignments == null) {
      doCluster();
    }
    int[][] resultMatrix = new int[environment.getNumberOfRows()][environment
        .getNumberOfColumns()];

    for (int i = 0; i < environment.getNumberOfRows(); i++) {
      for (int j = 0; j < environment.getNumberOfColumns(); j++) {
        resultMatrix[i][j] = ProblemConfiguration.ABSENT_PIXEL_CLUSTER;
      }
    }

    int pixelCounter = 0;
    for (ImagePixel clusteredPixel : pixelPositions) {
      resultMatrix[clusteredPixel.getxCoordinate()][clusteredPixel
          .getyCoordinate()] = (int) ((clusterAssignments[pixelCounter] + 1)
          / numberOfClusters * ImageFileHelper.GRAYSCALE_MAX_RANGE);
      pixelCounter++;
    }
    return resultMatrix;
  }

  /**
   * Generates an image representing an specific cluster.
   * 
   * @param clusterNumber Cluster identifier.
   * @return Image represented as an integer array. 
   * @throws Exception Exception In case file reading/writing fails.
   */
  public int[][] generateSegmentedImagePerCluster(double clusterNumber)
      throws Exception {
    if (clusterAssignments == null) {
      doCluster();
    }
    int[][] resultMatrix = new int[environment.getNumberOfRows()][environment
        .getNumberOfColumns()];

    for (int i = 0; i < environment.getNumberOfRows(); i++) {
      for (int j = 0; j < environment.getNumberOfColumns(); j++) {
        resultMatrix[i][j] = ImageFileHelper.GRAYSCALE_MIN_RANGE;
      }
    }

    int pixelCounter = 0;
    for (ImagePixel clusteredPixel : pixelPositions) {
      if (clusterAssignments[pixelCounter] == clusterNumber) {
        resultMatrix[clusteredPixel.getxCoordinate()][clusteredPixel
            .getyCoordinate()] = ImageFileHelper.GRAYSCALE_MAX_RANGE / 2;
      }
      pixelCounter++;
    }

    return resultMatrix;
  }

  private double[] getClusterAssignments(Instances instances) throws Exception {
    // TODO(cgavidia): Maybe this can be optimized with more adequate
    // parameters.

    SimpleKMeans simpleKMeans = new SimpleKMeans();
    simpleKMeans.setNumClusters(numberOfClusters);
    simpleKMeans.buildClusterer(instances);

    ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
    clusterEvaluation.setClusterer(simpleKMeans);
    clusterEvaluation.evaluateClusterer(instances);
    double[] clusterAssignments = clusterEvaluation.getClusterAssignments();
    return clusterAssignments;
  }

  private Instances getInstancesFromMatrix() throws IOException {
    FastVector atributes = new FastVector();
    atributes.addElement(new Attribute(PHEROMONE_VALUE_ATTRIBUTE));
    atributes.addElement(new Attribute(GREYSCALE_VALUE_ATTRIBUTE));

    Instances instances = new Instances(DATASET_NAME, atributes,
        INITIAL_CAPACITY);

    int[][] normalizedPheromoneMatrix = environment
        .getNormalizedPheromoneMatrix(ImageFileHelper.GRAYSCALE_MAX_RANGE);
    logger.info("Generating pheromone distribution image");
    ImageFileHelper.generateImageFromArray(normalizedPheromoneMatrix,
        ProblemConfiguration.OUTPUT_DIRECTORY
            + ProblemConfiguration.PHEROMONE_IMAGE_FILE);

    int absentPixelCounter = 0;
    for (int i = 0; i < environment.getNumberOfRows(); i++) {
      for (int j = 0; j < environment.getNumberOfColumns(); j++) {
        if (environment.getProblemGraph()[i][j] != ImageFileHelper.ABSENT_PIXEL_FLAG) {
          Instance instance = new Instance(atributes.size());

          instance.setValue(0, normalizedPheromoneMatrix[i][j]);
          instance.setValue(1, environment.getProblemGraph()[i][j]);
          instances.add(instance);
          pixelPositions
              .add(new ImagePixel(i, j, environment.getProblemGraph()));
        } else {
          absentPixelCounter++;
        }
      }
    }
    logger.info("Abstent pixel counter: " + absentPixelCounter);
    return instances;
  }

  public int getNumberOfClusters() {
    return numberOfClusters;
  }

}
