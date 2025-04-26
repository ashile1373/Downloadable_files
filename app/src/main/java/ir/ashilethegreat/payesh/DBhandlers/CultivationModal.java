package ir.ashilethegreat.payesh.DBhandlers;

public class CultivationModal {

    String cultivationID;
    String planID;
    String ownerID;
    String organID;


    public CultivationModal() {}

    public CultivationModal(String cultivationID, String planID, String ownerID, String organID) {
        this.cultivationID = cultivationID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getCultivationID() {
        return cultivationID;
    }

    public void setCultivitionID(String cultivationID) {
        this.cultivationID = cultivationID;
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
