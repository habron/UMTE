package cz.habrondrej.garden.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cz.habrondrej.garden.R;
import cz.habrondrej.garden.model.Plant;
import cz.habrondrej.garden.model.categories.Place;
import cz.habrondrej.garden.model.categories.Species;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantViewHolder> {

    List<Plant> plantList;
    Context context;

    public PlantsAdapter(List<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        holder.txt_title.setText(plantList.get(position).getTitle());
        holder.txt_date.setText("");
        if (plantList.get(position).getDate() != null)
            holder.txt_date.setText(plantList.get(position).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));


        Place place = plantList.get(position).getPlace();
        String placeTxt = place == null ? "" : place.getTitle();
        holder.txt_place.setText(placeTxt);

        Species species = plantList.get(position).getSpecies();
        String speciesTxt = species == null ? "" : species.getTitle();
        holder.txt_species.setText(speciesTxt);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        TextView txt_date;
        TextView txt_species;
        TextView txt_place;


        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_species = itemView.findViewById(R.id.txt_species);
            txt_place = itemView.findViewById(R.id.txt_place);
        }
    }
}
