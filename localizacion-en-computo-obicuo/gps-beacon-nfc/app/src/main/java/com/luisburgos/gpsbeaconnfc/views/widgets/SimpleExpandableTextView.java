package com.luisburgos.gpsbeaconnfc.views.widgets;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.luisburgos.gpsbeaconnfc.R;


public class SimpleExpandableTextView extends TextView {

    private static final int DEFAULT_MAX_TEXT_LENGTH = 250;
    private CharSequence originalText;
    private CharSequence trimmedText;
    private CharSequence displayableText;
    private String ellipsis;
    private BufferType bufferType;
    private int trimLength;

    public SimpleExpandableTextView(Context context) {
        this(context, null);
    }

    public SimpleExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ellipsis = getResources().getString(R.string.label_view_more);
        trimLength = DEFAULT_MAX_TEXT_LENGTH - ellipsis.length();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisplayableText(originalText);
                setText();
            }
        });
    }

    public SimpleExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private void setDisplayableText(CharSequence displayableText){
        this.displayableText = displayableText;
    }

    private CharSequence getDisplayableText() {
        return displayableText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        if(originalText != null && originalText.length() > DEFAULT_MAX_TEXT_LENGTH){
            trimmedText = getTrimmedText(originalText);
            setDisplayableText(trimmedText);
        }else{
            setDisplayableText(originalText);
        }
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text.subSequence(0, trimLength) + ellipsis);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        sb.setSpan(fcs, trimLength, DEFAULT_MAX_TEXT_LENGTH, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

}