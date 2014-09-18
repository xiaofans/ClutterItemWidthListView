package xiaofan.clutterwidthitemlistview.clutterlistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * Created by zhaoyu on 2014/9/16.
 * 不规则宽度的listview
 **/
public class ClutterListView extends AdapterView{

    private ListAdapter mAdapter;
    private SparseArrayCompat mChildrenArray;
    private boolean mDataChanged;

    private int mItemCount;
    private SparseArrayCompat mItemLocations;
    private SparseArrayCompat mRowHeight;
    private int mSelectIndex;
    private int aboveHeight = 0;
    private int mRows = 1;
    private int margin = 15;
    private Context context;


    private final String TAG = ClutterListView.class.getSimpleName();
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mRowHeight.clear();
            mDataChanged = true;
            int i = getChildCount();
            int j = mAdapter.getCount();
            if(i > 0 && i > j){
                for(int i1 = i + -1; i1 >= j; i1--)
                    mChildrenArray.remove(i1);
            }
            mItemCount = mAdapter.getCount();
            if(mSelectIndex >= mItemCount)
            {
                mSelectIndex = -1;
                int k = mChildrenArray.size();
                for(int l = 0; l < k; l++)
                    ((View)mChildrenArray.get(l)).setSelected(false);

            }
            invalidate();
            requestLayout();
            super.onChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    public ClutterListView(Context context) {
        this(context,null);
        this.context = context;
    }

    public ClutterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        mItemLocations = new SparseArrayCompat();
        mRowHeight = new SparseArrayCompat();
        mChildrenArray = new SparseArrayCompat();
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        removeAllViewsInLayout();
        if(mAdapter != null)
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        mAdapter = (ListAdapter) adapter;
        resetLayout();
        if(mAdapter != null)
        {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        }
        requestLayout();
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int i) {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mDataChanged){
            for(int i = 0; i < mChildrenArray.size();i ++){
                View view = (View)mChildrenArray.get(i);
                if(view != null)
                {
                    Rect rect = (Rect)mItemLocations.get(i);
                    view.layout(rect.left, rect.top, rect.right, rect.bottom);
                }
            }

            mDataChanged = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(0,0);
        if(mItemCount != mAdapter.getCount()){
            throw new IllegalStateException("Data set changed,but not call adapter.notifyDataSetChanged method");
        }
        int width = measureWidth(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // Log.w(TAG,"measure width is:" + width);
        for(int i = 0; i < mAdapter.getCount();i ++){
            View view = (View)mChildrenArray.get(i);
            View view1 = mAdapter.getView(i, view, this);
            ViewGroup.LayoutParams layoutparams = getLayoutParams(view1);
            if(view1 == null) continue;
            view1.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, 0), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
            int k4 = view1.getMeasuredHeight();
            int l4 = view1.getMeasuredWidth();
            aboveHeight = k4;
            layoutparams.height = k4;
            layoutparams.width = l4;
            if(l4 > width){
                l4 = width;
            }
            view1.setLayoutParams(layoutparams);
            //   Log.w(TAG,"view1 width and height is::" + k4 +":" + l4);
            mChildrenArray.put(i,view1);
            if(mItemLocations.get(i) == null){
                if(i == 0){
                    Rect rect = new Rect(dip2px(margin),dip2px(margin),l4 + dip2px(margin),k4 + dip2px(margin));
                    mItemLocations.put(i,rect);
                }else{
                    Rect preRect = (Rect) mItemLocations.get(i - 1);
                    if(preRect.right + l4 + dip2px(margin * 2) > width){
                        mRows += 1;
                        Rect newRect = new Rect(
                                dip2px(margin),
                                preRect.bottom + dip2px(margin),
                                l4 + dip2px(margin),
                                (k4 + dip2px(margin)) * mRows);
                        mItemLocations.put(i,newRect);
                    }else{
                        Rect rect = new Rect(
                                preRect.right + dip2px(margin),
                                preRect.top,
                                preRect.right + dip2px(margin) + l4,
                                (k4 + dip2px(margin)) * mRows);
                        mItemLocations.put(i,rect);
                    }
                }
            }
            if(mRowHeight.get(i) == null){
                mRowHeight.put(i,l4);
            }
            addViewInLayout(view1,i,layoutparams,true);
        }
        setMeasuredDimension(width,mRows * (aboveHeight + dip2px(margin)) + dip2px(margin));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
        }
        return result;
    }



    private android.view.ViewGroup.LayoutParams getLayoutParams(View view)
    {
        android.view.ViewGroup.LayoutParams layoutparams = view.getLayoutParams();
        if(layoutparams == null)
            layoutparams = new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return layoutparams;
    }

    private void resetLayout()
    {
        mItemLocations.clear();
        mRowHeight.clear();
        mChildrenArray.clear();
        mItemCount = 0;
        mSelectIndex = -1;
        mDataChanged = true;
        mRows = 1;
    }


    public  int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
