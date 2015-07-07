package pe.edu.pucp.acosthres.image;

public class PosiblePixel {
  private ImagePixel imagePixel;
  private double probability;
  private double heuristicTimesPheromone;

  public PosiblePixel(ImagePixel imagePixel, double heuristicTimesPheromone,
      double probability) {
    super();
    this.imagePixel = imagePixel;
    this.heuristicTimesPheromone = heuristicTimesPheromone;
    this.probability = probability;
  }

  public double getHeuristicTimesPheromone() {
    return heuristicTimesPheromone;
  }

  public void setHeuristicTimesPheromone(double heuristicTimesPheromone) {
    this.heuristicTimesPheromone = heuristicTimesPheromone;
  }

  public ImagePixel getImagePixel() {
    return imagePixel;
  }

  public void setImagePixel(ImagePixel imagePixel) {
    this.imagePixel = imagePixel;
  }

  public double getProbability() {
    return probability;
  }

  public void setProbability(double probability) {
    this.probability = probability;
  }

}
