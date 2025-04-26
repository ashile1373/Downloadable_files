package ir.ashilethegreat.payesh.DBhandlers;

public class CounselingModal {

    String counselingID;
    String planID;
    String ownerID;
    String organID;


    public CounselingModal() {}

    public CounselingModal(String counselingID, String planID, String ownerID, String organID) {
        this.counselingID = counselingID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getCounselingID() {
        return counselingID;
    }

    public void setCounselingID(String counselingID) {
        this.counselingID = counselingID;
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
