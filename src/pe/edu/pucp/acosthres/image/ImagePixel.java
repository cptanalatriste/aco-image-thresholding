package pe.edu.pucp.acosthres.image;

import java.util.ArrayList;
import java.util.List;

public class ImagePixel {

	private int xCoordinate;
	private int yCoordinate;
	private int greyScaleValue;

	public ImagePixel(int xCoordinate, int yCoordinate, int[][] imageGraph) {
		super();
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.greyScaleValue = imageGraph[xCoordinate][yCoordinate];
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getGreyScaleValue() {
		return greyScaleValue;
	}

	public void setGreyScaleValue(int greyScaleValue) {
		this.greyScaleValue = greyScaleValue;
	}

	// TODO(cgavidia): There must be a more elegant way to do this
	public List<ImagePixel> getNeighbourhood(int[][] imageGraph) {
		ArrayList<ImagePixel> neighbours = new ArrayList<ImagePixel>();
		if (yCoordinate - 1 >= 0) {
			neighbours.add(new ImagePixel(xCoordinate, yCoordinate - 1,
					imageGraph));
		}

		if (yCoordinate + 1 < imageGraph[0].length) {
			neighbours.add(new ImagePixel(xCoordinate, yCoordinate + 1,
					imageGraph));
		}

		if (xCoordinate - 1 >= 0) {
			neighbours.add(new ImagePixel(xCoordinate - 1, yCoordinate,
					imageGraph));
		}

		if (xCoordinate + 1 < imageGraph.length) {
			neighbours.add(new ImagePixel(xCoordinate + 1, yCoordinate,
					imageGraph));
		}

		if (xCoordinate - 1 >= 0 && yCoordinate - 1 >= 0) {
			neighbours.add(new ImagePixel(xCoordinate - 1, yCoordinate - 1,
					imageGraph));
		}

		if (xCoordinate + 1 < imageGraph.length && yCoordinate - 1 >= 0) {
			neighbours.add(new ImagePixel(xCoordinate + 1, yCoordinate - 1,
					imageGraph));
		}

		if (xCoordinate - 1 >= 0 && yCoordinate + 1 < imageGraph[0].length) {
			neighbours.add(new ImagePixel(xCoordinate - 1, yCoordinate + 1,
					imageGraph));
		}

		if (xCoordinate + 1 < imageGraph.length
				&& yCoordinate + 1 < imageGraph[0].length) {
			neighbours.add(new ImagePixel(xCoordinate + 1, yCoordinate + 1,
					imageGraph));
		}

		return neighbours;
	}
}
