package com.tranetech.dges.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.adapters.AdapterParentsMultiChild;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.R;
import com.tranetech.dges.utils.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class ActivityParentsMultiChild extends AppCompatActivity {
    private SharedPreferenceManager preferenceManager;
    private List<ParentChildData> parentChildDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterParentsMultiChild adapterParentsMultiChild;
    private FileCacher<String> stringCacher = new FileCacher<>(ActivityParentsMultiChild.this, "cache_tmp.txt");
    private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityParentsMultiChild.this, "cacheListTmp.txt");
    private FileCacher<Integer> storePosition = new FileCacher<>(ActivityParentsMultiChild.this, "SorageOFposition");
    private String response;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_multi_child);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Child");
        recyclerView = (RecyclerView) findViewById(R.id.rv_parents_multi_stu);
        preferenceManager = new SharedPreferenceManager();

        try {
            response = stringCacher.readCache();
            getJson(response);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (storePosition.hasCache()) {
            try {
                storePosition.clearCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void getJson(String response) throws JSONException, IOException {
        Log.e("get json data : ", response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {

                ParentChildData parentChildData = new ParentChildData();

                JSONObject jobj = jsonArray.getJSONObject(i);

                parentChildData.setsStudentID(jobj.getString("sId"));
                parentChildData.setsName(jobj.getString("fName"));
                parentChildData.setmName(jobj.getString("mName"));
                parentChildData.setlName(jobj.getString("lName"));
                parentChildData.setsStandard(jobj.getString("std"));
                parentChildData.setsStandard_ID(jobj.getString("stdid"));
                parentChildData.setDivision(jobj.getString("div"));
                parentChildData.setAdhar(jobj.getString("adhar"));
                parentChildData.setGrNo(jobj.getString("grNo"));
                parentChildData.setRollno(jobj.getString("rollNo"));
                parentChildData.setAddress(jobj.getString("address"));
                parentChildData.setMobile(jobj.getString("mobile"));
                parentChildData.setDob(jobj.getString("dob"));

                parentChildData.setGender(jobj.getString("gender"));
                parentChildData.seBloodgroop(jobj.getString("bloodgroup"));
                parentChildData.setNationality(jobj.getString("nationality"));
                parentChildData.setPhHndicap(jobj.getString("handi"));
                parentChildData.setCategory(jobj.getString("category"));
                parentChildData.setPhoto(jobj.getString("photo"));
                parentChildData.setLastschool(jobj.getString("lastschool"));
                parentChildData.setLaststd(jobj.getString("laststd"));
                parentChildData.setPercentage(jobj.getString("percentage"));
                parentChildData.setStatus(jobj.getString("status"));
                parentChildData.setMessage(jobj.getString("msg"));


                parentChildDataList.add(parentChildData);
                stringCacherList.writeCache(parentChildDataList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityParentsMultiChild.this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterParentsMultiChild = new AdapterParentsMultiChild(parentChildDataList, getApplicationContext());
       /* recyclerView.scrollToPosition(parentChildDataList.size() + 1);
        adapterParentsMultiChild.notifyItemInserted(parentChildDataList.size() + 1);*/
        recyclerView.setAdapter(adapterParentsMultiChild);
    }

}
