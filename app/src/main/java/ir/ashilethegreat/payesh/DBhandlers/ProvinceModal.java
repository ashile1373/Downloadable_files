package ir.ashilethegreat.payesh.DBhandlers;

public class ProvinceModal {
    int provinceID;
    String provinceName;

    public ProvinceModal() {
    }

    public ProvinceModal(int provinceID, String provinceName) {
        this.provinceID = provinceID;
        this.provinceName = provinceName;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
