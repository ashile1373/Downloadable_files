package ir.ashilethegreat.payesh.DBhandlers;

public class IdentificationModal {

    String identificationID;
    String planID;
    String ownerID;
    String organID;


    public IdentificationModal() {}

    public IdentificationModal(String identificationID, String planID, String ownerID, String organID) {
        this.identificationID = identificationID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getIdentificationID() {
        return identificationID;
    }

    public void setIdentificationID(String identificationID) {
        this.identificationID = identificationID;
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
