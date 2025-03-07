import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid. The view displays a rectangle for
 * each location and shows a legend with colored squares for each animal type.
 * It also provides an interactive control panel to pause/resume the simulation.
 */
public class SimulatorView extends Application {

    public static final int GRID_WIDTH = 100;
    public static final int GRID_HEIGHT = 80;
    public static final int WIN_WIDTH = 650;
    public static final int WIN_HEIGHT = 650;

    private static final Color EMPTY_COLOR = Color.WHITE;

    private final String GENERATION_PREFIX = "Generation: ";
    private final String POPULATION_PREFIX = "Population: ";

    private Label genLabel, population, infoLabel;
    private HBox legendPane;
    
    // Flag to control pausing/resuming the simulation.
    private volatile boolean paused = false;

    private FieldCanvas fieldCanvas;
    private FieldStats stats;
    private Simulator simulator;

    @Override
    public void start(Stage stage) {

        stats = new FieldStats();
        fieldCanvas = new FieldCanvas(WIN_WIDTH - 50, WIN_HEIGHT - 50);
        fieldCanvas.setScale(GRID_HEIGHT, GRID_WIDTH);
        simulator = new Simulator(GRID_HEIGHT, GRID_WIDTH);

        Group root = new Group();

        genLabel = new Label(GENERATION_PREFIX);
        infoLabel = new Label("  ");
        population = new Label(POPULATION_PREFIX);

        // Legend pane for displaying species colors.
        legendPane = new HBox();
        legendPane.setSpacing(10);

        // Create control panel with a Pause/Resume button.
        HBox controlPane = new HBox();
        controlPane.setSpacing(10);
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> {
            paused = !paused;
            if (paused) {
                pauseButton.setText("Resume");
                setInfoText("Simulation Paused");
            } else {
                pauseButton.setText("Pause");
                setInfoText("Simulation Running");
            }
        });
        controlPane.getChildren().add(pauseButton);

        BorderPane bPane = new BorderPane();
        HBox infoPane = new HBox();
        infoPane.setSpacing(10);
        // Add the control panel to the top info pane.
        infoPane.getChildren().addAll(genLabel, infoLabel, controlPane);
        bPane.setTop(infoPane);
        bPane.setCenter(fieldCanvas);

        // Bottom pane with population info and legend.
        VBox bottomPane = new VBox();
        bottomPane.setSpacing(5);
        bottomPane.getChildren().addAll(population, legendPane);
        bPane.setBottom(bottomPane);

        root.getChildren().add(bPane);
        Scene scene = new Scene(root, WIN_WIDTH, WIN_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Predator/Prey Simulation");
        updateCanvas(simulator.getStep(), simulator.getField());
        stage.show();

        simulate(2000);
    }

    /**
     * Sets the informational text at the top of the window.
     * @param text The text to display.
     */
    public void setInfoText(String text) {
        infoLabel.setText(text);
    }

    /**
     * Updates the canvas with the current simulation generation and field state.
     * Also updates the legend with species colors.
     * @param generation The current generation.
     * @param field The simulation field.
     */
    public void updateCanvas(int generation, Field field) {
        genLabel.setText(GENERATION_PREFIX + generation);
        stats.reset();

        Map<Class<?>, Color> legendMap = new HashMap<>();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getObjectAt(row, col);
                if (animal != null && animal.isAlive()) {
                    stats.incrementCount(animal.getClass());
                    fieldCanvas.drawMark(col, row, animal.getColor());
                    legendMap.putIfAbsent(animal.getClass(), animal.getColor());
                } else {
                    fieldCanvas.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }

        stats.countFinished();
        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));

        legendPane.getChildren().clear();
        for (Map.Entry<Class<?>, Color> entry : legendMap.entrySet()) {
            Rectangle colorSquare = new Rectangle(10, 10, entry.getValue());
            Label animalLabel = new Label(entry.getKey().getSimpleName());
            HBox legendItem = new HBox(5, colorSquare, animalLabel);
            legendPane.getChildren().add(legendItem);
        }
    }

    /**
     * Determines whether the simulation is viable (more than one species is alive).
     * @param field The simulation field.
     * @return true if viable, false otherwise.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Runs the simulation for a given number of generations.
     * Pauses the simulation if the pause flag is set.
     * @param numStep The number of generations to run.
     */
    public void simulate(int numStep) {
        new Thread(() -> {
            for (int gen = 1; gen <= numStep; gen++) {
                // Check if simulation is paused.
                while (paused) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        // Handle interruption if necessary.
                    }
                }
                simulator.simulateOneStep();
                simulator.delay(100);
<<<<<<< Updated upstream
                Platform.runLater(() -> {
                    updateCanvas(simulator.getStep(), simulator.getField());
                });
    
=======
                Platform.runLater(() -> updateCanvas(simulator.getStep(), simulator.getField()));
>>>>>>> Stashed changes
                if (!isViable(simulator.getField())) {
                    simulator.delay(3000);
                    Platform.runLater(() -> reset());
                }
            }
        }).start();
    }

    /**
     * Resets the simulation to the starting state.
     */
    public void reset() {
        simulator.reset();
        updateCanvas(simulator.getStep(), simulator.getField());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
