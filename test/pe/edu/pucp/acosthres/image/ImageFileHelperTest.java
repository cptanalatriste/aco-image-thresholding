package pe.edu.pucp.acosthres.image;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImageFileHelperTest {

  @Test
  public void testErode() {
    int[][] imageGraph = { { 0, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 1, 1, 1 },
        { 0, 0, 0, 0 } };
    int[][] structuringElement = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };

    int[][] expectedOutput = { { 0, 0, 1, 1 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 },
        { 0, 0, 0, 0 } };
    assertArrayEquals(expectedOutput,
        ImageFileHelper.erodeImage(imageGraph, structuringElement, 1, 0));
  }

  @Test
  public void testDilate() {
    int[][] imageGraph = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 1 },
        { 0, 0, 0, 0 } };
    int[][] structuringElement = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
    int[][] expectedOutput = { { 0, 0, 0, 0 }, { 0, 0, 1, 1 }, { 0, 0, 1, 1 },
        { 0, 0, 1, 1 } };
    ;
    assertArrayEquals(expectedOutput,
        ImageFileHelper.dilateImage(imageGraph, structuringElement, 1, 0));
  }

  @Test
  public void testIsFitForTrue() {
    int[][] imageGraph = { { 0, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 1, 1, 1 },
        { 0, 0, 0, 0 } };
    int[][] structuringElement = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
    boolean isFit = ImageFileHelper.isFit(1, 2, imageGraph, structuringElement,
        1);
    assertTrue(isFit);
    isFit = ImageFileHelper.isFit(0, 3, imageGraph, structuringElement, 1);
    assertTrue(isFit);
  }

  @Test
  public void testIsHitForTrue() {
    int[][] imageGraph = { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 0, 1 },
        { 0, 0, 0, 0 } };
    int[][] structuringElement = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
    boolean isHit = ImageFileHelper.isHit(1, 2, imageGraph, structuringElement,
        1);
    assertTrue(isHit);

    isHit = ImageFileHelper.isHit(0, 3, imageGraph, structuringElement, 1);
    assertTrue(isHit);
  }

  @Test
  public void testIsFitForFalse() {
    int[][] imageGraph = { { 0, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 1, 1, 0 },
        { 0, 0, 0, 0 } };
    int[][] structuringElement = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
    boolean isFit = ImageFileHelper.isFit(1, 2, imageGraph, structuringElement,
        1);
    assertFalse(isFit);

    isFit = ImageFileHelper.isFit(1, 3, imageGraph, structuringElement, 1);
    assertFalse(isFit);
  }

  @Test
  public void testIsHitForFalse() {
    int[][] imageGraph = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
        { 0, 0, 0, 0 } };
    int[][] structuringElement = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
    boolean isHit = ImageFileHelper.isHit(1, 2, imageGraph, structuringElement,
        1);
    assertFalse(isHit);

    isHit = ImageFileHelper.isHit(0, 3, imageGraph, structuringElement, 1);
    assertFalse(isHit);

  }
}
