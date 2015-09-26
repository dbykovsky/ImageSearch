package com.home.dbykovskyy.imagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.dbykovskyy.imagesearch.R;
import com.home.dbykovskyy.imagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dbykovskyy on 9/22/15.
 */

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

    static class ViewHolder{
        ImageView ivImage;
        TextView tvTextCaption;

    }

    public ImageResultArrayAdapter(Context context, List<ImageResult> images){

        super(context, R.layout.item_image_result, images);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        ImageResult imageResult = getItem(position);

        if(convertView==null){
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);

            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_single_image_result);
            viewHolder.tvTextCaption = (TextView) convertView.findViewById(R.id.tv_single_iamge_result_title);
            convertView.setTag(viewHolder);
        }else{
             viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.tvTextCaption.setText(Html.fromHtml(imageResult.getTitle()));

        //setting photo
        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext()).load(imageResult.getThumbnailUrl()).resize(100, 100).centerInside().into(viewHolder.ivImage);




        return convertView;
    }
}
