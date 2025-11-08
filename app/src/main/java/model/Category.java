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

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getHex() {
        return hex;
    }
}
