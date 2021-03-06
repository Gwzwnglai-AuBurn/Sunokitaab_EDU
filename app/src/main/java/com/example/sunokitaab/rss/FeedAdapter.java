package com.example.sunokitaab.rss;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunokitaab.R;

import java.io.File;

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    FeedAdapter.OnChapterListener onChapterListener;
    TextView title;
    String chapterTitle;
    SeekBar seekBar;
    TextView size;
    Button download;
    String url ="";
//    MediaController mediaController;

    public FeedViewHolder(View itemView, FeedAdapter.OnChapterListener onChapterListener) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        download = itemView.findViewById(R.id.downloadContent);
        size = itemView.findViewById(R.id.size);
//        mediaController = itemView.findViewById(R.id.mediaController);

        this.onChapterListener = onChapterListener;

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        onChapterListener.onChapterClick(url, chapterTitle);


    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private OnChapterListener mOnChapterListener;

    RSSObject rssObject;
    Context context;
    LayoutInflater inflater;


    public FeedAdapter(RSSObject rssObject, Context context, OnChapterListener onChapterListener) {
        this.rssObject = rssObject;
        this.context = context;
        this.mOnChapterListener = onChapterListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parsed_data_cardview,parent,false);
        return new FeedViewHolder(rootView, mOnChapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, final int position) {
          holder.chapterTitle = rssObject.getItems().get(position).title;
          holder.title.setText(rssObject.getItems().get(position).title);
          holder.url = (rssObject.getItems().get(position).enclosure.link);

        final int file_size = (rssObject.getItems().get(position).enclosure.length)/1024000;
        holder.size.setText(file_size + " MB");

        Log.d("media_links", rssObject.getItems().get(position).enclosure.link);

        holder.download.setOnClickListener(new View.OnClickListener() {
            String urlLL = rssObject.getItems().get(position).enclosure.link;
            @Override
            public void onClick(View v) {
                downloadMedia(urlLL,rssObject.getItems().get(position).title );
            }
        });
      /*catch (Exception e){
          e.printStackTrace();
          Toast.makeText(context, "Recordings Coming Soon !", Toast.LENGTH_SHORT).show();
      }

       */
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }

    public interface OnChapterListener{
        void onChapterClick(String url, String title);
    }

    private void downloadMedia(String url, String title) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("SunoKitaab Media Downloader");
        request.setDescription("Downloading media");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("audio/mpeg");
        File folder = new File(Environment.getExternalStorageDirectory() + "/SunoKitaab");
        if(!folder.exists()){
            folder.mkdir();
        }
        String finalTitle = title.trim() + ".mp3";
        request.setDestinationInExternalPublicDir("/SunoKitaab", finalTitle);

        DownloadManager manager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


       /* AsyncTask<String, String, String> downloadMediaAsync = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Please wait...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {

                String result = "";
                File folder = new File(android.os.Environment.getExternalStorageDirectory(), "SunoKitaab");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                try {
                    File mediaFile = new File(Environment.getExternalStorageDirectory() + "/SunoKitaab/" + title.trim() + ".mp3");
                    FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
                    fileOutputStream.write(urll.getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    result = "OK";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
                @Override
                protected void onPostExecute(String s){
                    mDialog.dismiss();
                }
            };

        */
    }

}


