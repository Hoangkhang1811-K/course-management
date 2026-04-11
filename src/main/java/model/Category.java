package model;

public class Category {
    private int category_id;
    private String name;
    private String description;
    private int status;

    public Category() {
    }

    public Category(int category_id, String name, String description, int status) {
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Category(String name, String description, int status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + category_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}