package booking.agency.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class AgencyMain extends Application {

    private final static String FXML = "agency.fxml";
    private final static String ICON = "/agency.png";

    private static String queueName = null;
    private static String agencyName = null;

    public static void main(String[] args) {
        if (args.length < 2 ){
            throw new IllegalArgumentException("Arguments are missing. You must provide two arguments: AGENCY_NAME and AGENCY_REQUEST_QUEUE");
        }
        if (args[0] == null){
            throw new IllegalArgumentException("Please provide AGENCY_NAME.");
        }
        if (args[1] == null){
            throw new IllegalArgumentException("Please provide AGENCY_REQUEST_QUEUE.");
        }
        agencyName = args[0];
        queueName =args[1];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        final Logger logger = LoggerFactory.getLogger(getClass());
        primaryStage.setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        URL url  = getClass().getClassLoader().getResource( FXML );
        if (url != null) {


            FXMLLoader loader = new FXMLLoader(url);

            // Create a controller instance

            AgencyController controller = new AgencyController(queueName, agencyName);
            // Set it in the FXMLLoader
            loader.setController(controller);


            Parent root = loader.load();
            primaryStage.setTitle("AGENCY - " + agencyName);

            primaryStage.setOnCloseRequest(new EventHandler<>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON)));
            primaryStage.setScene(new Scene(root, 500, 300));
            primaryStage.show();
            logger.info("Started "+ agencyName +" agency on " + queueName + ".");
        }
        else {
            logger.error("Could not load frame from "+FXML+".");
        }
    }
}
