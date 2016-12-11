/*
 */
package gameoflife;

import java.awt.Point;
import java.util.HashMap;

/**
 * @author Lloyd Cloer
 */
public class RuleSet{
    static HashMap rule_sets = new HashMap<String, RuleSet>();
    String name;//, notation;
    int[] B; int[] S; // number of neighbors for birth and survival
    HashMap patterns; // pattern name to point array
    String legend; // instructions to use hotkeys

    RuleSet(String name, int[] B, int[] S){
        this.B = B; this.S = S; this.legend = "Hotkeys: <br>";
        name += " (B";
        for (int b : B) name += b;
        name += "/S";
        for (int s : S) name += s;
        name += ")";
        this.name = name;
        rule_sets.put(name, this);
        patterns = new HashMap<String, Point[]>();
    }
    public void addPattern(String pattern_name, String hotkey, Point[] pattern){
        legend = legend + hotkey.toUpperCase() + " : " + pattern_name + "<br>";
        patterns.put(hotkey, pattern);
    }
    public boolean nextState(boolean currentState, int n_neighbors){
        if (currentState){
            for (int s : S)
                if (n_neighbors == s) return true;
        } else
            for (int b : B)
                if (n_neighbors == b) return true;
        return false;
    }
}
