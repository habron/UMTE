package cz.habrondrej.garden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;

import cz.habrondrej.garden.database.UserDatabase;
import cz.habrondrej.garden.model.User;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private UserDatabase userDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDatabase = new UserDatabase(getApplicationContext());

        user = userDatabase.getUser();
    }


    public User getUser() {
        return user;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
}