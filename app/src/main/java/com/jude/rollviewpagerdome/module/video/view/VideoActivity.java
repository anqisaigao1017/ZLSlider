//package com.jude.rollviewpagerdome.module.video.view;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.jude.rollviewpagerdome.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.utils.Log;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//
///**
// * Created by garry on 17/4/8.
// */
//
//public class VideoActivity extends AppCompatActivity {
//    private VideoView mVideoView;
//    private List<String> mNetWorkVideoList;
//    private int mCurrentVideoIndex;
//    private ProgressBar mProgressBar;
//    private TextView mDownloadRateView;
//    private TextView mLoadRateView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video);
//        initData();
//        initView();
//    }
//
//    private void initData() {
//        mNetWorkVideoList = new ArrayList<>();
//        mNetWorkVideoList.add("http://wsmp32.bbc.co.uk/");
//        mNetWorkVideoList.add("http://www.modrails.com/videos/passenger_nginx.mov");
//        mNetWorkVideoList.add("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
//        mCurrentVideoIndex = 0;
//    }
//
//    private void initView() {
//        mVideoView = (VideoView)findViewById(R.id.video_view);
//        mVideoView.setVideoURI(Uri.parse(mNetWorkVideoList.get(mCurrentVideoIndex)));
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                Log.d("testYJ","mVideoPrerpared");
//                mediaPlayer.setPlaybackSpeed(1.0f);
//            }
//        });
//        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                Log.d("testYJ","onCompletion");
//                if(mCurrentVideoIndex >= mNetWorkVideoList.size()-1){
//                    mCurrentVideoIndex = 0;
//                }else {
//                    mCurrentVideoIndex += 1;
//                }
//                mVideoView.setVideoURI(Uri.parse(mNetWorkVideoList.get(mCurrentVideoIndex)));
//                mVideoView.start();
//            }
//        });
//        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                        Log.d("testYJ","buffer start");
//                        if (mVideoView.isPlaying()) {
//                            mVideoView.pause();
//                            mProgressBar.setVisibility(View.VISIBLE);
//                            mDownloadRateView.setText("");
//                            mLoadRateView.setText("");
//                            mDownloadRateView.setVisibility(View.VISIBLE);
//                            mLoadRateView.setVisibility(View.VISIBLE);
//
//                        }
//                        break;
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                        Log.d("testYJ","buffer end");
//                        mVideoView.start();
//                        mProgressBar.setVisibility(View.GONE);
//                        mDownloadRateView.setVisibility(View.GONE);
//                        mLoadRateView.setVisibility(View.GONE);
//                        break;
//                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                        Log.d("testYJ","rate change");
//                        mDownloadRateView.setText("" + extra + "kb/s" + "  ");
//                        break;}
//                return true;
//            }
//        });
//
//        mProgressBar = (ProgressBar) findViewById(R.id.probar);
//
//        mDownloadRateView = (TextView) findViewById(R.id.download_rate);
//        mLoadRateView = (TextView) findViewById(R.id.load_rate);
//    }
//}
