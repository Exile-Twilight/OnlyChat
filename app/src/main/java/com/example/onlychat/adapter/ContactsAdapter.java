package com.example.onlychat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlychat.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {
    public ContactsAdapter(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }

    private final List<Contacts> contactsList;

    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item_recycler,parent,false);
        ContactsHolder contactsHolder = new ContactsHolder(view);
        return contactsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {
        holder.person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v, position);
            }
        });

        Contacts contacts = contactsList.get(position);
        holder.name.setText(contacts.getName());
        holder.num.setText(contacts.num);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactsHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView num;
        ConstraintLayout person;

        public ContactsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name_contacts);
            num = itemView.findViewById(R.id.item_num_contacts);
            person = itemView.findViewById(R.id.person);
        }
    }

    //1.定义变量接收接口
    private OnItemClickListener mOnItemClickListener;
    //2.定义接口：点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);//单击
    }
    //3.设置接口接收的方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public String getid(int pos)
    {
        String id = contactsList.get(pos).getNum();
        return id;
    }

    public static class Contacts {
        String name;
        String num;


        public Contacts(String name, String num) {
            this.name = name;
            this.num = num;

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }




    }
}
