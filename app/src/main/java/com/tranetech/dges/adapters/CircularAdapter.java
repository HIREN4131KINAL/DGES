package com.tranetech.dges.adapters;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.CircularData;
import com.tranetech.dges.utils.MarshmallowPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Markand on 26-05-2017 at 10:51 AM.
 */

public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.CircularViewHolder> {

    private List<CircularData> alCircularData;
    private Context context;
    private ProgressDialog mProgressDialog;
    private DownloadTask downloadTask;
    private MarshmallowPermissions marsh;


    public CircularAdapter(List<CircularData> alCircularData, Context context, MarshmallowPermissions marsh) {
        this.alCircularData = alCircularData;
        this.context = context;
        this.marsh = marsh;
    }

    @Override
    public CircularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_circular, parent, false);
        // execute this when the downloader must be fired
        downloadTask = new DownloadTask(context);

        return new CircularViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(CircularViewHolder holder, int position) {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
        final CircularData circularData = alCircularData.get(position);
        boolean fileExists = new File("/sdcard/" + circularData.getsCircularFileName()).isFile();
        holder.txtCircularTitle.setText(circularData.getsCircularTitle());
        holder.txtCircularDesc.setText(circularData.getsCircularDesc());
        holder.txtCircularDate.setText(circularData.getsCircualarDate());
        holder.imgCircualr.setImageResource(R.drawable.circular);
        if (circularData.getsCircularURL() != null) {
            holder.download.setVisibility(View.VISIBLE);
            if (fileExists) {
                holder.download.setText("Open");
                holder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!marsh.checkIfAlreadyhavePermission()) {
                            marsh.requestpermissions();
                        } else {
                            view(circularData.getsCircularFileName());
                        }
                    }
                });
            } else {
                holder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!marsh.checkIfAlreadyhavePermission()) {
                            marsh.requestpermissions();
                        } else {
                            downloadTask.execute(circularData.getsCircularURL(), circularData.getsCircularFileName());
                        }
                    }
                });
            }

        }
    }


    public void view(String filename) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > M) {
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(uri, "application/pdf");
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return alCircularData.size();
    }

    public class CircularViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCircularTitle, txtCircularDesc, txtCircularDate;
        public ImageView imgCircualr;
        public Button download;

        public CircularViewHolder(View itemView) {
            super(itemView);
            txtCircularTitle = (TextView) itemView.findViewById(R.id.txt_circular_title);
            txtCircularDesc = (TextView) itemView.findViewById(R.id.txt_circular_description);
            txtCircularDate = (TextView) itemView.findViewById(R.id.txt_circular_date);
            imgCircualr = (ImageView) itemView.findViewById(R.id.img_circular);
            download = (Button) itemView.findViewById(R.id.btn_circular_download);
        }
    }

    public void clear() {
        alCircularData.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<CircularData> alHWData) {
        this.alCircularData.addAll(alCircularData);
        notifyDataSetChanged();
    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(values[0]);
        }

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/" + sUrl[1]);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (s != null) {
                Toast.makeText(context, "Download error: " + s, Toast.LENGTH_LONG).show();
                Log.e("Download", s);
            } else {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        }
    }
}
