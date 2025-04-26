package ir.ashilethegreat.payesh.DBhandlers;

public class ProvidingTechnologyModal {

    String providingTechnologyID;
    String planID;
    String ownerID;
    String organID;


    public ProvidingTechnologyModal() {}

    public ProvidingTechnologyModal(String providingTechnologyID, String planID, String ownerID, String organID) {
        this.providingTechnologyID = providingTechnologyID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getProvidingTechnologyID() {
        return providingTechnologyID;
    }

    public void setProvidingTechnologyID(String providingTechnologyID) {
        this.providingTechnologyID = providingTechnologyID;
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
