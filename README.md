# aco-image-thresholding
A Java Program that applies a binary thresholding procedure to a MR Brain image. The proposed procedure includes simple thresholding to remove the background, an Ant Colony Algorithm to separate foreground from background and mathematical morphology to improve the produced segments.

The Ant-Colony Algorithm
========================
The Ant Colony algorithm contained in the procedure is Ant System, as was proposed by Alice R. Malisia in the paper "Applying ant colony optimization to binary thresholding". To implement it, we used the Isula Framework:


```java
    ConfigurationProvider configurationProvider = new ProblemConfiguration();
    AcoProblemSolver<ImagePixel> problemSolver = new AcoProblemSolver<ImagePixel>();

    EnvironmentForImageThresholding environment = new EnvironmentForImageThresholding(
        imageGraph, ProblemConfiguration.NUMBER_OF_STEPS);

    ImageThresholdingAntColony antColony = new ImageThresholdingAntColony();
    antColony.buildColony(environment);

    problemSolver.setConfigurationProvider(configurationProvider);
    problemSolver.setEnvironment(environment);
    problemSolver.setAntColony(antColony);

    problemSolver.addDaemonActions(new StartPheromoneMatrix<ImagePixel>(),
        new RandomizeHive(), new PerformEvaporation<ImagePixel>());
    antColony.addAntPolicies(new NodeSelectionForImageThresholding(),
        new OnlinePheromoneUpdateForThresholding());

    problemSolver.solveProblem();

```
