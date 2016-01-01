package com.taurus.blogreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taurus.blogreader.R;

import java.util.ArrayList;

import models.Blog;

/**
 * Created by Emin on 12/7/2015.
 */
public class BlogReaderListViewAdapter extends ArrayAdapter<Blog> {

    private Context context;
    private final int resource;
    private  ArrayList<Blog> objects;

    public BlogReaderListViewAdapter(Context context, int resource, ArrayList<Blog> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Blog getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objects.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

        }


        TextView textViewTitle = (TextView) convertView.findViewById(R.id.text_view_blog1);
        textViewTitle.setText(objects.get(position).getTitle());
        TextView textViewAuthor = (TextView) convertView.findViewById(R.id.text_view_blog2);
        textViewAuthor.setText(objects.get(position).getAuthor());
        return convertView;
    }


}
