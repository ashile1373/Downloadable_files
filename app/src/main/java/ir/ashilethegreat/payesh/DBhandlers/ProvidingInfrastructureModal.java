package ir.ashilethegreat.payesh.DBhandlers;

public class ProvidingInfrastructureModal {

    String providingInfrastructureID;
    String planID;
    String ownerID;
    String organID;


    public ProvidingInfrastructureModal() {}

    public ProvidingInfrastructureModal(String providingInfrastructureID, String planID, String ownerID, String organID) {
        this.providingInfrastructureID = providingInfrastructureID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getProvidingInfrastructureID() {
        return providingInfrastructureID;
    }

    public void setProvidingInfrastructureID(String providingInfrastructureID) {
        this.providingInfrastructureID = providingInfrastructureID;
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
