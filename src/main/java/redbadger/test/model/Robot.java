package redbadger.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Robot {

    private int currentX;
    private int currentY;
    private boolean lost;
    private RobotHeading heading;

    public enum RobotHeading{
        N,S,E,W
    }
}

