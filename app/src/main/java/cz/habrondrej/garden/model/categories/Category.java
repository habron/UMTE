package cz.habrondrej.garden.model.categories;

public abstract class Category {

    private final int id;

    private final String title;


    public Category(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
