package ir.ashilethegreat.payesh.DBhandlers;

public class EducationModal {

    String educationID;
    String planID;
    String ownerID;
    String organID;


    public EducationModal() {}

    public EducationModal(String educationID, String planID, String ownerID, String organID) {
        this.educationID = educationID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getEducationID() {
        return educationID;
    }

    public void setEducationID(String educationID) {
        this.educationID = educationID;
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
