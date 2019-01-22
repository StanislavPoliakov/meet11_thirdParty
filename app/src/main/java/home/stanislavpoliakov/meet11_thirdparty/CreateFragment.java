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
public class CreateFragment extends DialogFragment {
    private static final String TAG = "meet11_logs";
    private CRUDable mActivity;
    private EditText editTitle, editBody;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (CRUDable) context;
        } catch (ClassCastException ex) {
            Log.w(TAG, "Activity must implement CRUDable: ", ex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        editTitle = view.findViewById(R.id.editTitleEF);
        editBody = view.findViewById(R.id.editTextEF);

        Button createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(v -> {
            Entry entry = new Entry(editTitle.getText().toString(), editBody.getText().toString());
            mActivity.create(entry);
            dismiss();
        });
    }
}
