package cz.habrondrej.garden;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.database.PlantDatabase;
import cz.habrondrej.garden.database.categories.GroupDatabase;
import cz.habrondrej.garden.database.categories.PlaceDatabase;
import cz.habrondrej.garden.database.categories.SpeciesDatabase;
import cz.habrondrej.garden.database.categories.TypeDatabase;
import cz.habrondrej.garden.model.Plant;
import cz.habrondrej.garden.model.categories.Category;
import cz.habrondrej.garden.model.categories.Group;
import cz.habrondrej.garden.model.categories.Place;
import cz.habrondrej.garden.model.categories.Species;
import cz.habrondrej.garden.model.categories.Type;
import cz.habrondrej.garden.utils.DateParser;
import cz.habrondrej.garden.utils.ShakeDetector;

public class PlantEditFragment extends BaseFragment {

    private GroupDatabase groupDatabase;
    private PlantDatabase plantDatabase;
    private PlaceDatabase placeDatabase;
    private SpeciesDatabase speciesDatabase;
    private TypeDatabase typeDatabase;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector shakeDetector;

    private EditText et_title, et_date, et_description;
    private Spinner sp_group, sp_place, sp_species, sp_type;

    private int[] groupsIds, placesIds, speciesIds, typesIds;

    private Plant plant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();

        groupDatabase = mainActivity.getGroupDatabase();
        plantDatabase = mainActivity.getPlantDatabase();
        placeDatabase = mainActivity.getPlaceDatabase();
        speciesDatabase = mainActivity.getSpeciesDatabase();
        typeDatabase = mainActivity.getTypeDatabase();

        mSensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(this::handleShakeEvent);

        try {
            int id = getArguments().getInt("plantId");
            plant = plantDatabase.getOneById(id);
        } catch (Exception e) {
            NavHostFragment.findNavController(PlantEditFragment.this)
                    .navigate(R.id.action_PlantEditFragment_to_OverviewFragment);
        }

        return inflater.inflate(R.layout.fragment_plant_edit, container, false);
    }

    private void handleShakeEvent(int count) {
        Bundle bundle = new Bundle();
        bundle.putInt("plantId", plant.getId());

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_PlantEditFragment_to_CameraFragment, bundle);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(plant.getTitle());

        initSpinners(view);

        Button btn_savePlant = view.findViewById(R.id.btn_savePlant);
        AppCompatButton btn_cancelPlant = view.findViewById(R.id.btn_cancelPlant);

        et_title = view.findViewById(R.id.et_title);
        et_date = view.findViewById(R.id.et_date);
        et_description = view.findViewById(R.id.et_description);
        setDefaultValues();

        btn_savePlant.setOnClickListener(v -> {
            String message = "Změny se nepodařilo uložit!";
            if (updatePlant())
                message = "Změny uloženy";

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        btn_cancelPlant.setOnClickListener(v -> NavHostFragment.findNavController(PlantEditFragment.this)
                .navigate(R.id.action_PlantEditFragment_to_OverviewFragment));

    }

    private void setDefaultValues() {
        et_title.setText(plant.getTitle());
        if (plant.getDate() != null) et_date.setText(plant.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        et_description.setText(plant.getDescription());
    }

    private void initSpinners(View view) {
        sp_group = view.findViewById(R.id.sp_group);
        sp_place = view.findViewById(R.id.sp_place);
        sp_species = view.findViewById(R.id.sp_species);
        sp_type = view.findViewById(R.id.sp_type);


        List<? extends Category> groups = groupDatabase.getAll();
        List<? extends Category> places = placeDatabase.getAll();
        List<? extends Category> species = speciesDatabase.getAll();
        List<? extends Category> types = typeDatabase.getAll();

        groupsIds = new int[groups.size() + 1];
        placesIds = new int[places.size() + 1];
        speciesIds = new int[species.size() + 1];
        typesIds = new int[types.size() + 1];

        initSpinner(sp_group, groups, groupsIds);
        initSpinner(sp_place, places, placesIds);
        initSpinner(sp_species, species, speciesIds);
        initSpinner(sp_type, types, typesIds);
    }

    private void initSpinner(Spinner spinner, List<? extends Category> categories, int[] ids) {
        String[] items = new String[categories.size() + 1];
        items[0] = "";
        ids[0] = -1;

        int selectedPos = 0;
        int i = 1;
        for (Category category : categories) {
            items[i] = category.getTitle();
            ids[i] = category.getId();

            if (isSpinnerDefaultVal(category))
                selectedPos = i;

            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedPos);
    }

    private boolean isSpinnerDefaultVal(Category category) {
        if (category instanceof Group) {
            if (plant.getGroup() != null)
                return category.getId() == plant.getGroup().getId();
            return false;
        }

        if (category instanceof Place) {
            if (plant.getPlace() != null)
                return category.getId() == plant.getPlace().getId();
            return false;
        }

        if (category instanceof Species) {
            if (plant.getSpecies() != null)
                return category.getId() == plant.getSpecies().getId();
            return false;
        }

        if (category instanceof Type) {
            if (plant.getType() != null)
                return category.getId() == plant.getType().getId();
            return false;
        }

        return false;
    }

    private boolean updatePlant() {

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

        Group group = null;
        Place place = null;
        Species species = null;
        Type type = null;

        if (sp_group.getSelectedItemPosition() > 0) {
            try {
                group = groupDatabase.getOneById(groupsIds[sp_group.getSelectedItemPosition()]);
            } catch (Exception ignored) {}
        }

        if (sp_place.getSelectedItemPosition() > 0) {
            try {
                place = placeDatabase.getOneById(placesIds[sp_place.getSelectedItemPosition()]);
            } catch (Exception ignored) {}
        }

        if (sp_species.getSelectedItemPosition() > 0) {
            try {
                species = speciesDatabase.getOneById(speciesIds[sp_species.getSelectedItemPosition()]);
            } catch (Exception ignored) {}
        }

        if (sp_type.getSelectedItemPosition() > 0) {
            try {
                type = typeDatabase.getOneById(typesIds[sp_type.getSelectedItemPosition()]);
            } catch (Exception ignored) {}
        }

        return plantDatabase.update(new Plant(plant.getId(), et_title.getText().toString(), date, et_description.getText().toString(), group, place, species, type, plant.isArchive()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(shakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(shakeDetector);
    }
}
