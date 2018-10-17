package application;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// This is Ticket class created specifically for insertion data about tickets into Table View
// I got it from Oracle website and just change parameters
public class Ticket {
	
	private StringProperty userId;
    public final void setUserId(String value) { userIdProperty().set(value); }
    public String getUserId() { return userIdProperty().get(); }
    public StringProperty userIdProperty() { 
        if (userId == null) userId = new SimpleStringProperty(this, "userId");
        return userId; 
    }

    private StringProperty ticketId;
    public final void setTicketId(String value) { ticketIdProperty().set(value); }
    public String getTicketId() { return ticketIdProperty().get(); }
    public StringProperty ticketIdProperty() { 
        if (ticketId == null) ticketId = new SimpleStringProperty(this, "ticketId");
        return ticketId; 
    } 
    
    private StringProperty description;
    public final void setDescription(String value) { descriptionProperty().set(value); }
    public String getDescription() { return descriptionProperty().get(); }
    public StringProperty descriptionProperty() { 
        if (description == null) description = new SimpleStringProperty(this, "description");
        return description; 
    }

    private StringProperty open;
    public final void setOpen(String value) { openProperty().set(value); }
    public String getOpen() { return openProperty().get(); }
    public StringProperty openProperty() { 
        if (open == null) open = new SimpleStringProperty(this, "open");
        return open; 
    } 
    
    private StringProperty close;
    public final void setClose(String value) { closeProperty().set(value); }
    public String getClose() { return closeProperty().get(); }
    public StringProperty closeProperty() { 
        if (close == null) close = new SimpleStringProperty(this, "close");
        return close; 
    }

    private StringProperty status;
    public final void setStatus(String value) { statusProperty().set(value); }
    public String getStatus() { return statusProperty().get(); }
    public StringProperty statusProperty() { 
        if (status == null) status = new SimpleStringProperty(this, "status");
        return status; 
    } 
	
	
	public Ticket(String userId, String ticketId, String description, String open, String close, String status)
	{
		this.userId = new SimpleStringProperty(userId);
		this.ticketId = new SimpleStringProperty(ticketId);
		this.description = new SimpleStringProperty(description);
		this.open = new SimpleStringProperty(open);
		this.close = new SimpleStringProperty(close);
		this.status = new SimpleStringProperty(status);
	}
	
	
}
