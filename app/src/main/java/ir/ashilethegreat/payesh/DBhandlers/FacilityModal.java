package ir.ashilethegreat.payesh.DBhandlers;

public class FacilityModal {

    String financeID;
    String planID;
    String ownerID;
    String fixedFacilityAmount;
    String contributedFacilitiesAmount;
    String shareFacilitiesAmount;
    String totalFacilitiesAmount;
    String workingFacilitiesAmount;
    String supplyFacilitiesID;
    String dateFacilities;
    String fixedBankID;
    String workingBankID;
    String organID;

    public FacilityModal() {}

    public FacilityModal(String financeID, String planID, String ownerID, String fixedFacilityAmount, String contributedFacilitiesAmount,
                         String shareFacilitiesAmount, String totalFacilitiesAmount, String workingFacilitiesAmount, String supplyFacilitiesID,
                         String dateFacilities, String fixedBankID, String workingBankID, String organID) {
        this.financeID = financeID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.fixedFacilityAmount = fixedFacilityAmount;
        this.contributedFacilitiesAmount = contributedFacilitiesAmount;
        this.shareFacilitiesAmount = shareFacilitiesAmount;
        this.totalFacilitiesAmount = totalFacilitiesAmount;
        this.workingFacilitiesAmount = workingFacilitiesAmount;
        this.supplyFacilitiesID = supplyFacilitiesID;
        this.dateFacilities = dateFacilities;
        this.fixedBankID = fixedBankID;
        this.workingBankID = workingBankID;
        this.organID = organID;
    }

    public String getFinanceID() {
        return financeID;
    }

    public void setFinanceID(String financeID) {
        this.financeID = financeID;
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

    public String getFixedFacilityAmount() {
        return fixedFacilityAmount;
    }

    public void setFixedFacilityAmount(String fixedFacilityAmount) {
        this.fixedFacilityAmount = fixedFacilityAmount;
    }

    public String getContributedFacilitiesAmount() {
        return contributedFacilitiesAmount;
    }

    public void setContributedFacilitiesAmount(String contributedFacilitiesAmount) {
        this.contributedFacilitiesAmount = contributedFacilitiesAmount;
    }

    public String getShareFacilitiesAmount() {
        return shareFacilitiesAmount;
    }

    public void setShareFacilitiesAmount(String shareFacilitiesAmount) {
        this.shareFacilitiesAmount = shareFacilitiesAmount;
    }

    public String getTotalFacilitiesAmount() {
        return totalFacilitiesAmount;
    }

    public void setTotalFacilitiesAmount(String totalFacilitiesAmount) {
        this.totalFacilitiesAmount = totalFacilitiesAmount;
    }

    public String getWorkingFacilitiesAmount() {
        return workingFacilitiesAmount;
    }

    public void setWorkingFacilitiesAmount(String workingFacilitiesAmount) {
        this.workingFacilitiesAmount = workingFacilitiesAmount;
    }

    public String getSupplyFacilitiesID() {
        return supplyFacilitiesID;
    }

    public void setSupplyFacilitiesID(String supplyFacilitiesID) {
        this.supplyFacilitiesID = supplyFacilitiesID;
    }

    public String getDateFacilities() {
        return dateFacilities;
    }

    public void setDateFacilities(String dateFacilities) {
        this.dateFacilities = dateFacilities;
    }

    public String getFixedBankID() {
        return fixedBankID;
    }

    public void setFixedBankID(String fixedBankID) {
        this.fixedBankID = fixedBankID;
    }

    public String getWorkingBankID() {
        return workingBankID;
    }

    public void setWorkingBankID(String workingBankID) {
        this.workingBankID = workingBankID;
    }

    public String getOrganID() {
        return organID;
    }

    public void setOrganID(String organID) {
        this.organID = organID;
    }
}
