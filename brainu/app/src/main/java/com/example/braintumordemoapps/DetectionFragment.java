package com.example.braintumordemoapps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.braintumordemoapps.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DetectionFragment extends Fragment {

    private Button select, predict;
    private ImageView imageView;
    private TextView output;
    private Bitmap img;

    public DetectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detection, container, false);

        select = view.findViewById(R.id.select);
        predict = view.findViewById(R.id.predict);
        imageView = view.findViewById(R.id.brainImg);
        output = view.findViewById(R.id.output);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (img != null) {
                    img = Bitmap.createScaledBitmap(img, 64, 64, true);
                    try {
                        Model model = Model.newInstance(getContext());
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 3}, DataType.FLOAT32);

                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();

                        inputFeature0.loadBuffer(byteBuffer);

                        Model.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                        if (outputFeature0.getFloatArray()[0] == 1.0) {
                            output.setText("Yes Brain Tumor");
                        } else {
                            output.setText("No Brain Tumor");
                        }

                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }

                } else {
                    Toast.makeText(getContext(), "Please Select Image First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            imageView.setImageURI(data.getData());

            Uri uri = data.getData();

            try {
                img = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
