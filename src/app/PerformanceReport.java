package app;

import com.technoearth.model.StudentPerformance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;

public class PerformanceReport {

    private TableView<StudentPerformance> table;
    private ObservableList<StudentPerformance> fullData;
    private ObservableList<StudentPerformance> currentPageData;

    private int currentPage = 0;
    private final int PAGE_SIZE = 10;
    private Label pageInfoLabel;

    public PerformanceReport() {
        table = new TableView<>();
        currentPageData = FXCollections.observableArrayList();
        fullData = FXCollections.observableArrayList(
                new StudentPerformance("Rahul", 8, 7, 9),
                new StudentPerformance("Sneha", 9, 9, 10),
                new StudentPerformance("Amit", 6, 5, 6),
                new StudentPerformance("Priya", 7, 8, 7),
                new StudentPerformance("Kiran", 9, 10, 9),
                new StudentPerformance("Ayesha", 8, 9, 8),
                new StudentPerformance("Manoj", 6, 6, 7),
                new StudentPerformance("Deepa", 10, 9, 10),
                new StudentPerformance("Ravi", 7, 8, 8),
                new StudentPerformance("Snehal", 9, 10, 9),
                new StudentPerformance("Kartik", 8, 7, 8),
                new StudentPerformance("Simran", 7, 8, 9),
                new StudentPerformance("Sonal", 6, 6, 6),
                new StudentPerformance("Mehul", 10, 9, 10),
                new StudentPerformance("Aarti", 8, 9, 7)
        );

        createTable();
        table.setItems(currentPageData);  // show only current page data
    }

    private void createTable() {
        TableColumn<StudentPerformance, String> nameCol = new TableColumn<>("Student");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudentPerformance, Integer> hwCol = new TableColumn<>("Homework");
        hwCol.setCellValueFactory(new PropertyValueFactory<>("homeworkScore"));

        TableColumn<StudentPerformance, Integer> participationCol = new TableColumn<>("Participation");
        participationCol.setCellValueFactory(new PropertyValueFactory<>("participationScore"));

        TableColumn<StudentPerformance, Integer> tasksCol = new TableColumn<>("Daily Tasks");
        tasksCol.setCellValueFactory(new PropertyValueFactory<>("dailyTaskScore"));

        table.getColumns().addAll(nameCol, hwCol, participationCol, tasksCol);
    }

    public Pane getView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Button exportCSV = new Button("📄 Export CSV");
        Button exportPNG = new Button("🖼 Export PNG");

        Button prevBtn = new Button("⬅ Prev");
        Button nextBtn = new Button("Next ➡");

        pageInfoLabel = new Label(); // ✅ initialize before calling loadPage

        prevBtn.setOnAction(e -> {
            if (currentPage > 0) {
                loadPage(currentPage - 1);
            }
        });

        nextBtn.setOnAction(e -> {
            if ((currentPage + 1) * PAGE_SIZE < fullData.size()) {
                loadPage(currentPage + 1);
            }
        });

        exportCSV.setOnAction(e -> exportTableToCSV());
        exportPNG.setOnAction(e -> exportTableAsImage());

        HBox paginationControls = new HBox(10, prevBtn, nextBtn, pageInfoLabel);

        layout.getChildren().addAll(
                new Label("📊 Student Performance Report"),
                table,
                paginationControls,
                exportCSV,
                exportPNG
        );

        loadPage(0); // ✅ load after pageInfoLabel is initialized

        return layout;
    }

    private void loadPage(int pageIndex) {
        currentPage = pageIndex;
        int fromIndex = pageIndex * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, fullData.size());

        currentPageData.setAll(fullData.subList(fromIndex, toIndex));
        pageInfoLabel.setText("Page " + (currentPage + 1) + " of " + ((fullData.size() + PAGE_SIZE - 1) / PAGE_SIZE));
    }

    private void exportTableToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CSV");
        fileChooser.setInitialFileName("performance_report.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                List<TableColumn<StudentPerformance, ?>> columns = table.getColumns();
                for (TableColumn<StudentPerformance, ?> column : columns) {
                    writer.write(column.getText() + ",");
                }
                writer.newLine();

                for (StudentPerformance sp : fullData) {
                    writer.write(sp.getName() + "," + sp.getHomeworkScore() + "," +
                            sp.getParticipationScore() + "," + sp.getDailyTaskScore());
                    writer.newLine();
                }

                System.out.println("✅ Exported CSV to: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("❌ CSV Export Failed: " + e.getMessage());
            }
        }
    }

    private void exportTableAsImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as Image");
        fileChooser.setInitialFileName("performance_report.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            WritableImage image = table.snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("✅ Exported PNG to: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("❌ PNG Export Failed: " + e.getMessage());
            }
        }
    }
}
