package cz.habrondrej.garden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.database.categories.GroupDatabase;
import cz.habrondrej.garden.database.categories.PlaceDatabase;
import cz.habrondrej.garden.database.categories.SpeciesDatabase;
import cz.habrondrej.garden.database.categories.TypeDatabase;
import cz.habrondrej.garden.model.categories.Category;
import cz.habrondrej.garden.model.categories.Group;
import cz.habrondrej.garden.model.categories.Place;
import cz.habrondrej.garden.model.categories.Species;
import cz.habrondrej.garden.model.categories.Type;

public class CategoryNewFragment extends BaseFragment {

    GroupDatabase groupDatabase;
    PlaceDatabase placeDatabase;
    SpeciesDatabase speciesDatabase;
    TypeDatabase typeDatabase;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getString("type");

        MainActivity mainActivity = (MainActivity) getActivity();
        groupDatabase = mainActivity.getGroupDatabase();
        placeDatabase = mainActivity.getPlaceDatabase();
        speciesDatabase = mainActivity.getSpeciesDatabase();
        typeDatabase = mainActivity.getTypeDatabase();

        return inflater.inflate(R.layout.fragment_category_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        switch (type) {
            case Category.TYPE_GROUP:
                toolbar.setTitle("Přidat skupinu");
                break;
            case Category.TYPE_PLACE:
                toolbar.setTitle("Přidat místo");
                break;
            case Category.TYPE_SPECIES:
                toolbar.setTitle("Přidat druh");
                break;
            case Category.TYPE_TYPE:
                toolbar.setTitle("Přidat typ");
                break;
        }

        AppCompatButton btn_cancelCat = view.findViewById(R.id.btn_cancelCat);
        Button btn_createCat = view.findViewById(R.id.btn_createCat);
        EditText et_categoryTitle = view.findViewById(R.id.et_categoryTitle);

        btn_cancelCat.setOnClickListener(v -> {
            NavHostFragment.findNavController(CategoryNewFragment.this)
                    .navigate(R.id.action_CategoryNewFragment_to_PlantNewFragment);
        });

        btn_createCat.setOnClickListener(v -> {
            if (et_categoryTitle.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Vyplňte název!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!createCategory(et_categoryTitle.getText().toString())) {
                Toast.makeText(getContext(), "Chyba při ukládání!", Toast.LENGTH_SHORT).show();
                return;
            }

            NavHostFragment.findNavController(CategoryNewFragment.this)
                    .navigate(R.id.action_CategoryNewFragment_to_PlantNewFragment);
        });

    }

    private boolean createCategory(String title) {

        switch (type) {
            case Category.TYPE_GROUP:
                return groupDatabase.create(new Group(-1, title));
            case Category.TYPE_PLACE:
                return placeDatabase.create(new Place(-1, title));
            case Category.TYPE_SPECIES:
                return speciesDatabase.create(new Species(-1, title));
            case Category.TYPE_TYPE:
                return typeDatabase.create(new Type(-1, title));
        }
        return false;
    }
}
