// Written by Mykola Tokar
// 4/26/2018
// Final Project

/*
 This is my final project created using JavaFX. CRUD functions were
 implemented. This project has three different views, which are : Login, AdminView and UserView.
 In Login screen you simply provide your credentials. 
 Admin credentials: username: Mykola , password: admin1;
 User credentials: username: MykolaUser, password: user12345, or any other in database.
 In UserView you can only view and create a ticket.
 In AdminView you can Read, Update and Delete any ticket you want. 
*/

package application;
	
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class Main extends Application {
	
	//variable that I used for all classes
	protected static Stage mainStage = new Stage();
	public static Stage stageUser = new Stage();
	public static String usernameRecord;
	
	
	@Override
    public void start(Stage primaryStage) throws Exception{
	    mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        mainStage.setTitle("Login");
        mainStage.setScene(new Scene(root, 350, 215));
        mainStage.show();
    }
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	    
	   // My FXML variables
	
		@FXML
	    private TextField usernameTxt;

	    @FXML
	    private PasswordField passwordTxt;

	    @FXML
	    private Button loginBtn;
	    
	    @FXML
	    private Button exitBtn;
	    
	    
	    @FXML
	    private void handleExitBtn(ActionEvent event) throws Exception {
		    System.exit(0);
		}
	    
	    @FXML
	    private void handleButtonAction(ActionEvent event) throws Exception {
	        // Button was clicked, do something...
	            	
	    	// Create user friendly variable name to store user name from text box
			String userName = usernameTxt.getText();
			// convert characters from password field to string for input validation
			String password = new String(passwordTxt.getText());
			
			// Create user friendly variable name to store user name from text box
			String userName1 = usernameTxt.getText();
			// convert characters from password field to string for input validation
			String password1 = new String(passwordTxt.getText());
			
				Connection connect = Dao.getConnection();
				String queryString = "SELECT uname, upass, admin FROM mtokar_new_users where uname=? and upass=?";
				PreparedStatement ps;
				ResultSet results = null;
				try {
					// set up prepared statements to execute query string cleanly and safely
					ps = (PreparedStatement) connect.prepareStatement(queryString);
					ps.setString(1, userName1);
					ps.setString(2, password1);
					
					results = ps.executeQuery();
					
					if (results.next()) { 
						
						//Check if it is a user
						if(userName.equals(userName1) && password.equals(password1))
						{
							usernameRecord = userName;
							mainStage.close();
								
								if(userName1.equals("Mykola") && password.equals("admin1"))
								{
								JOptionPane.showMessageDialog(null, "Welcome " + userName);
								adminViewController admin = new adminViewController();
								admin.start(stageUser);
								}
								else
								{
									JOptionPane.showMessageDialog(null, "Welcome " + userName);
									userViewController user = new userViewController();
									user.start(stageUser);
								}
							}
						}
					
					else {
							JOptionPane.showMessageDialog(null, "Please Check Username and Password ");
						}
					
					connect.close();
							
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						results.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						connect.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		}
	

