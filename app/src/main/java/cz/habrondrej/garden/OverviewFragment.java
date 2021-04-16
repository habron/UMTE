package cz.habrondrej.garden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.model.Plant;

public class OverviewFragment extends BaseFragment {

    private PlantDatabase plantDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plantDatabase = new PlantDatabase(requireContext());
        List<Plant> plants = plantDatabase.getAll();
    }
}
