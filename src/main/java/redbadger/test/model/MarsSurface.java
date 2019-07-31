package redbadger.test.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarsSurface {

    public int maxX;
    public int maxY;
    public Set<Pair<Integer,Integer>> lostRobots = new HashSet<Pair<Integer, Integer>>();
}
