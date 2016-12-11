/*
 */
package gameoflife;

import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Lloyd Cloer
 */
public class Rules {
    public HashMap<String, RuleSet> rule_sets;
    
    public Rules(){
        initRules();
        rule_sets = RuleSet.rule_sets;
    }
    
    private Point[] translate(Point[] pattern, int x, int y){
        Point [] result = new Point[pattern.length];
        Point p;
        for (int i = 0; i < pattern.length; i++){
            p = (Point) pattern[i].getLocation();
            p.translate(x, y);
            result[i] = p;
        }
        return result;
    }
    //private Point[] rotate(Point[] pattern){}
    private Point[] concatenate(Point[] b, Point[] a){
        int a_len = a.length;
        int b_len = b.length;
        Point[] c = new Point[a_len+b_len];
        System.arraycopy(a, 0, c, 0, a_len);
        System.arraycopy(b, 0, c, a_len, b_len);
        return c;
    }
    private void initRules(){ //upon construction rulesets are added to static RuleSet hashmap
        //rule_sets = new HashMap<String, RuleSet>();
        RuleSet rules = new RuleSet("Conway's Life", new int[]{3}, new int[]{2,3}); 
            rules.addPattern("Toad", "t", new Point[]{new Point(0,0), new Point(1,0), 
                new Point(2,0), new Point(1,1), new Point(2,1), new Point(3,1)});    
            Point[] glider = new Point[]{new Point(0,0), new Point(2,0), new Point(1,1), new Point(1,2), new Point(2,1)};
            rules.addPattern("Glider", "g", glider);
            Point[] blinker = new Point[]{new Point(0,0), new Point(0,1), new Point(0,2)};
          //  rules.addPattern("Blinker", "b", blinker);
            Point[] lightweight_ss = new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0), 
                new Point(4,1), new Point(4,3), new Point(0,1), new Point(0,2), new Point(1,3)};
            rules.addPattern("Lightweight Spaceship", "s", lightweight_ss);
            rules.addPattern("Caterer", "c", new Point[]{new Point(0,1), 
                new Point(0,2), new Point(0,3), new Point(1,5), new Point(2,5), 
                new Point(2,0), new Point(3,4), new Point(4,2), new Point(4,1),
                new Point(5,1), new Point(6,1), new Point(7,1)});
            Point[] block = new Point[]{new Point(0,0), new Point(0,1), new Point(1,0), new Point(1,1)};
            Point[] queen_bee = new Point[]{new Point(0,0), new Point(0,1), 
                new Point(1,2), new Point(1,3), new Point(1,4), new Point(0,5), 
                new Point(0,6), new Point(2,1), new Point(3,2), new Point(4,3), 
                new Point(3,4), new Point(2,5)};
            Point[] queen_bee_shuttle = concatenate(translate(queen_bee, 12, 0),concatenate(translate(block, 0, 2), translate(block, 20, 3)));
            rules.addPattern("Queen Bee Shuttle", "q", queen_bee_shuttle);
            
        rules = new RuleSet("Higher Life", new int[]{3, 6}, new int[]{2,3});
            rules.addPattern("Glider", "g", glider);
            rules.addPattern("Lightweight Spaceship", "s", lightweight_ss);
            Point[] replicator = new Point[]{new Point(1,0), new Point(2,0), new Point(3,0), new Point(0,1), new Point(0,2), new Point(0,3)};
            rules.addPattern("Replicator", "r", replicator);
            Point[] bomber = concatenate(replicator, translate(blinker, 9, 3));
            rules.addPattern("Bomber", "b", bomber);
        new RuleSet("Seeds", new int[]{2}, new int[]{});
        new RuleSet("Life without Death", new int[]{3}, new int[]{0,1,2,3,4,5,6,7,8});
        new RuleSet("34 Life", new int[]{3,4}, new int[]{3,4});
        new RuleSet("Diamoeba", new int[]{3,5,6,7,8}, new int[]{5,6,7,8});
        new RuleSet("Morley's Move", new int[]{3,6,8}, new int[]{2,4,5});
        new RuleSet("Day & Night", new int[]{3,6,7,8}, new int[]{3,4,6,7,8});
        new RuleSet("Replicator 1", new int[]{1,3,5,7}, new int[]{1,3,5,7});
        new RuleSet("Fredkin", new int[]{1,3,5,7}, new int[]{0,2,4,6,8});
        new RuleSet("Gnarl", new int[]{1}, new int[]{1});
    }
}
