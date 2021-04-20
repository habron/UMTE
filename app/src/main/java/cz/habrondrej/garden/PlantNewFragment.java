package cz.habrondrej.garden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.model.Plant;
import cz.habrondrej.garden.model.categories.Category;
import cz.habrondrej.garden.utils.DateParser;

public class PlantNewFragment extends BaseFragment {

    private PlantDatabase plantDatabase;
    private EditText et_title, et_date, et_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plant_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Přidat rostlinu");

        plantDatabase = ((MainActivity) getActivity()).getPlantDatabase();

        handleCategoryButtons(view);

        Button btn_addPlant = view.findViewById(R.id.btn_addPlant);

        et_title = view.findViewById(R.id.et_title);
        et_date = view.findViewById(R.id.et_date);
        et_description = view.findViewById(R.id.et_description);

        btn_addPlant.setOnClickListener(v -> {
            if (addPlant())
                NavHostFragment.findNavController(PlantNewFragment.this)
                    .navigate(R.id.action_PlantNewFragment_to_OverviewFragment);
        });


    }

    private boolean addPlant() {

        if (et_title.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Nejsou vyplněny všechny potřebné údaje!", Toast.LENGTH_SHORT).show();
            return false;
        }

        LocalDate date;
        try {
            date = DateParser.parseDate(et_date.getText().toString());
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), "Nesprávný formát data!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return plantDatabase.create(new Plant(-1, et_title.getText().toString(), date, et_description.getText().toString()));
    }

    private void handleCategoryButtons(View view) {
        Button btn_addGroup = view.findViewById(R.id.btn_addGroup);
        Button btn_addPlace = view.findViewById(R.id.btn_addPlace);
        Button btn_addSpecies = view.findViewById(R.id.btn_addSpecies);
        Button btn_addType = view.findViewById(R.id.btn_addType);
        Bundle bundle = new Bundle();

        btn_addGroup.setOnClickListener(v -> {
            bundle.putString("type", Category.TYPE_GROUP);
            NavHostFragment.findNavController(PlantNewFragment.this)
                    .navigate(R.id.action_PlantNewFragment_to_CategoryNewFragment, bundle);
        });

        btn_addPlace.setOnClickListener(v -> {
            bundle.putString("type", Category.TYPE_PLACE);
            NavHostFragment.findNavController(PlantNewFragment.this)
                    .navigate(R.id.action_PlantNewFragment_to_CategoryNewFragment, bundle);
        });

        btn_addSpecies.setOnClickListener(v -> {
            bundle.putString("type", Category.TYPE_SPECIES);
            NavHostFragment.findNavController(PlantNewFragment.this)
                    .navigate(R.id.action_PlantNewFragment_to_CategoryNewFragment, bundle);
        });

        btn_addType.setOnClickListener(v -> {
            bundle.putString("type", Category.TYPE_TYPE);
            NavHostFragment.findNavController(PlantNewFragment.this)
                    .navigate(R.id.action_PlantNewFragment_to_CategoryNewFragment, bundle);
        });
    }
}
