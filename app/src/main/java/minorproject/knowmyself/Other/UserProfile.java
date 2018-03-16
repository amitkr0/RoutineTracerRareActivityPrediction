package minorproject.knowmyself.Other;



public class UserProfile {
    private String name;
    private String email;
    private String password;
    private String userid;
    private String guardemail;
    private String contact;

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }

    public String getGuardemail() {
        return guardemail;
    }

    public void setGuardemail(String guardemail) {
        this.guardemail = guardemail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
