package free.tech.jofrasa.ExtraClass;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import free.tech.jofrasa.R;

/**
 * Created by root on 02-11-17.
 */

public class ImageDialog extends DialogFragment {
    public static final  String TAG ="Dialog";

    private String UrlImange;

    public static ImageDialog newInstance(String UrlImage){
        ImageDialog imageDialog = new ImageDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, UrlImage);
        imageDialog.setArguments(bundle);
        return imageDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UrlImange = getArguments().getString(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_image_dialog, container, false);

        PhotoView imageView = view.findViewById(R.id.image_dialog);
        Picasso.with(getActivity()).load(UrlImange).into(imageView);

        return view;
    }
}
