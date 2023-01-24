package com.ovot.fiveStarapp_test1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class IncentiveAdapter  extends RecyclerView.Adapter<IncentiveAdapter.MyViewHolder> {

    private List<IncentiveModel> incentiveModelList;
    private Context context;

    public IncentiveAdapter(Context context, List<IncentiveModel> incentiveModelList) {

        this.context = context;
        this.incentiveModelList =incentiveModelList;

//        Toast.makeText(context, "Inside Adapter" +incentiveModelList.size(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        Toast.makeText(context, "Inside Bind-view Holder" +incentiveModelList.size(), Toast.LENGTH_SHORT).show();


        if(incentiveModelList.size()>=1)
        {

//            Toast.makeText(context, "Inside if of Bind-view Holder" +incentiveModelList.size(), Toast.LENGTH_SHORT).show();

            IncentiveModel model=incentiveModelList.get(position);
            holder.txt2.setText(model.getDate());
            holder.txt3.setText(model.getServiceId());
            holder.txt4.setText(model.getServicetype());
            holder.txt5.setText(model.getCustomerName());
            holder.txt6.setText(model.getCustomerRating());
            holder.txt7.setText(model.getStarIncentive());
            holder.textView8.setText(model.getApproveStatus());
            holder.textView9.setText(model.getIncentiveAmt());
            holder.textView10.setText(model.getUniformApproval());
            holder.textView11.setText(model.getPaidStatus());
//            holder.txt1.setText(position);

            holder.txt1.setText(String.valueOf(position+1));



        }

    }

    @Override
    public int getItemCount() {
        return incentiveModelList.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,textView8,textView9,textView10,textView11,textView12;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1=itemView.findViewById(R.id.tv_sr);
            txt2=itemView.findViewById(R.id.tv_date);
            txt3=itemView.findViewById(R.id.tv_service_id);
            txt4=itemView.findViewById(R.id.tv_service_type);
            txt5=itemView.findViewById(R.id.tv_customer);
            txt6=itemView.findViewById(R.id.tv_rating);
            txt7=itemView.findViewById(R.id.tv_incentive_five_star);
            textView8 = itemView.findViewById(R.id.tv_approval);
            textView9 = itemView.findViewById(R.id.tv_aincetive_amt);
            textView10= itemView.findViewById(R.id.tv_uniform_approval);
            textView11 = itemView.findViewById(R.id.tv_paid_status);



        }
    }
}
