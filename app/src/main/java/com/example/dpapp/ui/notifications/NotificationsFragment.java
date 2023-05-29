package com.example.dpapp.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dpapp.R;
import com.example.dpapp.databinding.FragmentNotificationsBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.android.exoplayer2.Player;
import android.content.DialogInterface;


public class NotificationsFragment extends Fragment {

    private PlayerView playerView;
    private ExoPlayer player;
    List<String> audioList = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference collectionRef;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection("audio");

        playerView= root.findViewById(R.id.audioplay);
        player = new ExoPlayer.Builder(getContext()).build();

        playerView.setPlayer(player);

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Add each document to the list
                        String data = document.getString("link");
                        audioList.add(data);
                    }

                    ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
                    for (String uri : audioList) {
                        MediaItem mediaItem = MediaItem.fromUri(uri);
                        MediaSource source = new ProgressiveMediaSource.Factory(new DefaultDataSource.Factory(getContext())).createMediaSource(mediaItem);
                        mediaSource.addMediaSource(source);
                        player.setMediaItem(mediaItem);
                        player.prepare();
                        player.play();
                    }

                    player.setMediaSource(mediaSource);
                    player.prepare();

                    player.addListener(new Player.Listener() {
                        @Override
                        public void onPlaybackStateChanged(int playbackState) {
                            if (playbackState == Player.STATE_ENDED) {
                                showNextAudioDialog();
                            }
                        }
                    });
                } else {
                    // Handle the error if the task fails
                    Log.d("Firestore", "Error getting documents: " + task.getException());
                }
            }

        });

        return root;
    }

    public void showNextAudioDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Play Next Audio")
                .setMessage("Do you want to play the next audio?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playNextAudio();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the user's choice if they don't want to play the next audio
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void playNextAudio() {
        int nextIndex = player.getCurrentMediaItemIndex() + 1;
        if (nextIndex < audioList.size()) {
            player.seekTo(nextIndex, C.TIME_UNSET);
            player.setPlayWhenReady(true);
        } else {
            // All audios have been played
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player.release();
        binding = null;
    }
}
