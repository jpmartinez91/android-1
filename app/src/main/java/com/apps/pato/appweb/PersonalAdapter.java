package com.apps.pato.appweb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pato on 20/07/17.
 */

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.PersonalViewHolder> {

    private List<Personal> personal;

    public static class PersonalViewHolder extends RecyclerView.ViewHolder{

        public ImageView imagen;
        public TextView nombre;
        public TextView apellido;
        public TextView correo;
        public TextView telefono;



        public PersonalViewHolder(View v){
            super(v);
            imagen = (ImageView)v.findViewById(R.id.leterPerson);
            nombre = (TextView)v.findViewById(R.id.personName);
            apellido = (TextView)v.findViewById(R.id.personLname);
            correo = (TextView)v.findViewById(R.id.personEmail);
            telefono = (TextView)v.findViewById(R.id.personPhone);

        }

    }

    public PersonalAdapter(List<Personal> personal){
        this.personal = personal;
    }

    @Override
    public int getItemCount(){
        return personal.size();
    }

    @Override
    public PersonalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_personal, viewGroup, false);
        return new PersonalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonalViewHolder viewHolder, int i) {
//        viewHolder.imagen.setImageResource(personal.get(i).getImagen());
        viewHolder.nombre.setText(personal.get(i).getNombres());
        viewHolder.apellido.setText(personal.get(i).getApellidos());
        viewHolder.correo.setText(personal.get(i).getCorreo());
        viewHolder.telefono.setText(personal.get(i).getTelefono());
    }
}
