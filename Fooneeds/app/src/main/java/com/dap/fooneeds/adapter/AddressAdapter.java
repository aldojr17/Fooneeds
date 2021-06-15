package com.dap.fooneeds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.AddressItemBinding;
import com.dap.fooneeds.databinding.CartItemBinding;
import com.dap.fooneeds.entity.Address;
import com.dap.fooneeds.entity.CartItem;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addresses;
    private AddressItemListener listener;

    public AddressAdapter(List<Address> addresses, AddressItemListener listener) {
        this.addresses = addresses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new AddressViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.setAddressData(addresses.get(position));
        if(addresses.get(position).getSelected().equals("No")){
            holder.binding.btnChecked.setVisibility(View.INVISIBLE);
        }else{
            holder.binding.btnChecked.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private AddressItemBinding binding;
        private WeakReference<AddressItemListener> listenerRef;

        public AddressViewHolder(@NonNull View itemView, AddressItemListener listener) {
            super(itemView);
            binding = AddressItemBinding.bind(itemView);

            listenerRef = new WeakReference<>(listener);
            itemView.setOnClickListener(this);
        }

        public void setAddressData(Address address){
            binding.tvStreet.setText(address.getStreet());
            binding.tvCity.setText(address.getCity());
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().itemClicked(getAdapterPosition());
        }
    }

    public interface AddressItemListener{
        void itemClicked(int position);
    }
}
