package pe.edu.pucp.acothres.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pe.edu.pucp.acosthres.image.ImagePixel;
import pe.edu.pucp.acosthres.image.PosiblePixel;
import pe.edu.pucp.acothres.ProblemConfiguration;

public class Ant {

	private int currentIndex = 0;
	private ImagePixel pixelPath[];

	// TODO(cgavidia):Visited matrix was removed because memory concerns

	public Ant(int solutionLength, int numberOfRows, int numberOfColumns) {
		this.pixelPath = new ImagePixel[solutionLength];
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public void visitPixel(ImagePixel visitedPixel) {
		pixelPath[currentIndex] = visitedPixel;
		currentIndex++;
	}

	public void clear() {
		for (int i = 0; i < pixelPath.length; i++) {
			pixelPath[i] = null;
		}
	}

	public boolean isPixelVisited(ImagePixel imagePixel) {
		// TODO(cgavidia): Pass this to equals
		if (!ProblemConfiguration.ALLOW_VISITED_PIXELS) {
			for (ImagePixel visitedPixel : pixelPath) {
				if (visitedPixel != null
						&& visitedPixel.getxCoordinate() == imagePixel
								.getxCoordinate()
						&& visitedPixel.getyCoordinate() == imagePixel
								.getyCoordinate()
						&& visitedPixel.getGreyScaleValue() == imagePixel
								.getGreyScaleValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public ImagePixel[] getPixelPath() {
		return pixelPath;
	}

	public ImagePixel selectNextPixel(double[][] pheromoneTrails,
			int[][] imageGraph) {
		ImagePixel nextPixel = null;
		Random random = new Random();
		double randomValue = random.nextDouble();
		List<PosiblePixel> probabilities = getProbabilities(pheromoneTrails,
				imageGraph);

		// TODO(cgavidia): Calibrate this parameter
		if (randomValue < ProblemConfiguration.BEST_CHOICE_PROBABILITY) {
			double currentMaximumPheromoneTimesHeuristic = -1;
			for (PosiblePixel posiblePixel : probabilities) {
				if (!isPixelVisited(posiblePixel.getImagePixel())
						&& posiblePixel.getHeuristicTimesPheromone() > currentMaximumPheromoneTimesHeuristic) {
					currentMaximumPheromoneTimesHeuristic = posiblePixel
							.getHeuristicTimesPheromone();
					nextPixel = posiblePixel.getImagePixel();
				}
			}
			return nextPixel;
		} else {
			double anotherRandomValue = random.nextDouble();
			double total = 0;
			for (PosiblePixel posiblePixel : probabilities) {
				total = total + posiblePixel.getProbability();
				if (total >= anotherRandomValue) {
					return posiblePixel.getImagePixel();
				}

			}
		}
		return nextPixel;
	}

	private List<PosiblePixel> getProbabilities(double[][] pheromoneTrails,
			int[][] imageGraph) {
		List<PosiblePixel> pixelsWithProbabilities = new ArrayList<PosiblePixel>();
		ImagePixel currentPosition = pixelPath[currentIndex - 1];

		List<ImagePixel> neighbours = currentPosition
				.getNeighbourhood(imageGraph);
		double denominator = 0.0;
		for (ImagePixel neighbour : neighbours) {
			if (!isPixelVisited(neighbour)
					&& neighbour.getGreyScaleValue() != ProblemConfiguration.ABSENT_PIXEL_FLAG) {
				// We add a small number to avoid division by zero
				double heuristicValue = 1 / Math.abs(neighbour
						.getGreyScaleValue()
						- getMeanGrayScaleValue()
						+ ProblemConfiguration.DELTA);

				double pheromoneTrailValue = pheromoneTrails[neighbour
						.getxCoordinate()][neighbour.getyCoordinate()]
						+ ProblemConfiguration.DELTA;
				double heuristicTimesPheromone = Math.pow(heuristicValue,
						ProblemConfiguration.HEURISTIC_IMPORTANCE)
						* Math.pow(pheromoneTrailValue,
								ProblemConfiguration.PHEROMONE_IMPORTANCE);

				// Temporary, we're storing the product as probability.
				pixelsWithProbabilities.add(new PosiblePixel(neighbour,
						heuristicTimesPheromone, 0.0));
				denominator = denominator + heuristicTimesPheromone;
			}
		}

		// TODO(cgavidia): Remove if doesn't improve
		if (pixelsWithProbabilities.size() == 0) {
			ImagePixel imagePixel = neighbours.get(new Random()
					.nextInt(neighbours.size()));
			pixelsWithProbabilities.add(new PosiblePixel(imagePixel, 1.0, 1.0));
		}

		for (PosiblePixel posiblePixel : pixelsWithProbabilities) {
			double heuristicTimesPheromone = posiblePixel
					.getHeuristicTimesPheromone();
			// Now we're dividing by the total sum
			posiblePixel.setProbability(heuristicTimesPheromone / denominator);
		}

		return pixelsWithProbabilities;
	}

	public double getMeanGrayScaleValue() {
		double grayScaleSum = 0.0;
		for (int i = 0; i < currentIndex; i++) {
			ImagePixel currentPixel = pixelPath[i];
			grayScaleSum = grayScaleSum + currentPixel.getGreyScaleValue();
		}
		return grayScaleSum / (currentIndex);
	}

	public String pathAsString() {
		String result = "";
		for (ImagePixel pixel : pixelPath) {
			if (pixel != null) {
				result = result + "(" + pixel.getxCoordinate() + ", "
						+ pixel.getyCoordinate() + ")  ";
			}
		}
		return result;
	}
}
