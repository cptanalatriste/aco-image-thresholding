package pe.edu.pucp.acothres;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.AntColony;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.InvalidInputException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import pe.edu.pucp.acosthres.image.ImageFileHelper;
import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acothres.ant.LegacyAntColony;
import pe.edu.pucp.acothres.ant.LegacyEnvironment;
import pe.edu.pucp.acothres.cluster.KmeansClassifier;
import pe.edu.pucp.acothres.exper.TestSuite;
import pe.edu.pucp.acothres.isula.AntForImageThresholding;
import pe.edu.pucp.acothres.isula.EnvironmentForImageThresholding;
import pe.edu.pucp.acothres.isula.RandomizeHive;

public class AcoImageThresholding {

  private LegacyEnvironment environment;
  private LegacyAntColony antColony;

  public AcoImageThresholding(LegacyEnvironment environment) {
    this.environment = environment;
    this.antColony = new LegacyAntColony(environment,
        ProblemConfiguration.NUMBER_OF_STEPS);
  }

  private void solveProblem() throws Exception {
    this.environment.initializePheromoneMatrix();
    int iteration = 0;
    System.out.println("STARTING ITERATIONS");
    System.out.println("Number of iterations: "
        + ProblemConfiguration.MAX_ITERATIONS);
    while (iteration < ProblemConfiguration.MAX_ITERATIONS) {
      System.out.println("Current iteration: " + iteration);
      this.antColony.clearAntSolutions();
      this.antColony
          .buildSolutions(ProblemConfiguration.DEPOSITE_PHEROMONE_ONLINE);
      System.out.println("UPDATING PHEROMONE TRAILS");
      if (!ProblemConfiguration.DEPOSITE_PHEROMONE_ONLINE) {
        this.antColony.depositPheromone();
      }
      this.environment.performEvaporation();
      iteration++;
    }
    System.out.println("EXECUTION FINISHED");
    // TODO(cgavidia): We're not storing best tour or it's lenght
  }

  public static int[][] getSegmentedImageAsArray(String imageFile,
      boolean generateOutputFiles) throws Exception {
    System.out.println("ACO FOR IMAGE THRESHOLDING");
    System.out.println("=============================");

    System.out.println("Data file: " + imageFile);

    int[][] imageGraph = getImageGraph(imageFile);

    applySegmentationWithIsula(imageGraph);

    LegacyEnvironment environment = new LegacyEnvironment(imageGraph);
    AcoImageThresholding acoImageSegmentation = new AcoImageThresholding(
        environment);
    System.out.println("Starting computation at: " + new Date());
    long startTime = System.nanoTime();

    acoImageSegmentation.solveProblem();

    System.out.println("Starting K-means clustering");

    // TODO(cgavidia): There should a method to get the number of
    // clústers automatically. Also, son preprocessing or postprocessing
    // would improve quality.
    KmeansClassifier classifier = new KmeansClassifier(environment,
        ProblemConfiguration.NUMBER_OF_CLUSTERS);

    int[][] segmentedImageAsMatrix = applyPostProcessingSteps(classifier,
        startTime);

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

  private static void applySegmentationWithIsula(final int[][] imageGraphAsInt)
      throws InvalidInputException {

    // TODO(cgavidia): Simple hack to support the type. It should be a generic
    // instead.
    double[][] imageGraph = new double[imageGraphAsInt.length][imageGraphAsInt[0].length];
    for (int i = 0; i < imageGraphAsInt.length; i++) {
      for (int j = 0; i < imageGraphAsInt[0].length; i++) {
        imageGraph[i][j] = imageGraphAsInt[i][j];
      }
    }

    // TODO(cgavidia): We need to find a way to force to implement this types.
    // Maybe as constructor arguments.
    ConfigurationProvider configurationProvider = new ProblemConfiguration();
    AcoProblemSolver<ImagePixel> problemSolver = new AcoProblemSolver<ImagePixel>();

    // TODO(cgavidia): This number is meaningless. The number of ants is
    // calculated later.
    int numberOfAnts = -1;

    Environment environment = new EnvironmentForImageThresholding(imageGraph,
        ProblemConfiguration.NUMBER_OF_STEPS);

    AntColony<ImagePixel> antColony = new AntColony<ImagePixel>(numberOfAnts) {

      @Override
      public void buildColony(Environment environment) {
        int antCounter = 0;
        double[][] problemGraph = environment.getProblemGraph();

        for (int i = 0; i < problemGraph.length; i++) {
          for (int j = 0; i < problemGraph[0].length; i++) {

            if (problemGraph[i][j] != ProblemConfiguration.ABSENT_PIXEL_FLAG) {
              Ant<ImagePixel> ant = this.createAnt(environment);
              ant.getSolution()[0] = new ImagePixel(i, j, imageGraphAsInt);
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
    };

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
      // TODO(cgavidia): Here goes the policy for the next node class.
      ant.addPolicy(null);
    }

    problemSolver.solveProblem();

  }

  private static int[][] applyPostProcessingSteps(KmeansClassifier classifier,
      long startTime) throws Exception {

    int[][] segmentedImageAsMatrix = classifier.generateSegmentedImage();

    // Only for demonstration purposes
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.OUTPUT_DIRECTORY + "without_open_process.bmp");

    segmentedImageAsMatrix = ImageFileHelper.openImage(segmentedImageAsMatrix,
        ProblemConfiguration.OPENING_REPETITION_PARAMETER);

    // Only for demonstration purposes
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.OUTPUT_DIRECTORY + "with_open_process.bmp");

    long endTime = System.nanoTime();
    System.out.println("Finishing computation at: " + new Date());
    System.out.println("Duration (in seconds): "
        + ((double) (endTime - startTime) / 1000000000.0));

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
