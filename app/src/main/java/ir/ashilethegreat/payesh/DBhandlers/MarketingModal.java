package ir.ashilethegreat.payesh.DBhandlers;

public class MarketingModal {

    String marketingID;
    String planID;
    String ownerID;
    String organID;


    public MarketingModal() {}

    public MarketingModal(String marketingID, String planID, String ownerID, String organID) {
        this.marketingID = marketingID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.organID = organID;
    }

    public String getMarketingID() {
        return marketingID;
    }

    public void setMarketingID(String marketingID) {
        this.marketingID = marketingID;
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
