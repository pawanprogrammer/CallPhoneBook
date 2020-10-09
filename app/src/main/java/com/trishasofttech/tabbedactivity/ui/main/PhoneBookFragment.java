package com.trishasofttech.tabbedactivity.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trishasofttech.tabbedactivity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneBookFragment extends Fragment {
RecyclerView recyclerView;
ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phonebook, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);
        fetchrecord();
        return v;
    }

    private void fetchrecord() {
        StringRequest sr = new StringRequest(0, "http://searchkero.com/pradip/showrecord.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jArray = jObj.getJSONArray("Data");
                            for (int i=0; i< jArray.length(); i++)
                            {
                                JSONObject jObj1 = jArray.getJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("namekey", jObj1.getString("name"));
                                hashMap.put("emailkey", jObj1.getString("email"));
                                hashMap.put("mobilekey", jObj1.getString("mobile"));
                                arrayList.add(hashMap);
                            }

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            MyAdapter myAdapter = new MyAdapter();
                            recyclerView.setAdapter(myAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
            MyHolder myHolder = new MyHolder(v);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            final HashMap<String, String> hashMap = arrayList.get(position);
            holder.tvname.setText(hashMap.get("namekey"));
            holder.tvmobile.setText(hashMap.get("mobilekey"));
            holder.tvemail.setText(hashMap.get("emailkey"));

            /*to click on the no for call*/
            holder.tvmobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent call  = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:"+hashMap.get("mobilekey")));
                    startActivity(call);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private class MyHolder extends  RecyclerView.ViewHolder{
        TextView tvname,tvemail,tvmobile;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvemail  = itemView.findViewById(R.id.tvemail);
            tvmobile = itemView.findViewById(R.id.tvmobile);
            tvname = itemView.findViewById(R.id.tvname);
        }
    }
}
