public class User {

    private String userName;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String[] ticketIds;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    User(String name, String fname, String password, String email, String phone) {
        this.userName = name;
        this.fullName = fname;
        this. password = password;
        this.email = email;
        this.phone = phone;
    }

}
