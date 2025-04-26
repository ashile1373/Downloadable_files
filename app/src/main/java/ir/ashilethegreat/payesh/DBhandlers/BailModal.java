package ir.ashilethegreat.payesh.DBhandlers;

public class BailModal {
    String confirmPlanID;
    String confirmPlanBailUserID;
    int confirmBailID;
    String confirmBailName;

    public BailModal() {
    }

    public BailModal(String confirmPlanID, String confirmPlanBailUserID, int confirmBailID, String confirmBailName) {
        this.confirmPlanID = confirmPlanID;
        this.confirmPlanBailUserID = confirmPlanBailUserID;
        this.confirmBailID = confirmBailID;
        this.confirmBailName = confirmBailName;
    }

    public String getConfirmPlanID() {
        return confirmPlanID;
    }

    public void setConfirmPlanID(String confirmPlanID) {
        this.confirmPlanID = confirmPlanID;
    }

    public String getConfirmPlanBailUserID() {
        return confirmPlanBailUserID;
    }

    public void setConfirmPlanBailUserID(String confirmPlanBailUserID) {
        this.confirmPlanBailUserID = confirmPlanBailUserID;
    }

    public int getConfirmBailID() {
        return confirmBailID;
    }

    public void setConfirmBailID(int confirmBailID) {
        this.confirmBailID = confirmBailID;
    }

    public String getConfirmBailName() {
        return confirmBailName;
    }

    public void setConfirmBailName(String confirmBailName) {
        this.confirmBailName = confirmBailName;
    }
}
