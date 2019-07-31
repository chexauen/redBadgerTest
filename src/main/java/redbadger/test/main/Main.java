package redbadger.test.main;

import redbadger.test.service.RobotsService;

public class Main {

    public static void main(String ... args){
        if (args.length != 1){
            System.out.println("Usage java -jar RedBadger.jar fileWithInstructions");
        } else {
            RobotsService robotsService = new RobotsService();
            for (String s : robotsService.generateRobotOutputs(args[0])) {
                System.out.println(s);
            }
        }
    }
}
