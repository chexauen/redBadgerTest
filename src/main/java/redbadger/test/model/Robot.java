package redbadger.test.model;

import lombok.Data;

@Data
public class Robot {

    private int currentX;
    private int currentY;
    private boolean lost;
    private RobotHeading heading;

    public enum RobotHeading{
        N,S,E,W
    }
}

