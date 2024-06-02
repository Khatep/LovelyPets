package com.example.lovelypets.exitalertdialog;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
        cancel.setOnClickListener(v -> dismiss());

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
