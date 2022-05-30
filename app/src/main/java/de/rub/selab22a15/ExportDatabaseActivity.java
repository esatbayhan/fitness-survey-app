package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class ExportDatabaseActivity extends AppCompatActivity {

    private Button btnExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_database);

        btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new btnExportOnClick());
    }

    private void sendEmail(Uri attachment) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "esat.bayhan@rub.de");
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tracking Data");
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private class btnExportOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Uri uri = new DBFileProvider().getDatabaseURI(getApplicationContext(), "tracking_database");
            sendEmail(uri);
        }
    }

    private class DBFileProvider extends FileProvider {

        Uri getDatabaseURI(Context context, String dbName) {
            File file = context.getDatabasePath(dbName);
            return getFileUri(context, file);
        }

        Uri getFileUri(Context context, File file) {
            return getUriForFile(context, "com.android.example.provider", file);
        }
    }
}