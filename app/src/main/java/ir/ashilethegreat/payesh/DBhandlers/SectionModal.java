package ir.ashilethegreat.payesh.DBhandlers;

public class SectionModal {
    int sectionID , provinceID , cityID , statisticID;
    String sectionName;

    public SectionModal(int sectionID, int provinceID, int cityID, int statisticID, String sectionName) {
        this.sectionID = sectionID;
        this.provinceID = provinceID;
        this.cityID = cityID;
        this.statisticID = statisticID;
        this.sectionName = sectionName;
    }

    public SectionModal() {
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getStatisticID() {
        return statisticID;
    }

    public void setStatisticID(int statisticID) {
        this.statisticID = statisticID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
