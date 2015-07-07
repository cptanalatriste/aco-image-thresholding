package pe.edu.pucp.acothres;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.AntColony;
import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.InvalidInputException;

import pe.edu.pucp.acosthres.image.ImageFileHelper;
import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acothres.cluster.KmeansClassifier;
import pe.edu.pucp.acothres.exper.TestSuite;
import pe.edu.pucp.acothres.isula.EnvironmentForImageThresholding;
import pe.edu.pucp.acothres.isula.ImageThresholdingAntColony;
import pe.edu.pucp.acothres.isula.OnlinePheromoneUpdateForThresholding;
import pe.edu.pucp.acothres.isula.RandomizeHive;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AcoImageThresholding {

  public static int[][] getSegmentedImageAsArray(String imageFile,
      boolean generateOutputFiles) throws Exception {
    System.out.println("ACO FOR IMAGE THRESHOLDING");
    System.out.println("=============================");

    System.out.println("Data file: " + imageFile);

    int[][] imageGraph = getImageGraph(imageFile);

    EnvironmentForImageThresholding environment = applySegmentationWithIsula(imageGraph);

    System.out.println("Starting K-means clustering");

    // TODO(cgavidia): There should a method to get the number of
    // clústers automatically. Also, son preprocessing or postprocessing
    // would improve quality.
    KmeansClassifier classifier = new KmeansClassifier(environment,
        ProblemConfiguration.NUMBER_OF_CLUSTERS);

    int[][] segmentedImageAsMatrix = applyPostProcessingSteps(classifier);

    if (generateOutputFiles) {
      System.out.println("Generating segmented image");
      ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
          ProblemConfiguration.OUTPUT_DIRECTORY
              + ProblemConfiguration.OUTPUT_IMAGE_FILE);

      System.out.println("Generating images per cluster");
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
      final int[][] imageGraphAsInt) throws InvalidInputException {

    // TODO(cgavidia): Simple hack to support the type. It should be a generic
    // instead.
    final double[][] imageGraph = new double[imageGraphAsInt.length][imageGraphAsInt[0].length];
    for (int i = 0; i < imageGraphAsInt.length; i++) {
      for (int j = 0; i < imageGraphAsInt[0].length; i++) {
        imageGraph[i][j] = imageGraphAsInt[i][j];
      }
    }

    // TODO(cgavidia): We need to find a way to force to implement this types.
    // Maybe as constructor arguments.
    ConfigurationProvider configurationProvider = new ProblemConfiguration();
    AcoProblemSolver<ImagePixel> problemSolver = new AcoProblemSolver<ImagePixel>();

    EnvironmentForImageThresholding environment = new EnvironmentForImageThresholding(
        imageGraph, ProblemConfiguration.NUMBER_OF_STEPS);

    AntColony<ImagePixel> antColony = new ImageThresholdingAntColony();

    problemSolver.setConfigurationProvider(configurationProvider);
    problemSolver.setEnvironment(environment);
    problemSolver.setAntColony(antColony);

    problemSolver.addDaemonAction(new StartPheromoneMatrix<ImagePixel>());
    problemSolver.addDaemonAction(new RandomizeHive());
    problemSolver.addDaemonAction(new PerformEvaporation<ImagePixel>());
    // TODO(cgavidia): The normalization of the pheromone on the Matrix is still
    // pending.

    List<Ant<ImagePixel>> hive = problemSolver.getAntColony().getHive();
    for (Ant<ImagePixel> ant : hive) {
      ant.addPolicy(new PseudoRandomNodeSelection<ImagePixel>());
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

    System.out.println("Finishing computation at: " + new Date());
    return segmentedImageAsMatrix;
  }

  private static int[][] getImageGraph(String imageFile) throws IOException {
    int[][] imageGraph = ImageFileHelper.getImageArrayFromFile(imageFile);

    System.out.println("Generating original image from matrix");
    ImageFileHelper.generateImageFromArray(imageGraph,
        ProblemConfiguration.OUTPUT_DIRECTORY
            + ProblemConfiguration.ORIGINAL_IMAGE_FILE);
    System.out.println("Starting background filtering process");
    imageGraph = ImageFileHelper.removeBackgroundPixels(imageGraph);
    return imageGraph;
  }

  /**
   * @param args
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

}
