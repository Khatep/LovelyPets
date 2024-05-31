package com.example.lovelypets.exit_alert_dialog;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lovelypets.R;

import java.util.Objects;

public class ExitDialogActivity extends Dialog {
    private TextView cancel;
    private TextView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exit_dialog);

        cancel = findViewById(R.id.cancel_text);
        close = findViewById(R.id.close_text);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        close.setOnClickListener(v -> finishAffinity(Objects.requireNonNull(getOwnerActivity())));

    }

    public ExitDialogActivity(@NonNull Context context) {
        super(context);
    }

    public ExitDialogActivity(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ExitDialogActivity(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
