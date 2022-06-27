package de.rub.selab22a15;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExportFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_export, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnExportDatabase = getView().findViewById(R.id.btnExportDatabase);
        btnExportDatabase.setOnClickListener(view1 -> {
            File booger = copyFileToFilesDir(getString(R.string.appDatabaseName));

            if (booger == null) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.msgExportError, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            Uri contentUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    "com.android.example.provider",
                    booger);
            sendEmail(contentUri);
        });
    }

    private File copyFileToFilesDir(String fileName) {
        File file;
        String newPath = getActivity().getFileStreamPath("").toString();
        Log.d("LOG PRINT SHARE DB", "newPath found, Here is string: " + newPath);
        String oldPath = getActivity().getDatabasePath(fileName).toString();
        Log.d("LOG PRINT SHARE DB", "oldPath found, Her is string: " + oldPath);
        try {
            File f = new File(newPath);

            if (f.mkdirs()) {
                throw new Exception();
            }

            FileInputStream fin = new FileInputStream(oldPath);
            FileOutputStream fos = new FileOutputStream(newPath + "/" + fileName);
            byte[] buffer = new byte[1024];
            int len1;
            while ((len1 = fin.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fin.close();
            fos.close();
            file = new File(newPath + "/" + fileName);
            if (file.exists())
                return file;
        } catch (Exception e) {
            Log.e("Export", e.toString());
        }
        return null;
    }

    private void sendEmail(Uri attachment) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.researcherEmail)});
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tracking Data");
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private class btnExportOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            File booger = copyFileToFilesDir(getString(R.string.appDatabaseName));

            if (booger == null) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.msgExportError, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            Uri contentUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    "com.android.example.provider",
                    booger);
            sendEmail(contentUri);
        }
    }

    private static class DBFileProvider extends FileProvider {
        Uri getDatabaseURI(Context context, String dbName) {
            File file = context.getDatabasePath(dbName);
            return getFileUri(context, file);
        }

        Uri getFileUri(Context context, File file) {
            return getUriForFile(context, "com.android.example.provider", file);
        }
    }
}