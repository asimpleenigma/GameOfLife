/**
 * @author Lloyd Cloer
 */
package gameoflife;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class GameOfLife extends JFrame implements ActionListener{
    private final int n_rows = 60;
    private final int n_columns = 120;
    private boolean[][] world_model;
    private boolean[][] next_world;
    private int delay;
    private Timer timer;
    private RuleSet rule_set;
    private HashMap<String, RuleSet> rule_sets;
    private RuleSelector rule_selector;
    private JLabel hotkey_legend;
    private String primed_key;
    
    public static void main(String[] args){
        GameOfLife app = new GameOfLife();
    }
    public GameOfLife(){
        KeyboardFocusManager kbfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kbfm.addKeyEventDispatcher(new MyDispatcher());
        
        setTitle("Game of Life");
        setSize(1200, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Master Panel
        JPanel master_panel = (JPanel) this.getContentPane();
        master_panel.setLayout(new BorderLayout());
        
        // Button Panel
        JPanel control_panel = new JPanel();
        control_panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
     //   button_panel.setLayout(new GridLayout(8, 1, 5, 5));
        control_panel.setLayout(new BoxLayout(control_panel, BoxLayout.Y_AXIS));
      //  button_panel.setPreferredSize(new Dimension(200,100));
        master_panel.add(control_panel, BorderLayout.WEST);
        
        // Rule Selector
        primed_key = "";
        rule_selector = new RuleSelector();
        
        // Hotkey Legend
        hotkey_legend = new JLabel();
        //master_panel.add(hotkey_legend, BorderLayout.EAST);
        
        // Init Rule Sets
        //initRules();
        Rules rules = new Rules();
        rule_sets = rules.rule_sets;
        for (String name : rule_sets.keySet()){ 
            rule_selector.addItem(name);
        }
        
        
        // Button Panel
        JPanel button_panel = new JPanel();
        button_panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button_panel.setLayout(new GridLayout(5, 1, 5, 5));
        button_panel.setMaximumSize(new Dimension(200, 200));
        
        button_panel.add(rule_selector);
        // Pause Button
        JButton pause_button = new JButton("Play");
        button_panel.add(pause_button);
        pause_button.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e){
                if (timer.isRunning()){timer.stop(); pause_button.setText("Play");}
                else{timer.start(); pause_button.setText("Pause");}
            }
        });
        // Step Button
        JButton step_button = new JButton("Step");
        step_button.addActionListener(this);
        button_panel.add(step_button);
        
        // Randomize Button
        JButton randomize_button = new JButton("Randomize");
        button_panel.add(randomize_button);
        randomize_button.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {randomizeWorld();}
        });
        
        // Clear Button
        JButton clear_button = new JButton("Clear");
        button_panel.add(clear_button);
        clear_button.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {clearWorld();}
        });
        control_panel.add(button_panel);
        
        // Hotkey Legend
        control_panel.add(hotkey_legend);
        
        // Speed Slider
        //control_panel.add(new JLabel(""));
        control_panel.add(new JLabel("Speed"));
        JSlider speed_slider = new JSlider(JSlider.HORIZONTAL, 1, 30, 10); // timesteps per second
        speed_slider.setMaximumSize(new Dimension(220,40));
        control_panel.add(speed_slider);
        speed_slider.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()){
                    delay = 1000 / source.getValue();
                    timer.setDelay(delay);
                }
            }
        });
        // World Panel
        JPanel world_panel = new JPanel();
        world_panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        world_panel.setLayout(new GridLayout(n_rows, n_columns, 0, 0));
        master_panel.add(world_panel, BorderLayout.CENTER);
        // Cells
        world_model = new boolean[n_rows][n_columns];
        next_world = new boolean[n_rows][n_columns];
        //randomizeWorld(); // initalize randomly
        for(int row = 0; row < n_rows; row++){
            for (int col = 0; col < n_columns; col++){
                Cell s = new Cell(row, col);
                world_panel.add(s);
            }
        }
        rule_selector.setSelectedItem("Conway's Life (B3/S23)");
        setVisible(true);
        
        // Timer
        delay = 100;
        timer = new Timer(delay, this);
        timer.setRepeats(true);
    }
    @Override // Main class's action for timer
    public void actionPerformed(ActionEvent e){
        for(int row = 0; row < n_rows; row++) //for each cell
            for (int col = 0; col < n_columns; col++)
                evolveCell(row, col);
        world_model = next_world;
        next_world = new boolean[n_rows][n_columns];
        repaint();
    }
    private void randomizeWorld(){
        for(int row = 0; row < n_rows; row++)
            for (int col = 0; col < n_columns; col++)
                world_model[row][col] = Math.random() > .5; // random initialization
        repaint();
    }
    private void clearWorld(){
        world_model = new boolean[n_rows][n_columns];
        repaint();
    }       
    private class Cell extends JButton{
        public int row, col;
        public Cell(int row, int col){
            super();
            this.row = row; 
            this.col = col;
            addMouseListener(new PatternDrawer());
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Dimension d = getSize();
            if (world_model[row][col])
                g.setColor(Color.GREEN);
            else 
                g.setColor(Color.BLACK);
            g.fillRect(0, 0, d.width, d.height);
        }
    }
    private int countNeighbors(int row, int col){
        int n_neighbors = 0;
        for (int r = -1; r < 2; r++)
            for (int c = -1; c < 2; c++){
                if (c == 0 && r == 0) continue; // don't count self
                if (world_model[(row+r+n_rows)%n_rows][(col+c+n_columns)%n_columns]) 
                    n_neighbors++;
            }
        return n_neighbors;
    }
    private void evolveCell(int row, int col){
        int n_neighbors = countNeighbors(row, col);
        next_world[row][col] = rule_set.nextState(world_model[row][col], n_neighbors);
    }
    private class RuleSelector extends JComboBox{
        public RuleSelector(){
            super();
            this.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    JComboBox cb = (JComboBox) e.getSource();
                    String s_rule = (String) cb.getSelectedItem();
                    rule_set = (RuleSet) rule_sets.get(s_rule);
                    String str = "<html>"+rule_set.legend+"<br><br><html>";
                    hotkey_legend.setText(str);
                }
            });
            //this.setSelectedItem("Conway's Life (B3/S23)");
        }
    }
    private class PatternDrawer extends MouseAdapter{
        private void drawPattern(Point position, Point[] pattern){
            for (Point p : pattern)
                world_model[(position.y+p.y)%n_rows][(position.x+p.x)%n_columns] = true;
        }
        @Override
        public void mousePressed(MouseEvent e){
            Cell cell = (Cell) e.getSource();
            if (rule_set.patterns.containsKey(primed_key)){
                Point[] p = (Point[]) rule_set.patterns.get(primed_key);
                drawPattern(new Point(cell.col,cell.row), p);
                primed_key = "";
            }
            else{
                world_model[cell.row][cell.col] = world_model[cell.row][cell.col]==false;
            }
            repaint();
        }
        @Override 
        public void mouseEntered(MouseEvent e){
            if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0){
                Cell cell = (Cell) e.getSource();
                world_model[cell.row][cell.col] = world_model[cell.row][cell.col]==false;
                cell.repaint();
            }
        }
    }
    public class MyDispatcher implements KeyEventDispatcher{
        @Override
        public boolean dispatchKeyEvent(KeyEvent e){
            if (e.getID() == KeyEvent.KEY_PRESSED){
                String c = e.getKeyChar()+"";
                primed_key = c;
            }
            return false;
        }
    } 
}
