package cz.habrondrej.garden;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;
import cz.habrondrej.garden.database.PhotoDatabase;
import cz.habrondrej.garden.model.Photo;

public class CameraFragment extends BaseFragment {

    private ImageView iv_picture;
    private Bitmap bitmap;

    private int plantId;

    private PhotoDatabase photoDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            plantId = getArguments().getInt("plantId");
        } catch (Exception e) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CameraFragment_to_OverviewFragment);
        }

        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoDatabase = ((MainActivity) getActivity()).getPhotoDatabase();

        Button btn_savePicture = view.findViewById(R.id.btn_savePicture);
        AppCompatButton btn_cancelCamera = view.findViewById(R.id.btn_cancelCamera);
        AppCompatButton btn_newPicture = view.findViewById(R.id.btn_newPicture);
        iv_picture = view.findViewById(R.id.iv_picture);
        startCamera();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }

        btn_newPicture.setOnClickListener(v -> startCamera());
        btn_savePicture.setOnClickListener(v -> savePicture());
        btn_cancelCamera.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("plantId", plantId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CameraFragment_to_PlantEditFragment, bundle);
        });

    }

    private void savePicture() {
        if (bitmap == null) {
            Toast.makeText(getContext(), "Nejdřív pořiďte fotku!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoDatabase.create(new Photo(-1, plantId, bitmap))) {
            Bundle bundle = new Bundle();
            bundle.putInt("plantId", plantId);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CameraFragment_to_PlantEditFragment, bundle);
        } else {
            Toast.makeText(getContext(), "Chyba při ukládání fotky!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            bitmap = (Bitmap) data.getExtras().get("data");
            iv_picture.setImageBitmap(bitmap);
        }
    }
}
