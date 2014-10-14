package com.scian.smsstorm.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scian.smsstorm.R;
import com.scian.smsstorm.data.bean.Log;
import com.scian.smsstorm.util.TextUtil;

public class LogListAdapter extends BaseAdapter {

    private List<Log> mData;

    private Context mContext;

    public LogListAdapter(Context context) {
        mData = new ArrayList<Log>();
        mContext = context;
    }

    public void SetData(List<Log> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        if (mData != null && mData.size() > arg0) {
            return mData.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_history_log_item, null);
            ViewHolder holder = new ViewHolder();
            convertView.setTag(holder);
        }

        Object tag = convertView.getTag();
        if (tag instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) tag;
            Log item = mData.get(position);

            holder.titleText.setText(TextUtil.longToTimeString(item.getTag()));
            holder.contentText.setText(item.getContent());

            setDetail(holder.totalText,
                    mContext.getString(R.string.detail_total),
                    String.valueOf(item.getTotal()));

            setDetail(holder.sentText,
                    mContext.getString(R.string.detail_sent),
                    String.valueOf(item.getSentCount()));

            setDetail(holder.deliveredText,
                    mContext.getString(R.string.detail_delivered),
                    String.valueOf(item.getDeliveredCount()));

            setDetail(holder.failedSentText,
                    mContext.getString(R.string.detail_sent_failed),
                    String.valueOf(item.getFailedSentCount()));

            setDetail(holder.failedDeliveredText,
                    mContext.getString(R.string.detail_delivered_failed),
                    String.valueOf(item.getDeliveredCount()));
        }
        return convertView;
    }

    private void setDetail(TextView text, String type, String count) {
        int start = type.length() - 1;
        int end = start + count.length();
        String concat = type + count;
        Spannable word = new SpannableString(concat);
        word.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setText(word);
    }

    public class ViewHolder {
        public TextView titleText;
        public TextView contentText;
        public TextView totalText;
        public TextView sentText;
        public TextView deliveredText;
        public TextView failedSentText;
        public TextView failedDeliveredText;
    }

}
