package pe.edu.pucp.acosthres.image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pe.edu.pucp.acothres.ProblemConfiguration;

public class ImageFileHelper {

	public static int[][] getImageArrayFromFile(String imageFile)
			throws IOException {

		BufferedImage image = ImageIO.read(new File(imageFile));
		Raster imageRaster = image.getData();

		int[][] imageAsArray;
		int[] pixel = new int[1];
		int[] buffer = new int[1];

		imageAsArray = new int[imageRaster.getWidth()][imageRaster.getHeight()];

		for (int i = 0; i < imageRaster.getWidth(); i++)
			for (int j = 0; j < imageRaster.getHeight(); j++) {
				pixel = imageRaster.getPixel(i, j, buffer);
				imageAsArray[i][j] = pixel[0];
			}
		return imageAsArray;
	}

	public static void generateImageFromArray(int[][] imageGraph,
			String outputImageFile) throws IOException {
		System.out.println("Generating output image");
		BufferedImage outputImage = new BufferedImage(imageGraph.length,
				imageGraph[0].length, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = outputImage.getRaster();
		for (int x = 0; x < imageGraph.length; x++) {
			for (int y = 0; y < imageGraph[x].length; y++) {
				if (imageGraph[x][y] != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
					raster.setSample(x, y, 0, imageGraph[x][y]);
				} else {
					raster.setSample(x, y, 0,
							ProblemConfiguration.GRAYSCALE_MIN_RANGE);
				}
			}
		}
		File imageFile = new File(outputImageFile);
		ImageIO.write(outputImage, "bmp", imageFile);
		System.out.println("Resulting image stored in: " + outputImageFile);
	}

	public static int[][] openImage(int[][] imageGraph, int repetitionParameter) {
		int[][] resultImage = imageGraph;

		for (int i = 0; i < repetitionParameter; i++) {
			resultImage = erodeImage(resultImage,
					ProblemConfiguration.DEFAULT_STRUCTURING_ELEMENT,
					ProblemConfiguration.GRAYSCALE_MAX_RANGE,
					ProblemConfiguration.GRAYSCALE_MAX_RANGE / 2);
		}

		for (int i = 0; i < repetitionParameter; i++) {
			resultImage = dilateImage(resultImage,
					ProblemConfiguration.DEFAULT_STRUCTURING_ELEMENT,
					ProblemConfiguration.GRAYSCALE_MAX_RANGE,
					ProblemConfiguration.GRAYSCALE_MAX_RANGE / 2);
		}

		return resultImage;
	}

	public static int[][] removeBackgroundPixels(int[][] imageGraph) {
		int[][] result = new int[imageGraph.length][imageGraph[0].length];
		for (int i = 0; i < imageGraph.length; i++) {
			for (int j = 0; j < imageGraph[0].length; j++) {
				if (Math.abs(imageGraph[i][j]
						- ProblemConfiguration.GRAYSCALE_MIN_RANGE) < ProblemConfiguration.GRAYSCALE_DELTA) {
					result[i][j] = ProblemConfiguration.ABSENT_PIXEL_FLAG;
				} else {
					result[i][j] = imageGraph[i][j];
				}
			}
		}
		return result;
	}

	public static int[][] erodeImage(int[][] imageGraph,
			int[][] structuringElement, int foregroundClass, int backgroundClass) {
		int[][] resultImage = new int[imageGraph.length][imageGraph[0].length];
		for (int i = 0; i < imageGraph.length; i++) {
			for (int j = 0; j < imageGraph[0].length; j++) {
				if (imageGraph[i][j] != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
					if (isFit(i, j, imageGraph, structuringElement,
							foregroundClass)) {
						resultImage[i][j] = foregroundClass;
					} else {
						resultImage[i][j] = backgroundClass;
					}
				} else {
					resultImage[i][j] = ProblemConfiguration.ABSENT_PIXEL_CLUSTER;
				}

			}
		}

		return resultImage;
	}

	public static int[][] dilateImage(int[][] imageGraph,
			int[][] structuringElement, int foregroundClass, int backgroundClass) {
		int[][] resultImage = new int[imageGraph.length][imageGraph[0].length];
		for (int i = 0; i < imageGraph.length; i++) {
			for (int j = 0; j < imageGraph[0].length; j++) {
				if (imageGraph[i][j] != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
					if (isHit(i, j, imageGraph, structuringElement,
							foregroundClass)) {
						resultImage[i][j] = foregroundClass;
					} else {
						resultImage[i][j] = backgroundClass;
					}
				} else {
					resultImage[i][j] = ProblemConfiguration.ABSENT_PIXEL_CLUSTER;
				}
			}

		}
		return resultImage;
	}

	public static boolean isHit(int i, int j, int[][] imageGraph,
			int[][] structuringElement, int foregroundClass) {
		int rowCounter = i - structuringElement.length / 2;
		int columnCounter = j - structuringElement[0].length / 2;
		int initialColumnValue = columnCounter;
		for (int k = 0; k < structuringElement.length; k++) {
			columnCounter = initialColumnValue;
			for (int l = 0; l < structuringElement[0].length; l++) {

				if (rowCounter >= 0 && rowCounter < imageGraph.length
						&& columnCounter >= 0
						&& columnCounter < imageGraph[0].length) {
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

	public static boolean isFit(int i, int j, int[][] imageGraph,
			int[][] structuringElement, int foregroundClass) {
		int rowCounter = i - structuringElement.length / 2;
		int columnCounter = j - structuringElement[0].length / 2;
		int initialColumnValue = columnCounter;
		for (int k = 0; k < structuringElement.length; k++) {
			columnCounter = initialColumnValue;
			for (int l = 0; l < structuringElement[0].length; l++) {
				if (rowCounter >= 0 && rowCounter < imageGraph.length
						&& columnCounter >= 0
						&& columnCounter < imageGraph[0].length) {
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
