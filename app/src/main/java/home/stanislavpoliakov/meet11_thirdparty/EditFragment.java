package home.stanislavpoliakov.meet11_thirdparty;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends DialogFragment {
    private static final String TAG = "meet11_logs";
    private CRUDable mActivity;
    private EditText title, body;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (CRUDable) context;
        } catch (ClassCastException ex) {
            Log.w(TAG, "Activity must implement CRUDable ", ex);
        }
    }

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        title = view.findViewById(R.id.editTitleEF);
        title.setText(getArguments().getString("title"));
        body = view.findViewById(R.id.editTextEF);
        body.setText(getArguments().getString("body"));

        Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            Bundle info = new Bundle();
            info.putString("title", title.getText().toString());
            info.putString("body", body.getText().toString());
            info.putInt("item position", getArguments().getInt("item position"));

            mActivity.update(info);
            dismiss();
        });
    }
}
