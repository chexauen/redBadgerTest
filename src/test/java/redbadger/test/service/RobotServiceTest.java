package redbadger.test.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(marsSurface.maxX,2);
        assertEquals(marsSurface.maxY,3);
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
        assertEquals(robotsAndMovements.get(0).getRight(),"RFR");
        assertEquals(robotsAndMovements.get(0).getLeft().getCurrentX(),4);
        assertEquals(robotsAndMovements.get(0).getLeft().getCurrentY(),5);
        assertEquals(robotsAndMovements.get(0).getLeft().getHeading(),Robot.RobotHeading.E);
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
        assertArrayEquals(strings.toArray(),Arrays.asList("1 1 E","3 3 N LOST","2 3 S").toArray());
    }

    @Test
    public void testOutputGeneratorSuccessfully(){
        Robot robot1 = Robot.builder().currentX(1).currentY(2).heading(Robot.RobotHeading.E).lost(false).build();
        Robot robot2 = Robot.builder().currentX(3).currentY(4).heading(Robot.RobotHeading.N).lost(false).build();
        Robot robot3 = Robot.builder().currentX(5).currentY(6).heading(Robot.RobotHeading.W).lost(false).build();
        Robot robot4 = Robot.builder().currentX(7).currentY(8).heading(Robot.RobotHeading.S).lost(false).build();
        Robot robot5 = Robot.builder().currentX(9).currentY(10).heading(Robot.RobotHeading.S).lost(true).build();
        List<String> strings = robotsService.generateStringOutput(Arrays.asList(robot1, robot2, robot3, robot4, robot5));
        assertArrayEquals(strings.toArray(), Arrays.asList("1 2 E","3 4 N","5 6 W","7 8 S","9 10 S LOST").toArray());
    }

    @Test
    public void moveRobotRight(){
        MarsSurface marsSurface = MarsSurface.builder().maxX(1).maxY(1).build();
        Robot robot = Robot.builder().currentX(0).currentY(0).heading(Robot.RobotHeading.N).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"R")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.E);
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"R")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.S);
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"R")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.W);
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"R")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.N);
    }

    @Test
    public void moveRobotLeft(){
        MarsSurface marsSurface = MarsSurface.builder().maxX(1).maxY(1).build();
        Robot robot = Robot.builder().currentX(0).currentY(0).heading(Robot.RobotHeading.N).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"L")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.W);
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"L")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.S);
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"L")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.E);
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"L")));
        assertEquals(robot.getHeading(),Robot.RobotHeading.N);
    }

    @Test
    public void moveRobotEachDirection(){
        MarsSurface marsSurface = MarsSurface.builder().maxX(2).maxY(2).build();
        Robot robot = Robot.builder().currentX(1).currentY(1).heading(Robot.RobotHeading.N).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"F")));
        assertEquals(robot.getCurrentX(),1);
        assertEquals(robot.getCurrentY(),2);
        robot = Robot.builder().currentX(1).currentY(1).heading(Robot.RobotHeading.S).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"F")));
        assertEquals(robot.getCurrentX(),1);
        assertEquals(robot.getCurrentY(),0);
        robot = Robot.builder().currentX(1).currentY(1).heading(Robot.RobotHeading.W).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"F")));
        assertEquals(robot.getCurrentX(),2);
        assertEquals(robot.getCurrentY(),1);
        robot = Robot.builder().currentX(1).currentY(1).heading(Robot.RobotHeading.E).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"F")));
        assertEquals(robot.getCurrentX(),0);
        assertEquals(robot.getCurrentY(),1);
    }

    @Test
    public void moveRobotWithinGrid(){
        MarsSurface marsSurface = MarsSurface.builder().maxX(1).maxY(1).build();
        Robot robot = Robot.builder().currentX(0).currentY(0).heading(Robot.RobotHeading.N).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"FRFRFRF")));
        assertEquals(robot.getCurrentX(),0);
        assertEquals(robot.getCurrentY(),0);
        assertEquals(robot.getHeading(),Robot.RobotHeading.E);
        assertFalse(robot.isLost());
        assertTrue(marsSurface.getLostRobots().isEmpty());
    }

    @Test
    public void moveRobotsOutOfGrid(){
        MarsSurface marsSurface = MarsSurface.builder().maxX(1).maxY(1).build();
        Robot robot = Robot.builder().currentX(0).currentY(0).heading(Robot.RobotHeading.N).build();
        //First robot goes out
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"FFFFRF")));
        assertEquals(robot.getCurrentX(),0);
        assertEquals(robot.getCurrentY(),1);
        assertEquals(robot.getHeading(),Robot.RobotHeading.N);
        assertTrue(robot.isLost());
        assertTrue(marsSurface.getLostRobots().contains(Pair.of(0,1)));

        //second doesn't
        robot = Robot.builder().currentX(0).currentY(0).heading(Robot.RobotHeading.N).build();
        robotsService.moveRobotsOverSurface(marsSurface,Arrays.asList(Pair.of(robot,"FF")));
        assertEquals(robot.getCurrentX(),0);
        assertEquals(robot.getCurrentY(),1);
        assertEquals(robot.getHeading(),Robot.RobotHeading.N);
        assertFalse(robot.isLost());
    }
}
