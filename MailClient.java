import java.lang.Integer;
import java.util.List;
import java.util.ArrayList;

/**
 * A class to model a simple email client. The client is run by a
 * particular user, and sends and retrieves mail via a particular server.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class MailClient
{
    // The server used for sending and receiving.
    private MailServer server;
    // The user running this client.
    private String user;
    
    private List<MailItem> items;

    /**
     * Create a mail client run by user and attached to the given server.
     */
    public MailClient(MailServer server, String user)
    {
        this.server = server;
        this.user = user;
        items = new ArrayList<MailItem>();
    }

    /**
     * Return the next mail item (if any) for this user.
     */
    public MailItem getNextMailItem()
    {
        MailItem item = server.getNextMailItem(user);
        
        if (item != null && esSpam(item)) {
            return null; 
        }
        
        if (item != null) {
            items.add(item);
        }
        
        return item;
    }
    
    /**
     * Print the next mail item (if any) for this user to the text 
     * terminal.
     */
    public void printNextMailItem()
    {
        MailItem item = server.getNextMailItem(user);
        
        if(item == null) {
            System.out.println("No new mail.");
        } else if (esSpam(item)) {
            System.out.println("Mensaje recibido de spam");
        } else {
            item.print();
        }
    }
    
    /**
     * Send the given message to the given recipient via
     * the attached mail server.
     * @param to The intended recipient.
     * @param message The text of the message to be sent.
     */
    public void sendMailItem(String to, String subject, String message)
    {
        MailItem item = new MailItem(user, to, subject, message);
        server.post(item);
    }
    
    public String sendMailItem(String correo, String asunto, String subject, String mensaje) {
        return mensaje;
    }
    
    public int getNumberOfMessageInServer() {
        return server.howManyMailItems(user);
    }
    
    public MailItem getLastReceivedMail() {
        MailItem lastMailItem = new MailItem("","","","");
        
        if (items.isEmpty()) {
            lastMailItem = null;
        } else {
            lastMailItem = items.get(items.size() - 1);
        }
        
        return lastMailItem;
    }
    
    public void receiveAndAutorespond() {
        MailItem receivedItem = server.getNextMailItem(user);
        
        if (receivedItem != null) {
            String message = "Gracias por su mensaje. Le contestare lo antes posible. " + receivedItem.getMessage();
            sendMailItem(receivedItem.getFrom(), "RE: " + receivedItem.getSubject(), message);
        }
    }
    
    public int getStatus() {
        return 1;
    }
    
    private boolean esSpam(MailItem item) {
        String message = item.getMessage();
        String subject = item.getSubject();
        boolean isSpam = false;
        
        if (subject.contains(user)) {
            isSpam = false;
        } else if (message.contains("loteria") || message.contains("viagra")) {
            isSpam = true;
        }
        
        return isSpam;
    }
}
