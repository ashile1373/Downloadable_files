package ir.ashilethegreat.payesh.DBhandlers;

public class UserModal {

    String tableID;
    String id;
    String token;
    String username;
    String password;
    String firstName;
    String lastName;
    String phone;
    int loginMode;
    int userType;
    String pic;

    public UserModal(String tableID, String id, String token, String username, String password, String firstName, String lastName, String phone, int loginMode, int userType) {
        this.tableID = tableID;
        this.id = id;
        this.token = token;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.loginMode = loginMode;
        this.userType = userType;
    }

    public UserModal(String tableID, String id, String token, String username, String password, String firstName, String lastName, String phone, int loginMode, int userType, String pic) {
        this.tableID = tableID;
        this.id = id;
        this.token = token;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.loginMode = loginMode;
        this.userType = userType;
        this.pic = pic;
    }

    public UserModal() {}

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public int getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(int loginMode) {
        this.loginMode = loginMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
