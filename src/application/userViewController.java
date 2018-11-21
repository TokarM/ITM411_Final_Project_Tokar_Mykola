package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class userViewController extends Application {
	
	//FXML variable;
	@FXML
    private TextArea ticketDescTxt;

    @FXML
    private Button createBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    private Label userID_txt = new Label();
    
    //FXML table columns variables assigned to class Tickets
    // I did it in order to display existent tickets from database in JavaFX TableView
    @FXML
    private TableColumn<Ticket, String> colmTicketId = new TableColumn<Ticket,String>();
    @FXML
    private TableColumn<Ticket, String> colmUserId = new TableColumn<Ticket,String>();
    @FXML
    private TableColumn<Ticket, String> colmDescription = new TableColumn<Ticket,String>();
    @FXML
    private TableColumn<Ticket, String> colmTimeCreated = new TableColumn<Ticket,String>();
    @FXML
    private TableColumn<Ticket, String> colmTimeClosed = new TableColumn<Ticket,String>();
    @FXML
    private TableColumn<Ticket, String> colmStatus = new TableColumn<Ticket,String>();
    @FXML
    private TableView<Ticket> userTable = new TableView<Ticket>();
    
    //Populate table with tickets before start
    @FXML
    public void initialize() {
    	
        populateTable();
        userID_txt.setText(Main.usernameRecord);
        
    }
    
    //Current date for created ticket
    java.util.Date date=new java.util.Date();
    java.sql.Date sqlDate=new java.sql.Date(date.getTime());
    
    //Default closed date for opened tickets
    String date1 = "2000-01-01";
    java.sql.Date sqlDate1 = java.sql.Date.valueOf(date1);
    
    //This is special type of list which is work with TableView in JavaFX
    protected static ObservableList<Ticket> data;
	
	//Upload and run JavaFX view
    @Override
	public void start(Stage arg0) throws Exception {
		try {
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserView.fxml"));
	                Parent root1 = (Parent) fxmlLoader.load();
	                populateTable();
	                Stage stage = arg0;
					stage.setScene(new Scene(root1));
					stage.setTitle("Welcome User");
	                stage.show();
	        } catch(Exception e) {
	           e.printStackTrace();
	          }
			
	}
	//Method to populate the table
	public void populateTable() {
		
		try {
			Connection conn = Dao.getConnection();
			data = FXCollections.observableArrayList();
			
			// I grab data about one ticket from two different tables
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mtokar_tickets");
			ResultSet rs1 = conn.createStatement().executeQuery("SELECT * FROM mtokar_tickets_status1");
			
			//Filling up my list
			while(rs.next() && rs1.next())
			{
				data.add(new Ticket(rs.getString(2),rs.getString(1), rs.getString(3), rs1.getString(2),rs1.getString(3), rs1.getString(4)));
			}
			
			//Push items to the table
			userTable.setItems(data);
					
			//This is specific for JavaFX declaration of which column should consist which item.
			
			colmUserId.setCellValueFactory(new PropertyValueFactory<Ticket,String>("userId"));
			colmTicketId.setCellValueFactory(new PropertyValueFactory<Ticket,String>("ticketId"));
			colmDescription.setCellValueFactory(new PropertyValueFactory<Ticket,String>("description"));
			colmTimeCreated.setCellValueFactory(new PropertyValueFactory<Ticket,String>("open"));
			colmTimeClosed.setCellValueFactory(new PropertyValueFactory<Ticket,String>("close"));
			colmStatus.setCellValueFactory(new PropertyValueFactory<Ticket,String>("status"));
			
			userTable.setVisible(true);
			
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}
	
    //This is my method to create a ticket
    @FXML
    private void handleCreateTicket(ActionEvent event) throws Exception {
    	
    	
		String description = ticketDescTxt.getText();			
		String sql;
		String open = "Opened";
		
		Dao.statement = Dao.getConnection().createStatement();
		
		//Check for empty text field or null
		if(ticketDescTxt.getText().equals("") || ticketDescTxt.getText().equals(null))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setContentText("Please provide ticket description");
			
			alert.showAndWait();
		}
		else
		{
			description = ticketDescTxt.getText();
		//Insert one part of data about tickets to one database	
			sql = "insert into mtokar_tickets(ticket_issuer, ticket_description) " + "values('" + Main.usernameRecord 
					+ "', '" + description + "');";
		//Insert second part of data to another database	
			PreparedStatement ps= (PreparedStatement) Dao.getConnection().prepareStatement("insert into mtokar_tickets_status1 (time_opened,time_closed,status) values(?,?,?)");
	        ps.setDate(1,sqlDate);
	        ps.setDate(2,sqlDate1);
	        ps.setString(3, open);
	        ps.executeUpdate();
	        
	        Dao.statement.execute(sql);
	        
	        Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setContentText("Ticket Created");

			alert.showAndWait();
		}
		Dao.connect.close();
		
		populateTable();
	}
    
    // Exit button
    @FXML
    private void handleExitBtn(ActionEvent event) throws Exception {
	    System.exit(0);
	}

}


