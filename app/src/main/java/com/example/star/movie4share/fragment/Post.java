package com.example.star.movie4share.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.star.movie4share.R;

public class Post extends Fragment {

    private EditText etName;
    private EditText etPrice;
    private EditText etStock;
    private EditText etShortDescription;
    private EditText etUrl;
    private GridView gridView;
    private Spinner spinner;
    private ArrayAdapter<String> category;

    private OnFragmentInteractionListener mListener;

    public Post() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        return view;
    }

    @Override
    public void onStart(){
        etName = (EditText) getActivity().findViewById(R.id.post_name_edittext);
        etPrice = (EditText) getActivity().findViewById(R.id.post_price_edittext);
        etStock = (EditText) getActivity().findViewById(R.id.post_stock_edittext);
        etShortDescription = (EditText) getActivity().findViewById(R.id.post_short_des_edittext);
        etUrl = (EditText) getActivity().findViewById(R.id.post_url_edittext);

        gridView = (GridView) getActivity().findViewById(R.id.post_pic_gridview);
        spinner = (Spinner) getActivity().findViewById(R.id.post_category_spinner);
        String[] mCategory = getResources().getStringArray(R.array.spinnerString);
        category = new ArrayAdapter<String>(getContext(), R.layout.spinner, mCategory);
        spinner.setAdapter(category);

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
