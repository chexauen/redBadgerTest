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


    public List<String> generateRobotOutputs(String fileName){
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            //we get the mars surface from the first line
            MarsSurface marsSurface = createMarsSurface(br.readLine());
            List<Pair<Robot, String>> robotsAndMovements = getRobotsAndMovements(br.lines().collect(Collectors.toList()));
            return generateStringOutput(marsSurface, robotsAndMovements);
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

    public List<String> generateStringOutput(MarsSurface marsSurface, List<Pair<Robot, String>> robotsAndMovements) {
        //TODO
        return new ArrayList<>();
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
