package com.example.star.movie4share.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.MovieTypeAction;
import com.example.star.movie4share.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class FindNew extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<Product> typeAction = new ArrayList<>();
    private List<Product> typeLove = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public FindNew() {
        // Required empty public constructor
    }

    // TODO: Try if onCreated() is also okay!
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_new, null);
    }

    @Override
    public void onStart(){
        TextView actionList = (TextView) getActivity().findViewById(R.id.movie_type_action_more);
        actionList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), MovieTypeAction.class);
                intent.putExtra("type", "action");
                getActivity().startActivity(intent);
            }
        });

        TextView loveList = (TextView) getActivity().findViewById(R.id.movie_type_love_more);
        loveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MovieTypeAction.class);
                intent.putExtra("type", "love");
                getActivity().startActivity(intent);
            }
        });

        TextView funList = (TextView) getActivity().findViewById(R.id.movie_type_fun_more);
        funList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MovieTypeAction.class);
                intent.putExtra("type", "fun");
                getActivity().startActivity(intent);
            }
        });

        super.onStart();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
