package cz.habrondrej.garden.model.categories;

public abstract class Category {

    public static final String TYPE_GROUP = "group";
    public static final String TYPE_PLACE = "place";
    public static final String TYPE_SPECIES = "species";
    public static final String TYPE_TYPE = "type";

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
