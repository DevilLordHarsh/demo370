/**
 * Class to simulate NotificationSystem, which sends
 * notification to user's email and/or phone
 */
public class NotificationSystem {

    /**
     * Sends notification
     * @param userNameId unique username stored in the database
     * @param ticketId unique ticket id stored in the database
     * @return true if notification is successfully sent to either
     * email address or phone number or both
     */
    public static boolean sendNotification(String userNameId, String ticketId) {
        /* looks up email and phone for this userNameId, and
         send email and/or phone notification providing details
         of the ticket using ticketId which was bought or cancelled.*/
        return true;
    }

}
