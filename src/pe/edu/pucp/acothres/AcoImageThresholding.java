package pe.edu.pucp.acothres;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.InvalidInputException;

import pe.edu.pucp.acosthres.image.ImageFileHelper;
import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acothres.cluster.KmeansClassifier;
import pe.edu.pucp.acothres.exper.TestSuite;
import pe.edu.pucp.acothres.isula.EnvironmentForImageThresholding;
import pe.edu.pucp.acothres.isula.ImageThresholdingAntColony;
import pe.edu.pucp.acothres.isula.NodeSelectionForImageThresholding;
import pe.edu.pucp.acothres.isula.OnlinePheromoneUpdateForThresholding;
import pe.edu.pucp.acothres.isula.RandomizeHive;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;


public class AcoImageThresholding {

  private static Logger logger = Logger.getLogger(AcoImageThresholding.class
      .getName());

  /**
   * Starts the Image Thresholding process.
   * 
   * @param args
   *          Program arguments.
   */
  public static void main(String[] args) {
    try {
      String imageFile = ProblemConfiguration.INPUT_DIRECTORY
          + ProblemConfiguration.IMAGE_FILE;
      AcoImageThresholding.getSegmentedImageAsArray(imageFile, true);
      new TestSuite().executeReport();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Returns an image in segments, represented as an array of Integers.
   * 
   * @param imageFile
   *          Image file location.
   * @param generateOutputFiles
   *          True to generate output files.
   * @return Segmented image as an array.
   * @throws Exception
   *           IOException migh be thrown.
   */
  public static int[][] getSegmentedImageAsArray(String imageFile,
      boolean generateOutputFiles) throws Exception {
    logger.info("ACO FOR IMAGE THRESHOLDING");
    logger.info("=============================");

    logger.info("Data file: " + imageFile);

    double[][] imageGraph = getImageGraph(imageFile);

    EnvironmentForImageThresholding environment = applySegmentationWithIsula(imageGraph);

    logger.info("Starting K-means clustering");

    KmeansClassifier classifier = new KmeansClassifier(environment,
        ProblemConfiguration.NUMBER_OF_CLUSTERS);

    int[][] segmentedImageAsMatrix = applyPostProcessingSteps(classifier);

    if (generateOutputFiles) {
      logger.info("Generating segmented image");
      ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
          ProblemConfiguration.OUTPUT_DIRECTORY
              + ProblemConfiguration.OUTPUT_IMAGE_FILE);

      logger.info("Generating images per cluster");
      for (int i = 0; i < classifier.getNumberOfClusters(); i++) {
        ImageFileHelper.generateImageFromArray(
            classifier.generateSegmentedImagePerCluster(i),
            ProblemConfiguration.OUTPUT_DIRECTORY + i + "_"
                + ProblemConfiguration.CLUSTER_IMAGE_FILE);
      }
    }
    return segmentedImageAsMatrix;

  }

  private static EnvironmentForImageThresholding applySegmentationWithIsula(
      double[][] imageGraph) throws InvalidInputException,
      ConfigurationException {

    // TODO(cgavidia): We need to find a way to force to implement this types.
    // Maybe as constructor arguments.
    ConfigurationProvider configurationProvider = new ProblemConfiguration();
    AcoProblemSolver<ImagePixel> problemSolver = new AcoProblemSolver<ImagePixel>();

    EnvironmentForImageThresholding environment = new EnvironmentForImageThresholding(
        imageGraph, ProblemConfiguration.NUMBER_OF_STEPS);

    ImageThresholdingAntColony antColony = new ImageThresholdingAntColony();
    antColony.buildColony(environment);

    problemSolver.setConfigurationProvider(configurationProvider);
    problemSolver.setEnvironment(environment);
    problemSolver.setAntColony(antColony);

    problemSolver.addDaemonAction(new StartPheromoneMatrix<ImagePixel>());
    problemSolver.addDaemonAction(new RandomizeHive());
    problemSolver.addDaemonAction(new PerformEvaporation<ImagePixel>());

    List<Ant<ImagePixel>> hive = problemSolver.getAntColony().getHive();
    for (Ant<ImagePixel> ant : hive) {
      ant.addPolicy(new NodeSelectionForImageThresholding());
      ant.addPolicy(new OnlinePheromoneUpdateForThresholding());
    }

    problemSolver.solveProblem();
    return environment;
  }

  private static int[][] applyPostProcessingSteps(KmeansClassifier classifier)
      throws Exception {

    int[][] segmentedImageAsMatrix = classifier.generateSegmentedImage();

    // Only for demonstration purposes
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.OUTPUT_DIRECTORY + "without_open_process.bmp");

    segmentedImageAsMatrix = ImageFileHelper.openImage(segmentedImageAsMatrix,
        ProblemConfiguration.OPENING_REPETITION_PARAMETER);

    // Only for demonstration purposes
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.OUTPUT_DIRECTORY + "with_open_process.bmp");

    return segmentedImageAsMatrix;
  }

  private static double[][] getImageGraph(String imageFile) throws IOException {
    int[][] imageGraphAsInt = ImageFileHelper.getImageArrayFromFile(imageFile);

    logger.info("Generating original image from matrix");
    ImageFileHelper.generateImageFromArray(imageGraphAsInt,
        ProblemConfiguration.OUTPUT_DIRECTORY
            + ProblemConfiguration.ORIGINAL_IMAGE_FILE);
    logger.info("Starting background filtering process");
    imageGraphAsInt = ImageFileHelper.removeBackgroundPixels(imageGraphAsInt);

    // TODO(cgavidia): Simple hack to support the type. It should be a generic
    // instead.
    double[][] imageGraph = new double[imageGraphAsInt.length][imageGraphAsInt[0].length];
    for (int i = 0; i < imageGraphAsInt.length; i++) {
      for (int j = 0; j < imageGraphAsInt[0].length; j++) {
        imageGraph[i][j] = imageGraphAsInt[i][j];
      }
    }

    return imageGraph;
  }

}
