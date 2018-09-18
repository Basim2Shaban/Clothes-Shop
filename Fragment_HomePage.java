package com.basim.outfitters;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.basim.outfitters.adapters.ViewPagerAdsAdapter;
import com.basim.outfitters.firebase_use.FireBaseGetData;
import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.modiles.ModelGetViewPagData;
import com.basim.outfitters.modiles.ModelKey;
import com.basim.outfitters.modiles.Model_GetPosts;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_HomePage extends Fragment {
    private Button btn_sale_something , btnBack , btnNext;
  //  private ArrayList<Model_GetPosts> ArrayGetPosts = new ArrayList<>();
    private RecyclerView recyclerView_allPosts;
    private ViewPager viewPagerBestAd;
    private FireBaseGetData fireBaseGetData = new FireBaseGetData();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static ArrayList<ModelKey> arrayLisKeys = new ArrayList<>();
    private ArrayList<ModelGetViewPagData> arrayListAllAds = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int dateToday;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private FireBaseVarConnect varConnect = new FireBaseVarConnect();
    private String mId, myName;
    private LinearLayout linearBtns ;


    public Fragment_HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__home_page, container, false);
        mAdView = (AdView) view.findViewById(R.id.adView);

        linkViews(view);

        mId = varConnect.bCurrentUser.getUid();




        Thread thread = new Thread(){
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fireBaseGetData.getArrayAllPosts(recyclerView_allPosts, getActivity());
                    }
                });
            }
        };
        thread.start();




        btn_sale_something.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_sale = new Intent(getActivity(), SaleNewSomething.class);
                startActivity(intent_to_sale);
            }
        });


        currentPage();
        getValuesKey();


        fireBaseGetData.onClickBtnNext(recyclerView_allPosts,btnNext,getActivity(), linearBtns);
        fireBaseGetData.onClickBtnBack(recyclerView_allPosts,btnBack,getActivity(), linearBtns);
        fireBaseGetData.OnScrollRec(recyclerView_allPosts,linearBtns , btnNext , btnBack, getActivity());

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        startAds();

    }



    public void linkViews(View view) {
        btn_sale_something = (Button) view.findViewById(R.id.btn_sale_something);
        linearBtns = (LinearLayout) view.findViewById(R.id.linearBtns);
        btnBack = (Button) view.findViewById(R.id.BtnBAck);
        btnNext = (Button) view.findViewById(R.id.BtnNext);
        recyclerView_allPosts = (RecyclerView) view.findViewById(R.id.recycler_all);
        recyclerView_allPosts.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_allPosts.setLayoutManager(staggeredGridLayoutManager);

        mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPagerBestAd = (ViewPager) view.findViewById(R.id.recycler_best);



    }

    public String getName() {
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myName = dataSnapshot.child("name").getValue().toString();
                if (myName.equals("")) {
                    Log.e("Vlaue is :", "null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myName;
    }

// this Method For Get Key Posts who's in the pager ads
    public void getValuesKey() {
        arrayLisKeys.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        dateToday = Integer.parseInt(sdf.format(new Date()));

        varConnect.mAdsShowNow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.e("This a Value Of :", "For Loop One");
                    String key = dataSnapshot1.child("key").getValue().toString();
                    arrayLisKeys.add(new ModelKey(key));

                }

                if (arrayLisKeys != null) {
                    Log.e("This a Value Of :", "For Loop Two");
                    for (int i = 0; i < arrayLisKeys.size(); i++) {
                        Log.e("This a Value Of :", "Get inside the fucking loop");
                        String key = arrayLisKeys.get(i).getKey();
                        getDataByKey(key);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

// and here in this method i sent key to get data from it
    public void getDataByKey(final String key) {
        varConnect.bDataBaseposts.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model_GetPosts modelGetPosts = dataSnapshot.getValue(Model_GetPosts.class);
                String sKey = key;
                String name = modelGetPosts.getMyName().toString();
                String price = modelGetPosts.getPrice().toString();
                String image_1 = modelGetPosts.getImage_1().toString();
                arrayListAllAds.add(new ModelGetViewPagData(name, price, image_1, sKey));

                ViewPagerAdsAdapter viewPagerAdsAdapter = new ViewPagerAdsAdapter(arrayListAllAds, getActivity(), mLayoutInflater);
                viewPagerBestAd.setAdapter(viewPagerAdsAdapter);

                //   }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

// here i selected who's display first ad in pager
    public void currentPage() {
        varConnect.mCurrentPage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int currentpage = Integer.parseInt(dataSnapshot.getValue().toString());
                viewPagerBestAd.setCurrentItem(currentpage, true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

// this method for google ads
    public void startAds() {
        MobileAds.initialize(getActivity(), "ca-app-pub-2166371604316477~5185775435");
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-2166371604316477/2423880724");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }


}

/*
save data in recycler when app restory
//
private Parcelable recyclerViewState;
recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

// Restore state
recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
 */




