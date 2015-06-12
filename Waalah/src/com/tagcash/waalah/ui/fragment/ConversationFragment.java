package com.tagcash.waalah.ui.fragment;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tagcash.waalah.R;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConversationFragment extends Fragment implements Serializable {

    private String TAG = "ConversationFragment";
    private int qbConferenceType;
    private int startReason;
    //    private QBGLVideoView videoView;
    private GLSurfaceView videoView;
    //    private QBRTCSessionDescription sessionDescription;
    private static VideoRenderer.Callbacks REMOTE_RENDERER;

    private ImageButton handUpVideoCall;
    private View view;
    private Map<String, String> userInfo;

    private String callerName;
    private boolean isMessageProcessed;
    private MediaPlayer ringtone;

	private MainActivity mainActivity;

	public ConversationFragment(MainActivity activity) {
		super();
		
		mainActivity = activity;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        Log.d(TAG, "Fragment. Thread id: " + Thread.currentThread().getId());

        if (getArguments() != null) {
            startReason = getArguments().getInt(mainActivity.START_CONVERSATION_REASON);
            callerName = getArguments().getString(mainActivity.CALLER_NAME);

            Log.d("Track", "CALLER_NAME: " + callerName);

        }

        initViews(view);
        initButtonsListener();
        VideoRendererGui.setView(videoView, new Runnable() {
            @Override
            public void run() {
            }
        });

        mainActivity.setCurrentVideoView(videoView);
        setUpUIByCallType(qbConferenceType);

        return view;

    }

    private void setUpUIByCallType(int qbConferenceType) {
        if (qbConferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO.getValue()) {

            videoView.setVisibility(View.INVISIBLE);
        }
    }

    public void actionButtonsEnabled(boolean enability) {


        videoView.setEnabled(enability);
        handUpVideoCall.setEnabled(enability);


        // inactivate toggle buttons
        videoView.setActivated(enability);
    }


    @Override
    public void onStart() {
        super.onStart();
        QBRTCSession session = mainActivity.getCurrentSession();
        if (!isMessageProcessed) {
            if (startReason == StartConversetionReason.INCOME_CALL_FOR_ACCEPTION.ordinal()) {
                session.acceptCall(session.getUserInfo());
            } else {
                session.startCall(session.getUserInfo());
                startOutBeep();
            }
            isMessageProcessed = true;
        }
    }

    private void startOutBeep() {
        ringtone = MediaPlayer.create(getActivity(), R.raw.beep);
        ringtone.setLooping(true);
        ringtone.start();

    }

    public void stopOutBeep() {

        if (ringtone != null) {
            try {
                ringtone.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ringtone.release();
            ringtone = null;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        Log.d("Track", "onCreate() from ConversationFragment");
        super.onCreate(savedInstanceState);
    }

    private void initViews(View view) {

        videoView = (GLSurfaceView) view.findViewById(R.id.videoView);


        handUpVideoCall = (ImageButton) view.findViewById(R.id.img_end_call);
    }


    private void initButtonsListener() {

        handUpVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopOutBeep();
                actionButtonsEnabled(false);
                Log.d("Track", "Call is stopped");
                mainActivity.getCurrentSession().hangUp(userInfo);
            }
        });

    }


    public static enum StartConversetionReason {
        INCOME_CALL_FOR_ACCEPTION,
        OUTCOME_CALL_MADE;
    }

//    private List<QBUser> getOpponentsFromCall(ArrayList<Integer> opponents) {
//        ArrayList<QBUser> opponentsList = new ArrayList<>();
//
//        for (Integer opponentId : opponents) {
//            try {
//                opponentsList.add(QBUsers.getUser(opponentId));
//            } catch (QBResponseException e) {
//                e.printStackTrace();
//            }
//        }
//        return opponentsList;
//    }

}


