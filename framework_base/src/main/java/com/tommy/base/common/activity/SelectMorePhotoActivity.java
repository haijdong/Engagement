package com.tommy.base.common.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tommy.base.R;
import com.tommy.base.base.BaseActivity;
import com.tommy.base.common.adapter.SelectPhotoAdapter;
import com.tommy.base.common.adapter.SelectPhotoPopAdapter;
import com.tommy.base.common.model.SelectImgVo;
import com.tommy.base.common.model.SelectPhotoPopVo;
import com.tommy.base.common.view.CustomSelectPhotoPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.segi.framework.consts.FrameworkConst;
import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.util.ImageUtil;

/**
 * 选择相册照片
 */
public class SelectMorePhotoActivity extends BaseActivity implements OnClickListener {
    /**
     * 请求当前页面的动作编码
     */
    public static final int SELECT_REQ_CODE = 0x1646;
    /**
     * 刷新页面
     */
    private static final int REFRESH_IMAGE = 0x1645;
    /**
     * 是否需要水印
     */
    public static final String IS_ADD_WATER = "is_add_water";
    /**
     * 是否能编辑
     */
    public static final String IS_EDITABLE = "is_editable";
    /**
     * 最大选择数
     */
    public static final String MAX_SELECT_IV = "max_select_iv";
    /**
     * 返回选择的图片列表
     */
    public static final String SELECT_IV_LIST = "select_iv_list";
    /**
     * 最大选择数
     */
    private int mMaxSelectCount = 0;
    /**
     * 是否需要水印
     */
    private boolean mIsAddWaterMark = true;
    /**
     * 是否能编辑
     */
    private boolean mIsEditAble = true;
    /**
     * 搜索到的所有照片
     */
    private Map<String, List<SelectImgVo>> mAllImgMap = new LinkedHashMap<String, List<SelectImgVo>>();
    /**
     * 选择的图片列表
     */
    private ArrayList<SelectImgVo> mHasSelectedImgs = new ArrayList<>();
    /**
     * 图片列表控件
     */
    private GridView mGirdView = null;
    private SelectPhotoAdapter mSelectPhotoAdapter = null;
    /**
     * 目录弹窗相关
     */
    private SelectPhotoPopAdapter mSpinnerAdapter;
    private List<SelectPhotoPopVo> mPopList = new ArrayList<SelectPhotoPopVo>();
    private CustomSelectPhotoPopWindow mCustomSelectPhotoPopWindow;
    /**
     * 当前目录名称
     */
    private TextView mPhotoTitleTv;
    /**
     * 完成按钮
     */
    private TextView mFinishTv;
    /**
     * 编辑按钮
     */
    private TextView mEditTv;

    /**
     * 刷新页面
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_IMAGE:
                    dismissLoadingDialog();
                    mPopList.clear();
                    Set<Map.Entry<String, List<SelectImgVo>>> entrySet = mAllImgMap.entrySet();
                    Iterator iterator = entrySet.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, List<SelectImgVo>> entry = (Map.Entry) iterator.next();
                        SelectPhotoPopVo vo = new SelectPhotoPopVo();
                        vo.count = String.valueOf(entry.getValue().size());
                        vo.dirName = entry.getKey();
                        vo.path = entry.getValue().get(0).path;
                        mPopList.add(vo);
                    }
                    if (null == mCustomSelectPhotoPopWindow) {
                        mSpinnerAdapter = new SelectPhotoPopAdapter(SelectMorePhotoActivity.this, mPopList, R.layout.select_more_album_item);
                        mCustomSelectPhotoPopWindow = new CustomSelectPhotoPopWindow(
                                SelectMorePhotoActivity.this,
                                mPopItemClickListener, mSpinnerAdapter);
                    }
                    if (mPopList.size() != 0) {
                        initImageGridView(mPopList.get(0).dirName);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void initViews() {
        setContentView(R.layout.select_more_photo);
        findViewById(R.id.LButton).setVisibility(View.INVISIBLE);
        mPhotoTitleTv = (TextView) findViewById(R.id.title);
        findViewById(R.id.select_photo_arr).setVisibility(View.VISIBLE);
        Button rBtn = (Button) findViewById(R.id.RButton);
        rBtn.setVisibility(View.VISIBLE);
        rBtn.setText(R.string.cancel);
        mGirdView = (GridView) findViewById(R.id.more_photo_gr);
        mFinishTv = (TextView) findViewById(R.id.finishSelect);
        mEditTv = (TextView) findViewById(R.id.edit);

        createLoadingDialog(this, true, R.string.loading);
        showLoadingDialog();
    }

    @Override
    protected void initEvents() {
        mPhotoTitleTv.setOnClickListener(this);
        findViewById(R.id.RButton).setOnClickListener(this);
        mFinishTv.setOnClickListener(this);
        mGirdView.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    protected void initData() {
        mFinishTv.setText(String.format(getString(R.string.finish_num), "0"));
        mMaxSelectCount = this.getIntent().getIntExtra(MAX_SELECT_IV, 3);
        mIsAddWaterMark = getIntent().getBooleanExtra(IS_ADD_WATER, true);
        mIsEditAble = getIntent().getBooleanExtra(IS_EDITABLE, true);

        if (mIsEditAble) {
            mEditTv.setVisibility(View.VISIBLE);
            mEditTv.setOnClickListener(this);
        } else {
            mEditTv.setVisibility(View.GONE);
        }

        loadImgaeFromSysDb();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.RButton) {
            finish();
        } else if (i == R.id.title) {
            if (null != mCustomSelectPhotoPopWindow && !mCustomSelectPhotoPopWindow.isShowing()) {
                mCustomSelectPhotoPopWindow.showAsDropDown(mPhotoTitleTv, 0, 0);
            }
        } else if (i == R.id.finishSelect) {
            if (null == mHasSelectedImgs || mHasSelectedImgs.size() <= 0) {
                finish();
                return;
            }
            showLoadingDialog();
            ExecutorSupport.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(SelectMorePhotoActivity.this, getIntent().getClass());
                    ArrayList<String> path = new ArrayList<String>();
                    for (SelectImgVo item : mHasSelectedImgs) {
                        String thumbPath = ImageUtil.compressImage(item.path);
                        if (mIsAddWaterMark) {
                            ImageUtil.addWaterRemark(
                                    thumbPath,
                                    "water_remark_icon.png",
                                    getResources().getDimensionPixelSize(R.dimen.x24),
                                    Color.YELLOW, Color.BLACK);
                        }
                        path.add(thumbPath);
                    }
                    in.putStringArrayListExtra(SELECT_IV_LIST, path);
                    setResult(Activity.RESULT_OK, in);
                    dismissLoadingDialog();
                    finish();
                }
            });
        } else if (i == R.id.edit) {
            if (null == mHasSelectedImgs || mHasSelectedImgs.size() <= 0) {
                return;
            }
            showLoadingDialog();
            ExecutorSupport.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    String thumbPath = ImageUtil.compressImage(mHasSelectedImgs.get(0).path);
                    Intent in = new Intent(SelectMorePhotoActivity.this, EditImageActivity.class);
                    in.putExtra(EditImageActivity.IMAGE_PATH, thumbPath);
                    in.putExtra(EditImageActivity.IS_ADD_WATER, mIsAddWaterMark);
                    startActivityForResult(in, EditImageActivity.REQUEST_CODE);
                    dismissLoadingDialog();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (EditImageActivity.REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                if (null == data.getExtras()) {
                    return;
                }
                String item = data.getExtras().getString(EditImageActivity.RESULT_PATH);
                if (TextUtils.isEmpty(item)) {
                    return;
                }
                ArrayList<String> paths = new ArrayList<String>();
                paths.add(item);
                Intent in = getIntent();
                in.putStringArrayListExtra(SELECT_IV_LIST, paths);
                setResult(Activity.RESULT_OK, in);
                finish();
            }
        }
    }

    /**
     * 加载系统图片
     */
    private void loadImgaeFromSysDb() {
        ExecutorSupport.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolver = getContentResolver();
                Cursor cursor = null;
                File picFile;
                try {
                    cursor = resolver.query(Images.Media.EXTERNAL_CONTENT_URI,
                            new String[] { Images.Media.DATA }, null, null,
                            Images.Media.DATE_MODIFIED + " DESC");
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            String imagePath = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
                            picFile = new File(imagePath);
                            if (!TextUtils.isEmpty(imagePath) && picFile.exists()) {
                                String parentPathName = picFile.getParentFile().getName();
                                if (!FrameworkConst.IMAGE_DIRECTORY.equals(parentPathName)
                                        && !FrameworkConst.IMAGE_COMPRESS_DIRECTORY.equals(parentPathName)) {
                                    SelectImgVo mSelectImgVo = new SelectImgVo();
                                    mSelectImgVo.path = imagePath;
                                    mSelectImgVo.isSelect = false;
                                    if (mAllImgMap.containsKey(parentPathName)) {
                                        List<SelectImgVo> voList = mAllImgMap.get(parentPathName);
                                        voList.add(mSelectImgVo);
                                    } else {
                                        List<SelectImgVo> voList = new ArrayList<SelectImgVo>();
                                        voList.add(mSelectImgVo);
                                        mAllImgMap.put(parentPathName, voList);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    mHandler.sendEmptyMessage(REFRESH_IMAGE);
                }
            }
        });

    }

    /**
     * 初始化标题以及列表数据
     * @param key
     */
    private void initImageGridView(String key) {
        if (mPhotoTitleTv.getText().toString().equals(key)) {
            return;
        }
        mPhotoTitleTv.setText(key);
        if (null == mSelectPhotoAdapter) {
            mSelectPhotoAdapter = new SelectPhotoAdapter(SelectMorePhotoActivity.this, null, R.layout.select_more_item);
            mGirdView.setAdapter(mSelectPhotoAdapter);
        }
        mSelectPhotoAdapter.updateData(mAllImgMap.get(key));
    }

    /**
     * 列表事件
     */
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (null == mSelectPhotoAdapter) {
                return;
            }
            SelectImgVo imgInfo = mSelectPhotoAdapter.getItem(position);
            if (null == imgInfo) {
                return;
            }
            ImageView selectIv = (ImageView) view.findViewById(R.id.select_btn_iv);
            ImageView statusIv = (ImageView) view.findViewById(R.id.status);
            int totalCount = null == mHasSelectedImgs ? 0 : mHasSelectedImgs.size();
            if (imgInfo.isSelect) {
                imgInfo.isSelect = false;
                selectIv.setImageResource(R.drawable.btn_selectimg_nor);
                statusIv.setVisibility(View.GONE);
                totalCount--;
                mHasSelectedImgs.remove(imgInfo);
            } else {
                if (totalCount >= mMaxSelectCount) {
                    show(String.format(getString(R.string.select_max_size), String.valueOf(mMaxSelectCount)));
                    return;
                }
                totalCount++;
                imgInfo.isSelect = true;
                selectIv.setImageResource(R.drawable.btn_selectimg_pre);
                statusIv.setVisibility(View.VISIBLE);
                mHasSelectedImgs.add(imgInfo);
            }
            if (mIsEditAble) {
                if (1 == totalCount) {//数量只有一个时可以编辑
                    mEditTv.setEnabled(true);
                    mEditTv.setTextColor(getResources().getColor(R.color.gray1));
                } else {
                    mEditTv.setEnabled(false);
                    mEditTv.setTextColor(getResources().getColor(R.color.grayC));
                }
            }
            mFinishTv.setText(String.format(getString(R.string.finish_num), Integer.toString(totalCount)));
        }
    };


    /**
     * 弹窗事件
     */
    private OnItemClickListener mPopItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            View v = view.findViewById(R.id.photo_select_title);
            SelectPhotoPopVo vo = (SelectPhotoPopVo) v.getTag();
            initImageGridView(vo.dirName);
            mCustomSelectPhotoPopWindow.dismiss();
        }
    };
}
