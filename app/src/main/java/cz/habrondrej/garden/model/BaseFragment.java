package cz.habrondrej.garden.model;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.MainActivity;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.checkAutentication(NavHostFragment.findNavController(this));
    }
}
