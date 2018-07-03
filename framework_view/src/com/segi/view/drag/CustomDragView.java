package com.segi.view.drag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.segi.view.R;
import com.segi.view.alert.CustomAlterListDialog;
import com.segi.view.alert.IdStringInfo;
import com.segi.view.scroll.ObservableScrollView;

import java.util.ArrayList;

import cn.segi.framework.log.Logger;

/**
 * 自定义拖动控件
 * Created by Kinwee on 2016/9/18.
 */
public abstract class CustomDragView<T> extends ObservableScrollView {

    /**
     * 跟布局
     */
    protected LinearLayout mRootLayout;

    private Context mContext;

    /**
     * 常用门布局
     */
    protected LinearLayout headerView;

    /**
     * 门列表布局
     */
    protected LinearLayout mainView;

    private ImageView dragImageView;//被拖拽项的影像，其实就是一个ImageView

    private WindowManager windowManager;//windows窗口控制类

    private WindowManager.LayoutParams windowParams;//用于控制拖拽项的显示的参数

    /**
     * 记录当前被拖拽的view
     */
    private View dragView;

    /**
     * 当前移动的y坐标
     */
    private float lastMoveY;

    private float offsetY;

    private int viewX = -1;

    private int viewY = -1;

    protected ArrayList<T> doorList;

    protected ArrayList<T> commonList;

    /**
     * 可拖拽排序的listview
     */
    protected DragSortListView dragListView;

    private DragListAdapter adapter;
    /**
     * 顶部listview的count限制
     */
    protected int draglist_limit_count = 3;

    /**
     * 顶部listview的最小高度 默认200
     */
    private int draglist_miniheight = 50;

    /**
     * 顶部listview的距父容器的边距
     */
    private int draglist_margin_toparent = 20;

    /**
     * 顶部和主view的左右边距
     */
    private int header_mainview_left_right_margin = 50;

    /**
     * view触发事件的id
     */
    protected static final int TOUTCH_ID =0x110001;

    /**
     * 可拖拽排序的listview长按是否可用
     */
    private boolean isOnlongClick=false;

    /**
     * 是否拖拽
     */
    protected boolean isToutch=false;

    /**
     * 拖拽滑动时的阻尼系数
     */
    private float damp=0.6f;

    /**
     * 拖拽最底部的界限
     */
    private int bottomLimit;

    public CustomDragView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CustomDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CustomDragView, 0, 0);

            draglist_margin_toparent = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.CustomDragView_draglist_margin_toparent, 1));
            header_mainview_left_right_margin = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.CustomDragView_header_mainview_left_right_margin, 1));
            draglist_miniheight = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.CustomDragView_draglist_miniheight, 1));
            draglist_limit_count = a.getInt(
                    R.styleable.CustomDragView_draglist_limit_count,
                    draglist_limit_count);
            damp = a.getFloat(
                    R.styleable.CustomDragView_damp,
                    damp);
            a.recycle();
        }
        init();
    }

    public CustomDragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        mRootLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mRootLayout.setLayoutParams(params);
        mRootLayout.setOrientation(LinearLayout.VERTICAL);
        initData();
        createHeaderView();
        createMainView();
        mRootLayout.addView(headerView);
        mRootLayout.addView(mainView);
        addView(mRootLayout);
        mRootLayout.setMotionEventSplittingEnabled(false);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 创建门列表布局
     */
    private void createMainView() {
        mainView = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(header_mainview_left_right_margin, 0, header_mainview_left_right_margin, 0);
        mainView.setLayoutParams(params);
        mainView.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 创建常用门布局
     */
    private void createHeaderView() {
        headerView = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(header_mainview_left_right_margin, 0, header_mainview_left_right_margin, 0);
        int padding = draglist_margin_toparent;
        headerView.setPadding(padding, padding, padding, padding);
        headerView.setLayoutParams(params);
        headerView.setMinimumHeight(draglist_miniheight);
        headerView.setOrientation(LinearLayout.VERTICAL);
        dragListView = new DragSortListView(mContext, null);
        adapter = new DragListAdapter();
        dragListView.setAdapter(adapter);
        adapter.setData(commonList);
        headerView.addView(dragListView);
        dragListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                if(isOnlongClick){
                    final ArrayList<IdStringInfo> list=new ArrayList<IdStringInfo>();
                    IdStringInfo idStringInfo=new IdStringInfo();
                    idStringInfo.id=1101;
                    idStringInfo.name="移除";
                    list.add(idStringInfo);
                    CustomAlterListDialog dialog=new CustomAlterListDialog(mContext, new CustomAlterListDialog.OnChooseListener() {
                        @Override
                        public void onChoose(int index) {
                            replaceOrRevoke(commonList.get(position));
                            commonList.remove(commonList.get(position));
                            adapter.notifyDataSetChanged();
                        }
                    },"",true,list);
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
        DragSortController controller = new DragSortController(dragListView);
        controller.setDragHandleId(getDragViewId());
        controller.setSortEnabled(true);
        dragListView.setFloatViewManager(controller);
        dragListView.setOnTouchListener(controller);
        dragListView.setDropListener(dropListener);
        dragListView.setDividerHeight(0);
    }

    /**
     * 设置常用门内间距
     */
    public void setHeaderViewPadding(){
        setHeaderViewPadding(draglist_margin_toparent,draglist_margin_toparent,draglist_margin_toparent,draglist_margin_toparent);
    }

    /**
     * 设置常用门无内间距
     */
    public void setHeaderViewNoPadding(){
        setHeaderViewPadding(0,0,0,0);
    }

    /**
     * 设置常用门内间距
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void setHeaderViewPadding(int l,int t,int r,int b){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(header_mainview_left_right_margin, 0, header_mainview_left_right_margin, 0);
        headerView.setPadding(l, t, r, b);
        headerView.setLayoutParams(params);
    }

    /**
     * 拖拽排序监听
     */
    DragSortListView.DropListener dropListener = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                T item = (T) adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
            }
        }
    };

    private void initData() {
        doorList = new ArrayList<T>();
        commonList = new ArrayList<T>();
    }

    /**
     * 刷新所有UI
     */
    protected void refreshAll(){
        for (int i=0;i<mainView.getChildCount();i++){
            if(null!=mainView.getChildAt(i).getTag()){
                refreshItem(mainView.getChildAt(i), (T)mainView.getChildAt(i).getTag());
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 常用门列表更新
     * @param contentList
     */
    protected void dragListNotifyDataSetChanged(ArrayList<T> contentList){
        commonList.clear();
        commonList.addAll(contentList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 开始拖拽
     * @param view
     */
    protected void startDrag(View view){
        isToutch=true;
        isDraging(isToutch);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int y = location[1];
        view.setVisibility(View.INVISIBLE);
        dragView(view, y);
    }

    /**
     * 获取拖动块的高度
     */
    public int getDragItemHeight(){
        int itemHeight=0;
        if(dragListView.getChildCount()!=0){
            itemHeight= dragListView.getChildAt(0).getHeight();
        }else{
            for (int i=0;i<mainView.getChildCount();i++){
                if(mainView.getChildAt(i) instanceof LinearLayout){
                    itemHeight= mainView.getChildAt(i).getHeight();
                    break;
                }
            }
        }
        return itemHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //捕获down事件
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            /**
             * 抬起
             */
            if (null != dragView) {
                /**
                 * 是否在虚线区域
                 */
                boolean isIn = checkPointInDotted((int) ev.getY(), headerView);
                if (isIn) {
                    addItemToHeaderView(ev, dragView);
                } else {
                    dragView.setVisibility(View.VISIBLE);
                }
            }
            stopDrag();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            /**
             * 移动
             */
            int y = (int) ev.getRawY();
            offsetY = y - lastMoveY;
            if (null != dragImageView && offsetY != 0) {
                initBitmapToDrag(null, y);
                return false;
            }
        }else{
            stopDrag();
        }
        lastMoveY = ev.getRawY();
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 将拖拽的view加入常用门列表
     * @param ev
     * @param dragView
     */
    private void addItemToHeaderView(MotionEvent ev, View dragView) {
        View tv = dragView;
        if (null != tv.getTag()) {
            T tagInfo = (T) tv.getTag();
            boolean isInparent=true;
            for (int i = 0; i < adapter.getCount(); i++) {
                View childView = dragListView.getChildAt(i);
                boolean isInChild = checkPointInDotted((int) ev.getY(), childView);
                if (isInChild) {
                    /**
                     * 替换
                     */
                    isInparent=false;
                    T replaceContent = commonList.get(i);
                    commonList.remove(i);
                    commonList.add(i, tagInfo);
                    /**
                     * 替换虚线区域数据
                     */
                    adapter.setData(commonList);
                    /**
                     * 替换列表区域数据
                     */
                    dragView.setVisibility(View.VISIBLE);
                    mainView.removeView(dragView);
                    replaceOrRevoke(replaceContent);
                }
            }
            if (isInparent&&dragListView.getChildCount() < draglist_limit_count) {
                /**
                 * 添加
                 */
                commonList.add(tagInfo);
                mainView.removeView(dragView);
                adapter.setData(commonList);
            }else{
                dragView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 判断当前坐标是否在虚线内
     */
    public boolean checkPointInDotted(int y, View checkView) {
        int[] location = new int[2];
        checkView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        int minY=location[1];
        int maxY=minY+checkView.getHeight();
        Logger.e("checkPointInDotted", "y=" +y);
        Logger.e("checkPointInDotted", " minY=" + minY + " maxY=" + maxY);
        if (minY < y && y < maxY) {
            return true;
        }
        return false;
    }

    /**
     * 拖动影像
     */
    public void dragView(View dragView, int y) {
        this.dragView = dragView;
        this.dragView.destroyDrawingCache();
        if (null != dragView) {
            Log.e("onInterceptTouchEvent", "MotionEvent.ACTION_DOWN");
            /**
             * 开始拖动
             */
            //设置Drawingcache为true，获得选中项的影像bm，就是后面我们拖动的那个图像
            this.dragView.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(this.dragView.getDrawingCache());
            initBitmapToDrag(bm, y);
        }
    }

    public void setBottomLimit(int bottomLimit){
        this.bottomLimit=bottomLimit;
    }

    /**
     * 准备拖动，初始化拖动项的图像
     *
     * @param bm
     * @param y
     */
    public void initBitmapToDrag(Bitmap bm,  int y) {

        windowParams = new WindowManager.LayoutParams();
        //从上到下计算y方向上的相对位置，
        windowParams.gravity = Gravity.TOP;
        if (viewX == -1 && viewY == -1) {
            windowParams.x = 0;
            windowParams.y = y - 10;
            viewX = 0;
            viewY = y - 10;
        } else {
            windowParams.x = viewX;
            windowParams.y = viewY + (int) offsetY;
            int[] location = new int[2];
            headerView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            if (windowParams.y < location[1]) {
                windowParams.y = location[1];
            }
            if(bottomLimit !=0){
                if (windowParams.y > bottomLimit-10) {
                    windowParams.y = bottomLimit-10;
                }
            }
            scrollBy(0, (int) (offsetY*damp) - 1);

            Logger.e("startDrag", "getScrollY()" + getScrollY());
            Logger.e("startDrag", "getScrollX()" + getScrollX());

            Logger.e("startDrag", "windowParams.x=" + windowParams.x);
            Logger.e("startDrag", "windowParams.y=" + windowParams.y);
            Logger.e("startDrag", "bottomLimit=" +bottomLimit);
        }
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //下面这些参数能够帮助准确定位到选中项点击位置，照抄即可
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        if (dragImageView == null) {
            //把影像ImagView添加到当前视图中
            dragImageView = new ImageView(getContext());
            dragImageView.setImageBitmap(bm);

            windowManager.addView(dragImageView, windowParams);
        } else {
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }

    /**
     * 停止拖动
     */
    public void stopDrag() {
        if (dragImageView != null) {
            isToutch=false;
            isDraging(isToutch);
            windowManager.removeView(dragImageView);
            if(dragView!=null){
                dragView.setVisibility(View.VISIBLE);
            }
            dragImageView = null;
            dragView = null;
            viewX = -1;
            viewY = -1;
            Logger.e("CustomDragView", "stopDrag()");
        }
    }

    public class DragListAdapter<T> extends BaseAdapter {

        private ArrayList<T> data;

        public void remove(T item) {
            data.remove(item);
            notifyDataSetChanged();
        }

        public void insert(T item, int to) {
            data.add(to, item);
        }

        public void setData(ArrayList<T> data) {
            this.data = data;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            dragListViewCountChange(null == data ? 0 : data.size());
            return null == data ? 0 : data.size();
        }

        @Override
        public T getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDragListItemView(position, convertView, parent);
        }

    }

    /**
     * 获取触摸拖动的view的id
     *
     * @return
     */
    protected abstract int getDragViewId();

    /**
     * 顶部listview的getView实现方法
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected abstract View getDragListItemView(int position, View convertView, ViewGroup parent);

    /**
     * 刷新门列表每一个item
     * @param view
     * @param content
     */
    protected abstract void refreshItem(View view, T content);

    /**
     * 常用门的个数变化-用于虚线内的文字提示
     * @param count
     */
    protected abstract void dragListViewCountChange(int count);

    /**
     * 替换或撤回常用门
     * @param content
     */
    protected abstract void replaceOrRevoke(T content);

    /**
     * 是否拖动-防止拖动的时候进行其他操作
     * @param b
     */
    protected abstract void isDraging(boolean b);


    /**
     * 设置常用门拖动和长按是否可用
     * @param isEnable
     */
    protected void setOnLongClickEnable(boolean isEnable){
        isOnlongClick=isEnable;
        dragListView.setDragEnabled(isOnlongClick);
    }
}
