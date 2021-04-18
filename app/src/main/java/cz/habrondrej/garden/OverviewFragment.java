package cz.habrondrej.garden;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.model.Plant;

public class OverviewFragment extends BaseFragment {

    private PlantDatabase plantDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Přehled");

        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_add:
                    NavHostFragment.findNavController(OverviewFragment.this)
                            .navigate(R.id.action_OverviewFragment_to_PlantNewFragment);
                    return true;
                case R.id.nav_settings:
                case R.id.nav_home:
                    return true;
            }
            return false;
        });

        plantDatabase = new PlantDatabase(requireContext());
        List<Plant> plants = plantDatabase.getAll();
    }
}
