package cz.habrondrej.garden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import cz.habrondrej.garden.model.User;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();
    }


    public void checkAutentication(NavController navController) {
        if (!user.isAuthenticated()) {
            navController.navigate(R.id.LoginFragment);
        }
    }


    public User getUser() {
        return user;
    }
}