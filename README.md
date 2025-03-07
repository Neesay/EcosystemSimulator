# EcosystemSimulator

# To-Do:
- make buttons for generations, pause play button like: <<  ||  |>  >>
- bird and worm prey

Coyote:

Opportunistic Scavenging:
In addition to hunting, allow coyotes to scavenge leftover food from carcasses (for instance, if a prey dies from disease, add a chance for a coyote to gain food from the dead prey).
Alternate Behavior:
Let coyotes sometimes switch between solitary hunting and small-group tactics (adjusting their breeding probability or movement speed when in a group).
B. For Prey
Deer:

Flight Response:
In the Deer class, implement a mechanism to “flee” when a predator is detected nearby.
Use the field’s adjacent location methods to find a cell farther from predators.
Grazing Behavior:
Allow deer to spend a turn “grazing,” during which they might slowly regain food points if no predators are nearby.
Squirrel:

Erratic Movement:
Modify the Squirrel class to include more unpredictable movement patterns.
For example, occasionally choose a random direction (not just adjacent free cell) to simulate quick, erratic jumps.
Hiding Mechanism:
Introduce a temporary “hiding” state when predators are detected, causing squirrels to remain in place for one turn to avoid detection.
Mouse:

Rapid Reproduction & Hiding:
Enhance the reproduction rate in the Mouse class under favorable conditions (high food levels).
Add behavior so that when a predator is nearby, mice increase their movement speed or hide (for example, by moving into a cell with grass that might “camouflage” them).




for disease
Variable Impact:
Implement variations where disease might reduce reproduction probability or slightly alter movement speed for a few turns.


Seasonal Changes:

Introduce a “season” variable that changes over time.
Modify grass growth rates, animal metabolism, or breeding probabilities based on the season (e.g., winter might slow growth and reduce food availability).
Interactive UI Controls:

In SimulatorView, add extra UI elements (sliders, buttons, checkboxes) that let the user adjust parameters like simulation speed, breeding probability, or disease spread.
Consider adding a “Pause/Resume” button.
Extra: Include a small panel displaying a live graph or statistics about population trends and gene variations.
Additional Statistics:

Extend FieldStats to record more detailed information  average gene values per species, frequency of disease outbreaks.
Display these statistics in the UI or in the report









Animals  
Wolf       - hunts in packs(pack of 3+) for deers, but hunts squirrels and mice without packs
Coyote     
Squirrel   
Mice       
Deer       


1. Add new Organisms:
    - Wolves - Predator
          - breeds slow
          - litter size (2)
          - max age (130)
          - breeding prob. (0.06)
          - breeding age (20)

      
    - Mice - Prey
          - breeds fast
          - low food value (4)
          - litter size (9)
          - max age (20)
          - breeding prob. (0.15)
          - breeding age (3)
      
    - Deer - Prey
          - breeds slow
          - high food value (18)
          - litter size (2)
          - max age (80)
          - breeding prob. (0.10)
          - breeding age (10)

2. Ideas:
    - Have different colours in the spectrum to show age of animal or whatever, so like bright red would be new born and it would get darker as they age towards their max age
    - edit the preditor classes so that it adds the foodvalue of the prey it eats to its foodlevel (not just rabbit).
    - pray should have hunger. (copy from preditor classes)
    - add menopause

3. disease:
    - set number of steps til death
    - set the probability of catching a disease
    - 
    
possible extra features:
Dynamic Weather Events: not implemented
Introduce random weather conditions (such as droughts or heavy rain) that temporarily affect plant growth and animal reproduction. For example, during a drought, plant reproduction or food value could be reduced, influencing both prey and predator dynamics.

Interactive Control Panel: done
Add a simple user control interface (using JavaFX controls) that lets the user pause/resume the simulation and adjust key parameters (like disease spread probability or reproduction rates) in real time. This interactivity can enhance the simulation’s usability and allow for dynamic experimentation.

Population Trend Graph: done
Integrate a real-time chart (e.g., using JavaFX’s LineChart) that plots the population of each species over time. This visual feedback not only enriches the simulation’s presentation but also helps analyze long-term ecological trends.

Detailed Simulation Logging: done
Implement a logging mechanism that records simulation events (births, deaths, disease outbreaks, weather events) and population statistics to a file. This extra data layer can be used for post-simulation analysis and debugging, showing a deeper level of system insight.

Enhanced Animal Behaviors: not implemented
Introduce additional, species-specific behaviors without altering their core traits. For example, add a simple territorial behavior for predators where they avoid overcrowded areas, or a grouping mechanism for prey (like mice or squirrels) that slightly boosts their survival chance when near each other.
