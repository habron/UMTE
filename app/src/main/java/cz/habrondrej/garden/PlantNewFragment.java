package cz.habrondrej.garden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.model.Plant;
import cz.habrondrej.garden.utils.DateParser;

public class PlantNewFragment extends BaseFragment {

    private PlantDatabase plantDatabase;
    private Button btn_addPlant;
    private EditText et_title, et_date, et_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plant_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        plantDatabase = ((MainActivity) getActivity()).getPlantDatabase();

        btn_addPlant = view.findViewById(R.id.btn_addPlant);

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

        if (et_title.getText().toString().isEmpty()) {
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
}
