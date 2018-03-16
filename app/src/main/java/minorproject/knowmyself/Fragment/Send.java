package minorproject.knowmyself.Fragment;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import minorproject.knowmyself.Database.ServicesDBHelper;
import minorproject.knowmyself.Other.SendtoServer;
import minorproject.knowmyself.R;
import minorproject.knowmyself.Service.NotificationService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Send.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Send#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Send extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected String YES_ACTION,MAYBE_ACTION,NO_ACTION;
    EditText name,email,password;
    Button save;
    String url = "http://192.168.43.53/index.php";
    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_1 = "action_1";
    private OnFragmentInteractionListener mListener;

    public Send() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Send.
     */
    // TODO: Rename and change types and number of parameters
    public static Send newInstance(String param1, String param2) {
        Send fragment = new Send();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (EditText) getActivity().findViewById(R.id.name);
        email = (EditText) getActivity().findViewById(R.id.email);
        password = (EditText) getActivity().findViewById(R.id.password);
        ServicesDBHelper servicesDBHelper = new ServicesDBHelper(getContext());
        final JSONArray data=servicesDBHelper.getResults();
        save = (Button) getActivity().findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = name.getText().toString();
                final String useremail = email.getText().toString();
                final String userpassword = password.getText().toString();
                Toast.makeText(getContext(),"Data = "+data+" ",Toast.LENGTH_SHORT).show();

                new SendtoServer().send(getContext(),url,data.toString());

            }
        });

    }
    public void addNotification() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(),NOTIFICATION_CHANNEL_ID);


        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.profile)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info");
        Intent yesReceive = new Intent(getContext(), NotificationService.class);

        yesReceive.setAction(YES_ACTION);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(getContext(), 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_action_normal, "Normal", pendingIntentYes);

        //Maybe intent
        Intent maybeReceive = new Intent(getContext(), NotificationService.class);
        maybeReceive.setAction(MAYBE_ACTION);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(getContext(), 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_action_seminormal, "Rare", pendingIntentMaybe);

        //No intent
        Intent noReceive = new Intent(getContext(), NotificationService.class);
        noReceive.setAction(NO_ACTION);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(getContext(), 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_action_name, "Semi-normal", pendingIntentNo);


        notificationManager.notify(NOTIFICATION_ID  , notificationBuilder.build());

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onFragmentInteraction(string);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String string);
    }
}
