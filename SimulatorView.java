import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import java.util.HashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid. The view displays a rectangle for
 * each location, shows a legend with colored squares for each animal type, and
 * provides an interactive control panel with buttons to pause/resume the simulation,
 * show a population chart, view detailed simulation logs, and restart the simulation.
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
    
    // Control flag for pausing the simulation.
    private volatile boolean paused = false;

    private FieldCanvas fieldCanvas;
    private FieldStats stats;
    private Simulator simulator;
    
    // Map for the chart series (used in the chart feature).
    private Map<String, XYChart.Series<Number, Number>> seriesMap = new HashMap<>();

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

        // Legend pane for species colors.
        legendPane = new HBox();
        legendPane.setSpacing(10);

        // Create control panel with Pause/Resume, Show Chart, Show Log, and Restart buttons.
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
        Button chartButton = new Button("Show Chart");
        chartButton.setOnAction(e -> showChart());
        Button logButton = new Button("Show Log");
        logButton.setOnAction(e -> showLog());
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            reset();
            setInfoText("Simulation Restarted");
        });
        controlPane.getChildren().addAll(pauseButton, chartButton, logButton, restartButton);

        BorderPane bPane = new BorderPane();
        HBox infoPane = new HBox();
        infoPane.setSpacing(10);
        // Add control panel to the top pane.
        infoPane.getChildren().addAll(genLabel, infoLabel, controlPane);
        bPane.setTop(infoPane);
        bPane.setCenter(fieldCanvas);

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
     * Updates the canvas with the current simulation state and updates the legend.
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
                    stats.incrementCount(animal.getClass(), animal);
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
     * Checks whether the simulation is viable (i.e., more than one species is alive).
     * @param field The simulation field.
     * @return true if viable, false otherwise.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Runs the simulation for the given number of generations.
     * Pauses execution if the simulation is paused.
     * @param numStep The number of generations to run.
     */
    public void simulate(int numStep) {
        new Thread(() -> {
            for (int gen = 1; gen <= numStep; gen++) {
                while (paused) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        // Handle interruption if needed.
                    }
                }
                simulator.simulateOneStep();
                simulator.delay(300);
                Platform.runLater(() -> updateCanvas(simulator.getStep(), simulator.getField()));
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

    /**
     * Displays a new window with a line chart tracking population trends.
     */
    private void showChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Population");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Population Trends");

        XYChart.Series<Number, Number> deerSeries = new XYChart.Series<>();
        deerSeries.setName("Deer");
        XYChart.Series<Number, Number> mouseSeries = new XYChart.Series<>();
        mouseSeries.setName("Mouse");
        XYChart.Series<Number, Number> squirrelSeries = new XYChart.Series<>();
        squirrelSeries.setName("Squirrel");
        XYChart.Series<Number, Number> coyoteSeries = new XYChart.Series<>();
        coyoteSeries.setName("Coyote");
        XYChart.Series<Number, Number> wolfSeries = new XYChart.Series<>();
        wolfSeries.setName("Wolf");
        XYChart.Series<Number, Number> grassSeries = new XYChart.Series<>();
        grassSeries.setName("Grass");

        lineChart.getData().addAll(deerSeries, mouseSeries, squirrelSeries, coyoteSeries, wolfSeries, grassSeries);

        seriesMap.put("Deer", deerSeries);
        seriesMap.put("Mouse", mouseSeries);
        seriesMap.put("Squirrel", squirrelSeries);
        seriesMap.put("Coyote", coyoteSeries);
        seriesMap.put("Wolf", wolfSeries);
        seriesMap.put("Grass", grassSeries);

        Stage chartStage = new Stage();
        chartStage.setTitle("Population Chart");
        Scene chartScene = new Scene(lineChart, 800, 600);
        chartStage.setScene(chartScene);
        chartStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateChart()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates the population chart with current simulation data.
     */
    private void updateChart() {
        int generation = simulator.getStep();
        Map<String, Integer> counts = new HashMap<>();
        counts.put("Deer", 0);
        counts.put("Mouse", 0);
        counts.put("Squirrel", 0);
        counts.put("Coyote", 0);
        counts.put("Wolf", 0);
        counts.put("Grass", 0);
        
        Field field = simulator.getField();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getObjectAt(row, col);
                if (animal != null && animal.isAlive()) {
                    String species = animal.getClass().getSimpleName();
                    counts.put(species, counts.getOrDefault(species, 0) + 1);
                }
            }
        }
        
        for (Map.Entry<String, XYChart.Series<Number, Number>> entry : seriesMap.entrySet()) {
            String species = entry.getKey();
            XYChart.Series<Number, Number> series = entry.getValue();
            int count = counts.getOrDefault(species, 0);
            series.getData().add(new XYChart.Data<>(generation, count));
        }
    }

    /**
     * Displays a new window with detailed simulation logs.
     * The log includes total deaths, births, disease events, and averages per 50 generations.
     */
    private void showLog() {
        Stage logStage = new Stage();
        logStage.setTitle("Simulation Log");
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        
        VBox logPane = new VBox();
        logPane.getChildren().add(logArea);
        VBox.setVgrow(logArea, Priority.ALWAYS);
        
        Scene logScene = new Scene(logPane, 600, 600);
        logStage.setScene(logScene);
        logStage.show();
        
        Timeline logTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            String logText = getSimulationLog();
            logArea.setText(logText);
        }));
        logTimeline.setCycleCount(Timeline.INDEFINITE);
        logTimeline.play();
    }
    
    /**
     * Retrieves simulation logging information using actual counters.
     * This includes total deaths, total births, disease catches, and disease spreads,
     * as well as average deaths and births per 50 generations for each species.
     * @return A string representing the simulation log.
     */
    private String getSimulationLog() {
        int totalDeaths = Animal.totalDeaths;
        int totalBirths = Animal.totalBirths;
        int diseaseCatches = Animal.totalDiseaseCatches;
        int diseaseSpreads = Animal.totalDiseaseSpreads;
        
        int generation = simulator.getStep();
        double factor = generation / 50.0;
        if (factor < 1) {
            factor = 1;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Simulation Log:\n");
        sb.append("Total Deaths: ").append(totalDeaths).append("\n");
        sb.append("Total Births: ").append(totalBirths).append("\n");
        sb.append("Animals Catching Disease: ").append(diseaseCatches).append("\n");
        sb.append("Disease Spreads: ").append(diseaseSpreads).append("\n\n");
        sb.append("Averages per 50 generations:\n");
        for (String species : Animal.deathsBySpecies.keySet()) {
            int speciesDeaths = Animal.deathsBySpecies.get(species);
            int speciesBirths = Animal.birthsBySpecies.getOrDefault(species, 0);
            double avgDeaths = speciesDeaths / factor;
            double avgBirths = speciesBirths / factor;
            sb.append(species)
              .append(" - Avg Deaths: ").append(String.format("%.1f", avgDeaths))
              .append(", Avg Births: ").append(String.format("%.1f", avgBirths))
              .append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
