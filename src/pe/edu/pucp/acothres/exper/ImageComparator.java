package pe.edu.pucp.acothres.exper;

import pe.edu.pucp.acosthres.image.ImageFileHelper;

public class ImageComparator {

	private static final int GREYSCALE_POSITIVE_THRESHOLD = 20;

	private String description;

	private double truePositives = 0;
	private double falsePositives = 0;
	private double falseNegatives = 0;
	private double trueNegatives = 0;
	private double intersectingPixels = 0;
	private double unionedPixels = 0;

	private String referenceImageFile;
	private String imageToValidateFile;

	public ImageComparator(String description, String referenceImageFile) {
		this.description = description;
		this.referenceImageFile = referenceImageFile;
	}

	public String getReferenceImageFile() {
		return referenceImageFile;
	}

	public void executeComparison() throws Exception {
		truePositives = 0;
		falsePositives = 0;
		falsePositives = 0;
		intersectingPixels = 0;
		unionedPixels = 0;
		trueNegatives = 0;

		int[][] referenceImage = ImageFileHelper
				.getImageArrayFromFile(referenceImageFile);
		int[][] imageToValidate = ImageFileHelper
				.getImageArrayFromFile(imageToValidateFile);
		if (referenceImage.length != imageToValidate.length
				|| referenceImage[0].length != imageToValidate[0].length) {
			throw new Exception("Images are not comparable");
		}

		for (int i = 0; i < referenceImage.length; i++) {
			for (int j = 0; j < referenceImage[0].length; j++) {

				if (referenceImage[i][j] >= GREYSCALE_POSITIVE_THRESHOLD
						&& imageToValidate[i][j] >= GREYSCALE_POSITIVE_THRESHOLD) {
					truePositives++;
					intersectingPixels++;
					unionedPixels++;
				} else if (referenceImage[i][j] >= GREYSCALE_POSITIVE_THRESHOLD
						&& imageToValidate[i][j] < GREYSCALE_POSITIVE_THRESHOLD) {
					falseNegatives++;
					unionedPixels++;
				} else if (referenceImage[i][j] < GREYSCALE_POSITIVE_THRESHOLD
						&& imageToValidate[i][j] >= GREYSCALE_POSITIVE_THRESHOLD) {
					falsePositives++;
					unionedPixels++;
				} else if (referenceImage[i][j] < GREYSCALE_POSITIVE_THRESHOLD
						&& imageToValidate[i][j] < GREYSCALE_POSITIVE_THRESHOLD) {
					trueNegatives++;
				}
			}
		}
	}

	public void setImageToValidateFile(String imageToValidateFile) {
		this.imageToValidateFile = imageToValidateFile;
	}

	public double getBuildingDetectionPercentage() {
		return truePositives / (truePositives + falseNegatives);
	}

	public double getBranchingFactor() {
		return falsePositives / truePositives;
	}

	public double getJaccardSimilarityIndex() {
		return intersectingPixels / unionedPixels;
	}

	public double getFalsePositiveRate() {
		return falsePositives / (falsePositives + trueNegatives);
	}

	public double getFalseNegativeRate() {
		return falseNegatives / (falseNegatives + truePositives);
	}

	public String resultAsString() {
		String result = description + ": BDP = "
				+ getBuildingDetectionPercentage() + " BF = "
				+ getBranchingFactor() + " JSI = "
				+ getJaccardSimilarityIndex() + " FPR = "
				+ getFalsePositiveRate() + " FNR = " + getFalseNegativeRate()
				+ "\n";
		result += "Reference File ->" + referenceImageFile + "\n";
		result += "Generated File ->" + imageToValidateFile + "\n\n";

		return result;
	}

}
