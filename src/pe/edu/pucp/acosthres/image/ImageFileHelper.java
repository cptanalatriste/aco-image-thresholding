package pe.edu.pucp.acosthres.image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageFileHelper {

  private static Logger logger = Logger.getLogger(ImageFileHelper.class
      .getName());

  public static final int ABSENT_PIXEL_FLAG = -1;
  public static final int GRAYSCALE_MIN_RANGE = 0;
  public static final int GRAYSCALE_MAX_RANGE = 255;

  private static final int ABSENT_PIXEL_CLUSTER = -1;
  private static final int[][] DEFAULT_STRUCTURING_ELEMENT = {
      { 255, 255, 255 }, { 255, 255, 255 }, { 255, 255, 255 } };
  private static final int GRAYSCALE_DELTA = 10;

  /**
   * Transforms an image into an array of ints.
   * 
   * @param imageFile
   *          Image file.
   * @return An array of ints.
   * @throws IOException
   *           In case file reading fails.
   */
  public static int[][] getImageArrayFromFile(String imageFile)
      throws IOException {

    BufferedImage image = ImageIO.read(new File(imageFile));
    Raster imageRaster = image.getData();

    int[][] imageAsArray;
    int[] pixel = new int[1];
    int[] buffer = new int[1];

    imageAsArray = new int[imageRaster.getWidth()][imageRaster.getHeight()];

    for (int i = 0; i < imageRaster.getWidth(); i++) {
      for (int j = 0; j < imageRaster.getHeight(); j++) {
        pixel = imageRaster.getPixel(i, j, buffer);
        imageAsArray[i][j] = pixel[0];
      }
    }
    return imageAsArray;
  }

  /**
   * Generates an image file from an integer array.
   * 
   * @param imageGraph
   *          Array of ints representing the image.
   * @param outputImageFile
   *          Location of the generated file.
   * @throws IOException
   *           If file writing/reading fails.
   */
  public static void generateImageFromArray(int[][] imageGraph,
      String outputImageFile) throws IOException {
    logger.info("Generating output image");
    BufferedImage outputImage = new BufferedImage(imageGraph.length,
        imageGraph[0].length, BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster raster = outputImage.getRaster();
    for (int x = 0; x < imageGraph.length; x++) {
      for (int y = 0; y < imageGraph[x].length; y++) {
        if (imageGraph[x][y] != ABSENT_PIXEL_CLUSTER) {
          raster.setSample(x, y, 0, imageGraph[x][y]);
        } else {
          raster.setSample(x, y, 0, GRAYSCALE_MIN_RANGE);
        }
      }
    }
    File imageFile = new File(outputImageFile);
    ImageIO.write(outputImage, "bmp", imageFile);
    logger.info("Resulting image stored in: " + outputImageFile);
  }

  /**
   * Applies the open operator of mathematical morphology to an image.
   * 
   * @param imageGraph
   *          Image as an array on ints.
   * @param repetitionParameter
   *          Number of repetitions.
   * @return A new image after the application of the operator.
   */
  public static int[][] openImage(int[][] imageGraph, int repetitionParameter) {
    int[][] resultImage = imageGraph;

    for (int i = 0; i < repetitionParameter; i++) {
      resultImage = erodeImage(resultImage, DEFAULT_STRUCTURING_ELEMENT,
          GRAYSCALE_MAX_RANGE, GRAYSCALE_MAX_RANGE / 2);
    }

    for (int i = 0; i < repetitionParameter; i++) {
      resultImage = dilateImage(resultImage, DEFAULT_STRUCTURING_ELEMENT,
          GRAYSCALE_MAX_RANGE, GRAYSCALE_MAX_RANGE / 2);
    }

    return resultImage;
  }

  /**
   * Marks black pixels as background ones.
   * 
   * @param imageGraph
   *          Image as an array of ints.
   * @return A new image, with black pixels marked as missing.
   */
  public static int[][] removeBackgroundPixels(int[][] imageGraph) {
    int[][] result = new int[imageGraph.length][imageGraph[0].length];
    for (int i = 0; i < imageGraph.length; i++) {
      for (int j = 0; j < imageGraph[0].length; j++) {
        if (Math.abs(imageGraph[i][j] - GRAYSCALE_MIN_RANGE) < GRAYSCALE_DELTA) {
          result[i][j] = ABSENT_PIXEL_FLAG;
        } else {
          result[i][j] = imageGraph[i][j];
        }
      }
    }
    return result;
  }

  /**
   * Applies the erosion operator of mathematical morphology.
   * 
   * @param imageGraph
   *          Image represented as an array of integers.
   * @param structuringElement
   *          Structuring element.
   * @param foregroundClass
   *          Class for the foreground.
   * @param backgroundClass
   *          Class for the background.
   * @return A new image, after the application of the operator.
   */
  public static int[][] erodeImage(int[][] imageGraph,
      int[][] structuringElement, int foregroundClass, int backgroundClass) {
    int[][] resultImage = new int[imageGraph.length][imageGraph[0].length];
    for (int i = 0; i < imageGraph.length; i++) {
      for (int j = 0; j < imageGraph[0].length; j++) {
        if (imageGraph[i][j] != ABSENT_PIXEL_CLUSTER) {
          if (isFit(i, j, imageGraph, structuringElement, foregroundClass)) {
            resultImage[i][j] = foregroundClass;
          } else {
            resultImage[i][j] = backgroundClass;
          }
        } else {
          resultImage[i][j] = ABSENT_PIXEL_CLUSTER;
        }

      }
    }

    return resultImage;
  }

  /**
   * Applies the dilation operator of mathematical morphology.
   * 
   * @param imageGraph
   *          Image represented as an array of integers.
   * @param structuringElement
   *          Structuring element.
   * @param foregroundClass
   *          Class for the foreground.
   * @param backgroundClass
   *          Class for the background.
   * @return A new image, after the application of the operator.
   */
  public static int[][] dilateImage(int[][] imageGraph,
      int[][] structuringElement, int foregroundClass, int backgroundClass) {
    int[][] resultImage = new int[imageGraph.length][imageGraph[0].length];
    for (int i = 0; i < imageGraph.length; i++) {
      for (int j = 0; j < imageGraph[0].length; j++) {
        if (imageGraph[i][j] != ABSENT_PIXEL_CLUSTER) {
          if (isHit(i, j, imageGraph, structuringElement, foregroundClass)) {
            resultImage[i][j] = foregroundClass;
          } else {
            resultImage[i][j] = backgroundClass;
          }
        } else {
          resultImage[i][j] = ABSENT_PIXEL_CLUSTER;
        }
      }

    }
    return resultImage;
  }

  /**
   * Identifies a pixel as a hit.
   * 
   * @param row
   *          Row position
   * @param column
   *          Column position.
   * @param imageGraph
   *          Image as an array of integers.
   * @param structuringElement
   *          Structuring element.
   * @param foregroundClass
   *          Class of the foreground.
   * @return True if is a hit, false otherwise.
   */
  public static boolean isHit(int row, int column, int[][] imageGraph,
      int[][] structuringElement, int foregroundClass) {
    int rowCounter = row - structuringElement.length / 2;
    int columnCounter = column - structuringElement[0].length / 2;
    int initialColumnValue = columnCounter;
    for (int k = 0; k < structuringElement.length; k++) {
      columnCounter = initialColumnValue;
      for (int l = 0; l < structuringElement[0].length; l++) {

        if (rowCounter >= 0 && rowCounter < imageGraph.length
            && columnCounter >= 0 && columnCounter < imageGraph[0].length) {
          if (structuringElement[k][l] == foregroundClass
              && imageGraph[rowCounter][columnCounter] == foregroundClass) {
            return true;
          }
        }
        columnCounter++;
      }
      rowCounter++;
    }
    return false;

  }

  /**
   * Identifies a pixel as a fit.
   * 
   * @param row
   *          Row position
   * @param column
   *          Column position.
   * @param imageGraph
   *          Image as an array of integers.
   * @param structuringElement
   *          Structuring element.
   * @param foregroundClass
   *          Class of the foreground.
   * @return True if is a hit, false otherwise.
   */
  public static boolean isFit(int row, int column, int[][] imageGraph,
      int[][] structuringElement, int foregroundClass) {
    int rowCounter = row - structuringElement.length / 2;
    int columnCounter = column - structuringElement[0].length / 2;
    int initialColumnValue = columnCounter;
    for (int k = 0; k < structuringElement.length; k++) {
      columnCounter = initialColumnValue;
      for (int l = 0; l < structuringElement[0].length; l++) {
        if (rowCounter >= 0 && rowCounter < imageGraph.length
            && columnCounter >= 0 && columnCounter < imageGraph[0].length) {
          if (structuringElement[k][l] == foregroundClass
              && imageGraph[rowCounter][columnCounter] != foregroundClass) {
            return false;
          }
        }
        columnCounter++;
      }
      rowCounter++;
    }
    return true;
  }
}
