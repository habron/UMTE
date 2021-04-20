package cz.habrondrej.garden;

import androidx.appcompat.app.AppCompatActivity;

import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.database.UserDatabase;
import cz.habrondrej.garden.database.categories.GroupDatabase;
import cz.habrondrej.garden.database.categories.PlaceDatabase;
import cz.habrondrej.garden.database.categories.SpeciesDatabase;
import cz.habrondrej.garden.database.categories.TypeDatabase;
import cz.habrondrej.garden.model.User;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private GroupDatabase groupDatabase;
    private PlaceDatabase placeDatabase;
    private PlantDatabase plantDatabase;
    private SpeciesDatabase speciesDatabase;
    private TypeDatabase typeDatabase;
    private UserDatabase userDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groupDatabase = new GroupDatabase(getApplicationContext());
        placeDatabase = new PlaceDatabase(getApplicationContext());
        plantDatabase = new PlantDatabase(getApplicationContext());
        speciesDatabase = new SpeciesDatabase(getApplicationContext());
        typeDatabase = new TypeDatabase(getApplicationContext());
        userDatabase = new UserDatabase(getApplicationContext());
        user = userDatabase.getUser();
    }


    public User getUser() {
        return user;
    }

    public GroupDatabase getGroupDatabase() {
        return groupDatabase;
    }

    public PlaceDatabase getPlaceDatabase() {
        return placeDatabase;
    }

    public PlantDatabase getPlantDatabase() {
        return plantDatabase;
    }

    public SpeciesDatabase getSpeciesDatabase() {
        return speciesDatabase;
    }

    public TypeDatabase getTypeDatabase() {
        return typeDatabase;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
}
