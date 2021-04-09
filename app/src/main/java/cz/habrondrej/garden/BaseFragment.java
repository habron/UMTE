package cz.habrondrej.garden;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.MainActivity;
import cz.habrondrej.garden.R;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (!mainActivity.getUser().isAuthenticated()) {
            NavHostFragment.findNavController(this).navigate(R.id.LoginFragment);
        }
    }
}
