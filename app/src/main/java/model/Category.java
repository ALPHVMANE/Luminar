package model;

public class Category {
    private int id;
    private String name;
    private String hex;

    public Category(int id, String name, String hex) {
        this.id = id;
        this.name = name;
        this.hex = hex;
    }

    public Category() {

    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getHex() {
        return hex;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setHex(String hex) { this.hex = hex; }
}
