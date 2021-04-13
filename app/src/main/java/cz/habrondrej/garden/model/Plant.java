package cz.habrondrej.garden.model;

import androidx.annotation.Nullable;

import java.util.Date;

import cz.habrondrej.garden.model.categories.Group;
import cz.habrondrej.garden.model.categories.Place;
import cz.habrondrej.garden.model.categories.Species;
import cz.habrondrej.garden.model.categories.Type;

public class Plant {

    private final int id;

    private final String title;

    private final Date date;

    private final String description;

    private Group group;

    private Place place;

    private Species species;

    private Type type;


    public Plant(int id, String title, Date date, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public Plant(int id, String title, Date date, String description, @Nullable Group group, @Nullable Place place, @Nullable Species species, @Nullable Type type) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.group = group;
        this.place = place;
        this.species = species;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Group getGroup() {
        return group;
    }

    public Place getPlace() {
        return place;
    }

    public Species getSpecies() {
        return species;
    }

    public Type getType() {
        return type;
    }
}
