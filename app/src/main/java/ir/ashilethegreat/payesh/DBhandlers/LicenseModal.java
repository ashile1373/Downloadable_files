package ir.ashilethegreat.payesh.DBhandlers;

public class LicenseModal {

    String licenseID;
    String planID;
    String ownerID;
    String licenseNumber;
    String licenseType;
    String licenseDate;
    String organID;

    public LicenseModal() {}

    public LicenseModal(String licenseID, String planID, String ownerID, String licenseNumber, String licenseType, String licenseDate, String organID) {
        this.licenseID = licenseID;
        this.planID = planID;
        this.ownerID = ownerID;
        this.licenseNumber = licenseNumber;
        this.licenseType = licenseType;
        this.licenseDate = licenseDate;
        this.organID = organID;
    }

    public String getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
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

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }
}
