package pe.edu.pucp.acothres.exper;

import isula.image.util.ImageComparator;
import pe.edu.pucp.acothres.ProblemConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestSuite {

  private static Logger logger = Logger.getLogger(TestSuite.class.getName());
  private static final int GREYSCALE_POSITIVE_THRESHOLD = 20;
  private List<ImageComparator> comparisonList = new ArrayList<ImageComparator>();

  /**
   * Generates an instance of the report tester.
   */
  public TestSuite() {
    comparisonList.add(new ImageComparator("CSF",
        ProblemConfiguration.INPUT_DIRECTORY + "csf_21130transverse1_64.gif",
        GREYSCALE_POSITIVE_THRESHOLD));
    comparisonList.add(new ImageComparator("Grey Matter",
        ProblemConfiguration.INPUT_DIRECTORY + "grey_20342transverse1_64.gif",
        GREYSCALE_POSITIVE_THRESHOLD));
    comparisonList.add(new ImageComparator("White Matter",
        ProblemConfiguration.INPUT_DIRECTORY + "white_20358transverse1_64.gif",
        GREYSCALE_POSITIVE_THRESHOLD));
  }

  /**
   * Executes the test.
   * 
   * @throws Exception
   *           In case something fails.
   */
  public void executeReport() throws Exception {

    // TODO(cgavidia): It would be good to evaluate the behaviuor with
    // noise.
    logger.info("\n\nEXPERIMENT EXECUTION REPORT");
    logger.info("===============================");

    for (ImageComparator comparator : comparisonList) {
      double maximumBdp = 0;
      String maximumBdpClusterFile = "";
      for (int i = 0; i < ProblemConfiguration.NUMBER_OF_CLUSTERS; i++) {
        String currentFile = ProblemConfiguration.OUTPUT_DIRECTORY + i + "_"
            + ProblemConfiguration.CLUSTER_IMAGE_FILE;
        comparator.setImageToValidateFile(ProblemConfiguration.OUTPUT_DIRECTORY
            + i + "_" + ProblemConfiguration.CLUSTER_IMAGE_FILE);
        comparator.executeComparison();
        if (comparator.getBuildingDetectionPercentage() > maximumBdp) {
          maximumBdp = comparator.getBuildingDetectionPercentage();
          maximumBdpClusterFile = currentFile;
        }

      }
      comparator.setImageToValidateFile(maximumBdpClusterFile);
    }

    logger.info(ProblemConfiguration.currentConfigurationAsString());
    for (ImageComparator comparator : comparisonList) {
      comparator.executeComparison();
      logger.info(comparator.resultAsString());
    }

  }
}
