package minorproject.knowmyself.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import minorproject.knowmyself.Adapter.CustomToDoAdapter;
import minorproject.knowmyself.Database.ToDoDBHelper;
import minorproject.knowmyself.Other.ToDoBean;
import minorproject.knowmyself.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ToDo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ToDo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mTaskListView;
    private ToDoDBHelper mydb;
//    private CustomToDoAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    public ToDo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDo.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDo newInstance(String param1, String param2) {
        ToDo fragment = new ToDo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
        //qwer@gmail.com
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
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("To Do");
        }


      updateUI(view);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // or
        ListView listView = getActivity().findViewById(R.id.list_todo);
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String s=adapterView.getItemAtPosition(i).toString();
                    deleteTask(s);

            }
        });*/
    }


    public void updateUI(View view) {
        mTaskListView =(ListView) view.findViewById(R.id.list_todo);
        List<ToDoBean> list = new ToDoDBHelper(getContext()).getData();

        CustomToDoAdapter customToDoAdapter = new CustomToDoAdapter(getContext(),R.layout.to_do_listview,list);
        mTaskListView.setAdapter(customToDoAdapter);


        /*if (mAdapter == null) {
            mAdapter = new CustomToDoAdapter(getContext(),R.layout.to_do_listview,list);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }*/

    }
    public void deleteTask(String task) {
        /*mydb = new ToDoDBHelper(getContext());//todo
        Log.v("abcd",""+mydb+"   u  "+task);*/
        //mydb.deleteToDo(task);
        Toast.makeText(getContext(),"hitodod",Toast.LENGTH_SHORT).show();
        //updateUI(getView());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String title) {
        if (mListener != null) {
            mListener.onFragmentInteraction(title);
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
//qwer@gmail.com


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
        void onFragmentInteraction(String title);
    }
}
