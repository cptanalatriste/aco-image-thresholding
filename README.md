# aco-image-thresholding

[![Build Status](https://travis-ci.org/cptanalatriste/aco-image-thresholding.svg?branch=master)](https://travis-ci.org/cptanalatriste/aco-image-thresholding)
[![packagecloud](https://img.shields.io/badge/java-packagecloud.io-844fec.svg)](https://packagecloud.io/cgavidia/aco-image-thresholding)

A Java Program that applies a binary thresholding procedure to a MR Brain image. The proposed procedure includes simple thresholding to remove the background, an Ant Colony Algorithm to separate foreground from background and mathematical morphology to improve the produced segments.

The Ant-Colony Algorithm
------------------------
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
The implemented process has the following characteristics:
* The Ant Colony puts Ants on every pixel of the images. We have an specialized Ant Colony to support that procedure.
* Each Ant builts solutions while traversing pixels around its position. The lenght of this path is determined by the number of steps parameter.
* At the beginning of each iteration we randomize the position of the ants in the Colony. This is accomplished by a Daemon Action provided by the Framework.
* The node selection procedure is the Pseudo-Random policy proposed in Ant Colony System. We extend the original daemon action and add some problem-dependent logic.
* Being an Ant System algorithm, the update of the pheromone is made online, after an ant builds a solution. We use the Ant Policy provided by the framework.

The results 
-----------
We use an MR Brain image as an input. This was provided by the BrainWeb Database: http://brainweb.bic.mni.mcgill.ca/brainweb/

![Original Image](https://github.com/cptanalatriste/aco-image-thresholding/blob/master/src/inputImg/19952transverse2_64.gif?raw=true)

The program identified this areas as corresponding to the skull and fat (grey) and the cerebrum (white):

![Segments](https://raw.githubusercontent.com/cptanalatriste/aco-image-thresholding/master/with_open_process.bmp)

How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Maven Java Project. As such, it is strongly recommended that you have Maven installed before working with it.

**This project depends on the Isula Framework**.  It is available at this Github Repository: https://github.com/cptanalatriste/isula

Keep in mind that several file and folder locations were configured on the `ProblemConfiguration.java` file. You need to set values according to your environment in order to avoid a `FileNotFoundException`. 
Once this is ready, you can launch this project by executing `mvn exec:java -Dexec.mainClass="pe.edu.pucp.acothres.AcoImageThresholding"` from the project root folder.


More about Isula
----------------
Visit the Isula Framework site: http://cptanalatriste.github.io/isula/

Review the Isula JavaDoc: http://cptanalatriste.github.io/isula/doc/

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
