package cz.habrondrej.garden;

import androidx.appcompat.app.AppCompatActivity;

import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.database.UserDatabase;
import cz.habrondrej.garden.model.User;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private PlantDatabase plantDatabase;
    private UserDatabase userDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.plantDatabase = new PlantDatabase(getApplicationContext());
        this.userDatabase = new UserDatabase(getApplicationContext());
        user = userDatabase.getUser();
    }


    public User getUser() {
        return user;
    }

    public PlantDatabase getPlantDatabase() {
        return plantDatabase;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
}
