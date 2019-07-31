package redbadger.test.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import redbadger.test.model.MarsSurface;
import redbadger.test.model.Robot;

public class RobotsService {

    private Robot.RobotHeading[] orderedPositions = new Robot.RobotHeading[]{Robot.RobotHeading.N,Robot.RobotHeading.W,Robot.RobotHeading.S,Robot.RobotHeading.E};

    public List<String> generateRobotOutputs(String fileName){
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            //we get the mars surface from the first line
            MarsSurface marsSurface = createMarsSurface(br.readLine());
            List<Pair<Robot, String>> robotsAndMovements = getRobotsAndMovements(br.lines().collect(Collectors.toList()));
            moveRobotsOverSurface(marsSurface,robotsAndMovements);
            return generateStringOutput(robotsAndMovements.stream().map(Pair::getKey).collect(Collectors.toList()));
        } catch (FileNotFoundException e) {
            System.out.println("Specified file wasn't found: "+ file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("file format is wrong, please check test.txt on the resources to check the correct format");
        } finally {
            if (br !=null){
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error closing buffered reader");
                }
            }
        }
        return new ArrayList<>();
    }

    public void moveRobotsOverSurface(MarsSurface marsSurface, List<Pair<Robot,String>> robotsAndMovements) {
        for (Pair<Robot, String> robotMovementPair : robotsAndMovements) {
            char[] movementsArray = robotMovementPair.getRight().toCharArray();
            for (char c : movementsArray) {
                performMovement(marsSurface, robotMovementPair.getLeft(), c);
            }
        }
    }

    private void performMovement(MarsSurface marsSurface, Robot robot, char c) {
        if (robot.isLost()) {
            return;
        }
        if (c == 'R'){
            moveRobotToTheRight(robot);
        } else if (c == 'L'){
            moveRobotToTheLeft(robot);
        } else if (c == 'F'){
            Pair<Integer, Integer> nextPosition = positionAfterForwardMovement(robot);
            if (isPositionOutsideBorders(marsSurface, nextPosition)){
                if (marsSurface.getLostRobots().contains(Pair.of(robot.getCurrentX(),robot.getCurrentY()))){
                    //ignore
                } else {
                    robot.setLost(true);
                    marsSurface.getLostRobots().add(Pair.of(robot.getCurrentX(),robot.getCurrentY()));
                }
            } else {
                robot.setCurrentX(nextPosition.getLeft());
                robot.setCurrentY(nextPosition.getRight());
            }
        }
    }

    private boolean isPositionOutsideBorders(MarsSurface marsSurface, Pair<Integer, Integer> nextPosition) {
        return nextPosition.getLeft() <0 || nextPosition.getRight() <0 || nextPosition.getLeft() > marsSurface.getMaxX() || nextPosition.getRight() > marsSurface.getMaxY();
    }

    private Pair<Integer, Integer> positionAfterForwardMovement(Robot robot) {
        Pair<Integer, Integer> result = null;
        switch (robot.getHeading()){
            case N:
                result = Pair.of(robot.getCurrentX(),robot.getCurrentY()+1);
                break;
            case S:
                result = Pair.of(robot.getCurrentX(),robot.getCurrentY()-1);
                break;
            case W:
                result = Pair.of(robot.getCurrentX()-1,robot.getCurrentY());
                break;
            case E:
                result = Pair.of(robot.getCurrentX()+1,robot.getCurrentY());
                break;
        }
        return result;
    }

    private void moveRobotToTheLeft(Robot robot) {
        int currentHeadingValue = getCurrentHeadingValue(robot);
        robot.setHeading(orderedPositions[(currentHeadingValue + 1)%4]);
    }

    private void moveRobotToTheRight(Robot robot) {
        int currentHeadingValue = getCurrentHeadingValue(robot);
        int i = currentHeadingValue - 1;
        if (i<0){
            i=3;
        }
        robot.setHeading(orderedPositions[i%4]);
    }

    private int getCurrentHeadingValue(Robot robot) {
        switch (robot.getHeading()){
            case N:
                return 0;
            case W:
                return 1;
            case S:
                return 2;
            case E:
                return 3;
        }
        return 0;
    }

    public List<String> generateStringOutput(List<Robot> robots) {
        List<String> result = new ArrayList<>();
        for (Robot robot : robots) {
            StringBuilder sb = new StringBuilder();
            sb.append(robot.getCurrentX()).append(" ").append(robot.getCurrentY())
                    .append(" ").append(robot.getHeading().name());
            if (robot.isLost()){
                sb.append(" LOST");
            }
            result.add(sb.toString());
        }
        return result;
    }

    public List<Pair<Robot, String>> getRobotsAndMovements(List<String> robotStrings){
        List<Pair<Robot,String>> robotsAndMovements = new ArrayList<>();
        for (int i = 0; i < robotStrings.size(); i+=2) {
            String robotLine = robotStrings.get(i);
            Pair<Integer, Integer> xAndY = getXAndYFromString(robotLine);
            Robot robot = Robot.builder().currentX(xAndY.getLeft()).currentY(xAndY.getRight()).lost(false)
                    .heading(Robot.RobotHeading.valueOf(robotLine.substring(robotLine.length()-1)))
                    .build();
            robotsAndMovements.add(Pair.of(robot,robotStrings.get(i+1)));
        }
        return robotsAndMovements;
    }


    public MarsSurface createMarsSurface(String s) {
        MarsSurface marsSurface = new MarsSurface();
        Pair<Integer, Integer> xAndY = getXAndYFromString(s);
        marsSurface.setMaxX(xAndY.getLeft());
        marsSurface.setMaxY(xAndY.getRight());
        return marsSurface;
    }

    private Pair<Integer, Integer> getXAndYFromString(String s){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        m.find();
        int x = Integer.valueOf(m.group());
        m.find();
        int y = Integer.valueOf(m.group());
        return Pair.of(x,y);
    }
}
