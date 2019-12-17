package booking.client.gui;

import booking.client.model.ClientBookingReply;
import booking.client.model.ClientBookingRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;

public class ClientController implements Initializable {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TextField tfOrigin;
    @FXML
    private TextField tfDestination;
    @FXML
    private TextField tfNrPassangers;
    @FXML
    private TextField tfCustomerId;
    @FXML
    private CheckBox cbCustomerId;
    @FXML
    DatePicker dpDate;
    @FXML
    private ListView<ClientListViewLine> lvLoanRequestReply;


    public ClientController(){

    }

    @FXML
    public void btnSendLoanRequestClicked(){
        try {
        // create the ClientBookingRequest
        String id =  UUID.randomUUID().toString();
        String fromAirport = tfOrigin.getText();
        String toAirport = tfDestination.getText();

        int nrTravellers = Integer.parseInt(this.tfNrPassangers.getText());

        LocalDate date = dpDate.getValue();
        if (fromAirport.isEmpty() || toAirport.isEmpty() || nrTravellers < 1 || date.isBefore(LocalDate.now())){
            showErrorMessageDialog("All fields must be filled in.\nNumber of travellers must be at least 1. \nDate cannot be in the past.");
        } else {

            ClientBookingRequest request;
            if (cbCustomerId.isSelected()) {
                int customerID = Integer.parseInt(tfCustomerId.getText());
                request = new ClientBookingRequest(id, fromAirport, toAirport, nrTravellers, customerID);
            } else {
                request = new ClientBookingRequest(id, fromAirport, toAirport, date, nrTravellers);
            }
            //create the ListViewLine line with the request and add it to lvLoanRequestReply
            ClientListViewLine listViewLine = new ClientListViewLine(request);
            this.lvLoanRequestReply.getItems().add(listViewLine);

             // TODO: Send teh booking request
            logger.info("Send here the booking request: " + request);
        }
        } catch (NumberFormatException e){
            showErrorMessageDialog("All fields must be filled in.\nNumber of travellers must be at least 1. \nDate cannot be in the past.");
        }
    }

    private void showErrorMessageDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Booking Client");
        alert.setHeaderText("Error occurred while sending booking request.");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void cbCustomerIDClicked(){
       tfCustomerId.setDisable(!cbCustomerId.isSelected());
    }


    /**
     * This method returns the line of lvMessages which contains the given loan request.
     * @param request BankInterestRequest for which the line of lvMessages should be found and returned
     * @return The ListViewLine line of lvMessages which contains the given request
     */
    private ClientListViewLine getRequestReply(ClientBookingRequest request) {

        for(ClientListViewLine clientListViewLine: lvLoanRequestReply.getItems()){
            if (clientListViewLine.getRequest() == request) {
                return clientListViewLine;
            }
        }

        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfOrigin.setText("Schiphol");
        tfDestination.setText("Heathrow");
        tfNrPassangers.setText("3");
        cbCustomerId.setSelected(false);
        tfCustomerId.setDisable(true);
        dpDate.setValue(LocalDate.now());
    }
}
