package redbadger.test.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import redbadger.test.model.MarsSurface;
import redbadger.test.model.Robot;

public class RobotServiceTest {

    RobotsService robotsService = new RobotsService();

    @Test
    public void createSurfaceSuccessfully(){
        MarsSurface marsSurface = robotsService.createMarsSurface("2 3");
        Assertions.assertEquals(marsSurface.maxX,2);
        Assertions.assertEquals(marsSurface.maxY,3);
    }

    @Test
    public void createSurfaceIncorrectFormat(){
        try {
            robotsService.createMarsSurface("2 a");
            Assertions.fail();
        } catch (IllegalStateException e) {

        }
    }
    @Test
    public void getRobotsSuccessfully(){
        List<Pair<Robot, String>> robotsAndMovements = robotsService.getRobotsAndMovements(Arrays.asList("4 5 E", "RFR"));
        Assertions.assertEquals(robotsAndMovements.get(0).getRight(),"RFR");
        Assertions.assertEquals(robotsAndMovements.get(0).getLeft().getCurrentX(),4);
        Assertions.assertEquals(robotsAndMovements.get(0).getLeft().getCurrentY(),5);
        Assertions.assertEquals(robotsAndMovements.get(0).getLeft().getHeading(),Robot.RobotHeading.E);
    }

    @Test
    public void getRobotsIncorrectFormat(){
        try {
            robotsService.getRobotsAndMovements(Arrays.asList("4 5 E"));
            Assertions.fail();
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }


    @Test
    public void testFileSuccessfully(){
        List<String> strings = robotsService.generateRobotOutputs("src/test/resources/test.txt");
        Assertions.fail(); //TODO
    }
}
