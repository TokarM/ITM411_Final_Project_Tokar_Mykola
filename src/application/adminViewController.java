package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class adminViewController extends Application {

	 	// FXML variables
		@FXML
	    private Button exitBtn;
	 	
	 	@FXML
	    private Button deleteBtn;
	
	 	@FXML
	    private Button closeBtn;
	 	
	 	@FXML
	    private Button readBtn;
	 	
	 	@FXML
	    private TextField deleteTxt;
	 	
	 	@FXML
	    private TextField closeTxt;
	
	 	@FXML
	    private TextField readTxt;
	 	
	 	@FXML
	    private TextArea descriptionTxt;
	 	
		// I had to establish the same populate method in this class too, since I did not find a way how to use it from user view
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
	    
	    //This are date for "close" button method
	    java.util.Date date=new java.util.Date();
	    java.sql.Date sqlDate=new java.sql.Date(date.getTime());
	    
	    ObservableList<Ticket> data;
	    
	    // PopUp method 
	    public void alertMethod( String message)
	   {
	    Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setContentText(message);
		alert.showAndWait();
	   }
	
	//Populate table before view showed up
	@FXML
    public void initialize() {
    	populateTable(); 	
    }
	
	@Override
	public void start(Stage arg0) throws Exception {
		try {
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
	                Parent root1 = (Parent) fxmlLoader.load();
	                Stage stage = arg0;
					stage.setScene(new Scene(root1));  
					stage.setTitle("Welcome Admin");
	                stage.show();
	        } catch(Exception e) {
	           e.printStackTrace();
	          }
	}
	
// The same method as in userView class.
//I described it step-by-step in userView class
public void populateTable() {
		
		try {
			Connection conn = Dao.getConnection();
			data = FXCollections.observableArrayList();
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mtokar_tickets");
			ResultSet rs1 = conn.createStatement().executeQuery("SELECT * FROM mtokar_tickets_status1");
	
			while(rs.next() && rs1.next())
			{
				data.add(new Ticket(rs.getString(2),rs.getString(1), rs.getString(3), rs1.getString(2), rs1.getString(3), rs1.getString(4)));		
			}
			userTable.setItems(data);
					
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

	//This is my delete button
	 @FXML
		private void handleDeleteBtn(ActionEvent event) throws Exception
		{
		 	
		 //Grab text from text field  and use REGEX in order to check it is it an integer
		 	String text = deleteTxt.getText();
		 	if (text.matches("-?\\d+"))
		 	{
		 	int i = Integer.parseInt(text);
		 	
		 	Connection conn = Dao.getConnection();
		 
		 	  //Delete from databases
		 	  String query = "delete from mtokar_tickets_status1 where uid = ?";
		      PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(query);
		      preparedStmt.setInt(1, i);
		      preparedStmt.execute();
			
		      String query1 = "delete from mtokar_tickets where ticket_id = ?";
		      preparedStmt = (PreparedStatement) conn.prepareStatement(query1);
		      preparedStmt.setInt(1, i);
		      preparedStmt.execute();
			
		      conn.close();
			populateTable();
			
			alertMethod("Ticket was deleted successfully!");		 	
			}
			else
		 	{
		 	alertMethod("Please provide a valid TicketId");
		 	}
		}
	 //This is my close button
	 @FXML
		private void handleCloseBtn(ActionEvent event) throws Exception
		{
		 	
		 	// I used the same logic as in delete button except I change the value only in one database
		 	String text = closeTxt.getText();
		 	if (text.matches("-?\\d+"))
		 	{
		    Connection conn = Dao.getConnection();
		 	String close = "Closed";
		 	
			int i = Integer.parseInt(text);
		 	String query = "update mtokar_tickets_status1 set status = '" + close +"', time_closed = '" + sqlDate + "' where uid = ?";
		    PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(query);
		    preparedStmt.setInt(1, i);
		    preparedStmt.execute();
		    
		    alertMethod("Ticket was closed successfully");
		
		    conn.close();
			
			populateTable();
		 	}
		 	else
		 	{
		 		alertMethod("Please provide a valid TicketId");
		 	}
		}
	 
	
	 //This is my read button which show up the full ticket description in textArea box
	 @FXML
		private void handleReadBtn(ActionEvent event) throws Exception
		{
		 	
		 	//I used the following logic
		 	// Grab text, check if it is a valid value, then select specific description from database and insert it to the textArea.
		 	String text = readTxt.getText();
		 	String description = "";
		 	if (text.matches("d+"))
		 	{
		 		Connection conn = Dao.getConnection();
			 	
			    ResultSet rs;
			 	
				int i = Integer.parseInt(text);
			 	String query = "select ticket_description from mtokar_tickets where ticket_id = '" + i +"'";
			    PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(query);
			    rs =  preparedStmt.executeQuery(query);
			    while(rs.next())
			    {
			    description = rs.getString(1);
			    }
			    descriptionTxt.setText(description);
			    conn.close();
				
				populateTable(); 	
		 		
		 	}
		 	else
		 	{
		 		alertMethod("Please provide a valid TicketId");
		 	}
		}
	 //Exit button
	 @FXML
	    private void handleExitBtn(ActionEvent event) throws Exception {
		    System.exit(0);
		}
	
}
