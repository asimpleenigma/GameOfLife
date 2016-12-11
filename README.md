# GameOfLife
A GUI for simulating "life like" Cellular Automata.

## Introduction
While playing with his Go board in 1970, John Conway of Cambridge University invented a peculiar game with serious acedemic and philosophical implications. By making the state of each space on the board change based off of some simple rules, complex patterns of activity would spontaneously emerge from random initial states. The system has since been extensively studied in chaos theory, computability theory, and other fields. To me it is most striking that the rules can be seen as a metaphor for the underlying physics of the universe from which complexity as rich as you and me emerged.

## How to Play
Conway called life a "zero-player" game. The simulation is deterministic, but you can play God and interfere. There is no objective.

### The Board State
Each cell in the world grid has 2 states: alive/dead, on/off, 1/0, green/black.
Click in a cell to toggle individual cells' states.
Click the "Randomize" button to make every cell on the board alive/dead with a 50/50 chance.
Click the "Clear" button to kill every cell in the world.

### Evolution
Clicking the "step" button will cause the world to progress one time step.
The "play/pause" button will cause the world's state to continuously update or pause.
Use the slide bar to control the speed of the simulation.

### The Rules
This program is designed to simulate a variety of "life-like" automata, meaning that the next state of a cell is determined by the state of the 8 adjacent cells. With Conway's original rules, a live cell will stay alive ("survive") if the there are 2 or 3 live adjacent live cells, and a dead cell will become alive (be "born") if it has exactly 3 adjacent live cells. This rule is abreviated as (B3/S23). 10 rules that have been studied have been programmed in. Use the drop-down menu to select which set of rules you would like the world to play by. Different rules result in very different behavior!

### Patterns
Even given random initial conditions, the game of life will produce interesting results. However, the more interesting a pattern, the less common it is. Some interesting patterns have been programmed in. Hotkeys for these patterns are displayed on the control pannel. Simply press a hotkey corresponding to a pattern and click on the board where you would like to place the pattern.

![Alt Text](/ScreenShots/ConwayLife.png)
