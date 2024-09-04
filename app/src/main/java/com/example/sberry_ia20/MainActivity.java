package com.example.sberry_ia20;
/*
# Copyright 2024 Giovanny M.R.

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

# http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        RecyclerView iconListRecyclerView = findViewById(R.id.iconListRecyclerView);

        public class IconItem {
            public int iconResId;
            public String description;

            public IconItem(int iconResId, String description) {
                this.iconResId = iconResId;
                this.description = description;
            }
        }
        List<IconItem> iconItems = new ArrayList<>();
        iconItems.add(new IconItem(R.drawable.ic_icon1, "Cuenta"));
        iconItems.add(new IconItem(R.drawable.ic_icon2, "Controbuir "));
        iconItems.add(new IconItem(R.drawable.ic_icon3, "Detecciones en la Zona "));
// ... more items

        IconListAdapter iconListAdapter = new IconListAdapter(iconItems);
        iconListRecyclerView.setAdapter(iconListAdapter);

*/
    }

    public void entrar(View view){
        Intent entrar = new Intent(this,activity_eleccion.class);
        startActivity(entrar);
    }

    public void salir(View view){
        finish();
    }
/*
    public class IconListAdapter extends RecyclerView.Adapter<IconListAdapter.IconViewHolder> {

        private List<IconItem> iconItems;

        public IconListAdapter(List<IconItem> iconItems) {
            this.iconItems = iconItems;
        }

        public static class IconViewHolder extends RecyclerView.ViewHolder {
            public ImageView iconImageView;
            public TextView descriptionTextView;

            public IconViewHolder(View itemView) {
                super(itemView);
                iconImageView = itemView.findViewById(R.id.iconImageView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            }
        }

        @NonNull
        @Override
        public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_icon, parent, false);
            return new IconViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
            IconItem currentItem = iconItems.get(position);
            holder.iconImageView.setImageResource(currentItem.iconResId);
            holder.descriptionTextView.setText(currentItem.description);
        }

        @Override
        public int getItemCount() {
            return iconItems.size();
        }
    }
*/

}