package model;

public class User {
    private String id;
    private String name;
    private String email;
    private String profilePicURL; //TO DO
    private String FCMToken;

    private boolean enableNotif;

//    private Role role; (admin feature) TBD

    public User(String id, String name, String email, boolean enableNotif) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enableNotif = enableNotif;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    public boolean isEnableNotif() {
        return enableNotif;
    }

    public void setEnableNotif(boolean enableNotif) {
        this.enableNotif = enableNotif;
    }
}
