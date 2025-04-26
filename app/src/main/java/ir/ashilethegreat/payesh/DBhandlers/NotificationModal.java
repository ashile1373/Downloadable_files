package ir.ashilethegreat.payesh.DBhandlers;

public class NotificationModal {

    String notificationID;
    String planID;
    String ownerID;
    String organID;


    public NotificationModal() {}

    public NotificationModal(String notificationID, String planID, String ownerID, String organID) {
        this.notificationID = notificationID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getOrganID() {
        return organID;
    }

    public void setOrganID(String organID) {
        this.organID = organID;
    }
}
