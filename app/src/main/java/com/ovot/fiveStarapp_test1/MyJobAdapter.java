package com.ovot.fiveStarapp_test1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyJobAdapter extends RecyclerView.Adapter<MyJobAdapter.MyViewHolder>{

    private List<MyJobModel> myJobList;
    private Context context;
    String service_id=" ";
    String statusId = "";
    String technicianId = "";
    String customerId = "";

    DatabaseHelper2 databaseHelper2;

    public MyJobAdapter(Context context, List<MyJobModel> myJobList) {

        this.context = context;
        this.myJobList = myJobList;

        databaseHelper2 = new DatabaseHelper2(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.custom_card_view,parent,false);



//        view.setOnClickListener(new );

        return new MyJobAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          service_id= String.valueOf(myJobList.get(position).getServiceId());
          statusId = String.valueOf(myJobList.get(position).getStatus_id());
          customerId = String.valueOf(myJobList.get(position).getCustomerId());
          technicianId = String.valueOf(myJobList.get(position).getTechnicianId());

          holder.serviceId.setText(service_id);
//        holder.serviceId.setText(("ServiceId : "+String.valueOf(myJobList.get(position).getServiceId())));
        holder.cmName.setText("CustomerName : "+myJobList.get(position).getCustomer_name());
        holder.phone.setText("MobileNumber : "+myJobList.get(position).getPhone1()+"/"+myJobList.get(position).getPhone2());
//        holder.address.setText("Address : "+myJobList.get(position).getAddress1()+","+myJobList.get(position).getCity()+","+myJobList.get(position).getState());
        holder.address.setText("Address : "+myJobList.get(position).getAddress1());

        holder.pincode.setText("PinCode : "+String.valueOf(myJobList.get(position).getPinCode()));
        holder.product_name.setText("ProductName : "+myJobList.get(position).getProduct_name());
        holder.product_code.setText("ProductCode : "+myJobList.get(position).getProductType_code());
        holder.model_name.setText("ModelCode : "+myJobList.get(position).getModel_code());
        holder.call_date.setText("CallDate : "+myJobList.get(position).getCall_date());
        holder.appoinmentdate.setText("AppointmentDate : "+myJobList.get(position).getAppointment_date());
        holder.status.setText("Status : "+myJobList.get(position).getStatusCode());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences preferences =context.getSharedPreferences(PREF_NAME2, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.apply();
                databaseHelper2.deleteIncentiveTableData();

                databaseHelper2.addServiceData(service_id,statusId,customerId,technicianId);
//
                Intent intent = new Intent(context, WorkEntryForm.class);
                intent.putExtra("service_id",service_id);
                intent.putExtra("status_id",statusId);
                intent.putExtra("customer_id",customerId);
                intent.putExtra("technician_id",technicianId);

                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return myJobList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView serviceId,cmName,phone,address,pincode,product_name,product_code,model_name,call_date,appoinmentdate,status;
     CardView cardView;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_job);

            serviceId = itemView.findViewById(R.id.service_id);
            cmName = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            product_name = itemView.findViewById(R.id.product_name);
            product_code = itemView.findViewById(R.id.product_code);
            model_name =  itemView.findViewById(R.id.model_name);
            call_date = itemView.findViewById(R.id.call_date);
            appoinmentdate = itemView.findViewById(R.id.appointment_date);
            product_code = itemView.findViewById(R.id.product_code);
            status = itemView.findViewById(R.id.status);


        }
    }
}
