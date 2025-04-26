package ir.ashilethegreat.payesh.DBhandlers;

public class PlanModal {
    // PLAN OWNER INFO
    String planID;
    String userID;
    String name;
    String familyName;
    String parentName;
    String natCode;
    String phone;
    String fixedPhone;
    String birthday;
    String planStartDate;
    String education;
    String fieldOfStudy;
    String specialization;
    String households;
    //PLAN DETAILS INFO
    String planTitle;
    String planEconomicSection;
    String planSituation;
    String province;
    String township;
    String section;
    String city;
    String region;
    String mainStreet;
    String subStreet;
    String alley;
    String code;
    String postalCode;
    String moreAddress;
    int addressUpdated;
    //PLAN ECO SYSTEM REQUIREMENT
    String planEchoSystemLicence;
    String planEchoSystemFinance;
    String planEchoSystemCounseling;
    String planEchoSystemMarketing;
    String planEchoSystemEducation;
    String planEchoSystemProvidingTechnology;
    String planEchoSystemNotices;
    String planEchoSystemProvidingInfrastructure;
    String planEchoSystemIdentification;
    String planEchoSystemCultivation;
    int isCompleted;
    int whichStep;
    String employerID;
    int statusRegistration;
    int planFinalSuperVisionSituation;
    String userDescription;
    String planPinnedPosition;
    int planPinned;

    public PlanModal() {
    }

    public PlanModal(String planID, String userID, String name, String familyName, String parentName, String natCode, String phone, String fixedPhone, String birthday,
                     String planStartDate, String education, String fieldOfStudy, String specialization, String households, String planTitle, String planEconomicSection,
                     String planSituation, String province, String township, String section, String city, String region, String mainStreet,
                     String subStreet, String alley, String code, String postalCode, String moreAddress, int addressUpdated, String planEchoSystemLicence,
                     String planEchoSystemFinance, String planEchoSystemCounseling, String planEchoSystemMarketing, String planEchoSystemEducation,
                     String planEchoSystemProvidingTechnology, String planEchoSystemNotices, String planEchoSystemProvidingInfrastructure,
                     String planEchoSystemIdentification, String planEchoSystemCultivation, int isCompleted, int whichStep, String employerID,
                     int statusRegistration, int planFinalSuperVisionSituation, String userDescription, String planPinnedPosition, int planPinned) {
        this.planID = planID;
        this.userID = userID;
        this.name = name;
        this.familyName = familyName;
        this.parentName = parentName;
        this.natCode = natCode;
        this.phone = phone;
        this.fixedPhone = fixedPhone;
        this.birthday = birthday;
        this.planStartDate = planStartDate;
        this.education = education;
        this.fieldOfStudy = fieldOfStudy;
        this.specialization = specialization;
        this.households = households;
        this.planTitle = planTitle;
        this.planEconomicSection = planEconomicSection;
        this.planSituation = planSituation;
        this.province = province;
        this.township = township;
        this.section = section;
        this.city = city;
        this.region = region;
        this.mainStreet = mainStreet;
        this.subStreet = subStreet;
        this.alley = alley;
        this.code = code;
        this.postalCode = postalCode;
        this.moreAddress = moreAddress;
        this.addressUpdated = addressUpdated;
        this.planEchoSystemLicence = planEchoSystemLicence;
        this.planEchoSystemFinance = planEchoSystemFinance;
        this.planEchoSystemCounseling = planEchoSystemCounseling;
        this.planEchoSystemMarketing = planEchoSystemMarketing;
        this.planEchoSystemEducation = planEchoSystemEducation;
        this.planEchoSystemProvidingTechnology = planEchoSystemProvidingTechnology;
        this.planEchoSystemNotices = planEchoSystemNotices;
        this.planEchoSystemProvidingInfrastructure = planEchoSystemProvidingInfrastructure;
        this.planEchoSystemIdentification = planEchoSystemIdentification;
        this.planEchoSystemCultivation = planEchoSystemCultivation;
        this.isCompleted = isCompleted;
        this.whichStep = whichStep;
        this.employerID = employerID;
        this.statusRegistration = statusRegistration;
        this.planFinalSuperVisionSituation = planFinalSuperVisionSituation;
        this.userDescription = userDescription;
        this.planPinnedPosition = planPinnedPosition;
        this.planPinned = planPinned;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getNatCode() {
        return natCode;
    }

    public void setNatCode(String natCode) {
        this.natCode = natCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getHouseholds() {
        return households;
    }

    public void setHouseholds(String households) {
        this.households = households;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanEconomicSection() {
        return planEconomicSection;
    }

    public void setPlanEconomicSection(String planEconomicSection) {
        this.planEconomicSection = planEconomicSection;
    }

    public String getPlanSituation() {
        return planSituation;
    }

    public void setPlanSituation(String planSituation) {
        this.planSituation = planSituation;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMainStreet() {
        return mainStreet;
    }

    public void setMainStreet(String mainStreet) {
        this.mainStreet = mainStreet;
    }

    public String getSubStreet() {
        return subStreet;
    }

    public void setSubStreet(String subStreet) {
        this.subStreet = subStreet;
    }

    public String getAlley() {
        return alley;
    }

    public void setAlley(String alley) {
        this.alley = alley;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMoreAddress() {
        return moreAddress;
    }

    public void setMoreAddress(String moreAddress) {
        this.moreAddress = moreAddress;
    }

    public int getAddressUpdated() {
        return addressUpdated;
    }

    public void setAddressUpdated(int addressUpdated) {
        this.addressUpdated = addressUpdated;
    }

    public String getPlanEchoSystemLicence() {
        return planEchoSystemLicence;
    }

    public void setPlanEchoSystemLicence(String planEchoSystemLicence) {
        this.planEchoSystemLicence = planEchoSystemLicence;
    }

    public String getPlanEchoSystemFinance() {
        return planEchoSystemFinance;
    }

    public void setPlanEchoSystemFinance(String planEchoSystemFinance) {
        this.planEchoSystemFinance = planEchoSystemFinance;
    }

    public String getPlanEchoSystemCounseling() {
        return planEchoSystemCounseling;
    }

    public void setPlanEchoSystemCounseling(String planEchoSystemCounseling) {
        this.planEchoSystemCounseling = planEchoSystemCounseling;
    }

    public String getPlanEchoSystemMarketing() {
        return planEchoSystemMarketing;
    }

    public void setPlanEchoSystemMarketing(String planEchoSystemMarketing) {
        this.planEchoSystemMarketing = planEchoSystemMarketing;
    }

    public String getPlanEchoSystemEducation() {
        return planEchoSystemEducation;
    }

    public void setPlanEchoSystemEducation(String planEchoSystemEducation) {
        this.planEchoSystemEducation = planEchoSystemEducation;
    }

    public String getPlanEchoSystemProvidingTechnology() {
        return planEchoSystemProvidingTechnology;
    }

    public void setPlanEchoSystemProvidingTechnology(String planEchoSystemProvidingTechnology) {
        this.planEchoSystemProvidingTechnology = planEchoSystemProvidingTechnology;
    }

    public String getPlanEchoSystemNotices() {
        return planEchoSystemNotices;
    }

    public void setPlanEchoSystemNotices(String planEchoSystemNotices) {
        this.planEchoSystemNotices = planEchoSystemNotices;
    }

    public String getPlanEchoSystemProvidingInfrastructure() {
        return planEchoSystemProvidingInfrastructure;
    }

    public void setPlanEchoSystemProvidingInfrastructure(String planEchoSystemProvidingInfrastructure) {
        this.planEchoSystemProvidingInfrastructure = planEchoSystemProvidingInfrastructure;
    }

    public String getPlanEchoSystemIdentification() {
        return planEchoSystemIdentification;
    }

    public void setPlanEchoSystemIdentification(String planEchoSystemIdentification) {
        this.planEchoSystemIdentification = planEchoSystemIdentification;
    }

    public String getPlanEchoSystemCultivation() {
        return planEchoSystemCultivation;
    }

    public void setPlanEchoSystemCultivation(String planEchoSystemCultivation) {
        this.planEchoSystemCultivation = planEchoSystemCultivation;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getWhichStep() {
        return whichStep;
    }

    public void setWhichStep(int whichStep) {
        this.whichStep = whichStep;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public int getStatusRegistration() {
        return statusRegistration;
    }

    public void setStatusRegistration(int statusRegistration) {
        this.statusRegistration = statusRegistration;
    }

    public int getPlanFinalSuperVisionSituation() {
        return planFinalSuperVisionSituation;
    }

    public void setPlanFinalSuperVisionSituation(int planFinalSuperVisionSituation) {
        this.planFinalSuperVisionSituation = planFinalSuperVisionSituation;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getPlanPinnedPosition() {
        return planPinnedPosition;
    }

    public void setPlanPinnedPosition(String planPinnedPosition) {
        this.planPinnedPosition = planPinnedPosition;
    }

    public int getPlanPinned() {
        return planPinned;
    }

    public void setPlanPinned(int planPinned) {
        this.planPinned = planPinned;
    }
}
