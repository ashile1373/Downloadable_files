package ir.ashilethegreat.payesh.DBhandlers;

public class CityModal {
    int cityID, provinceID, statisticCode;
    String cityName;

    public CityModal(int cityID, int provinceID, int statisticCode, String cityName) {
        this.cityID = cityID;
        this.provinceID = provinceID;
        this.statisticCode = statisticCode;
        this.cityName = cityName;
    }

    public CityModal() {
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getStatisticCode() {
        return statisticCode;
    }

    public void setStatisticCode(int statisticCode) {
        this.statisticCode = statisticCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
