/*
 * Copyright (c) 2019 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.rimet.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ToastUtil;
import com.sky.xposed.rimet.R;
import com.sky.xposed.rimet.ui.adapter.SearchResultAdapter;
import com.sky.xposed.rimet.ui.util.MapUtil;
import com.sky.xposed.rimet.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2019/3/26.
 */
public class MapActivity extends Activity implements LocationSource, AdapterView.OnItemClickListener {

    private MapView mMapView;
    private ListView mListView;
    private AMap mAMap;
    private SearchResultAdapter mSearchResultAdapter;

    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;

    private GeocodeSearch mGeocodeSearch;
    private PoiSearch.Query mQuery;
    private PoiSearch mPoiSearch;

    private Marker mCurMarker;
    private LatLonPoint mSearchLatLonPoint;
    private Marker mLocationMarker;
    private boolean isItemClickAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mListView = findViewById(R.id.list_view);
        mSearchResultAdapter = new SearchResultAdapter(this);
        mListView.setAdapter(mSearchResultAdapter);
        mListView.setOnItemClickListener(this);

        mAMap = mMapView.getMap();

        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.setLocationSource(this);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);
        mAMap.setMyLocationEnabled(true);

        mAMap.setOnCameraChangeListener(new MyOnCameraChangeListener());
        mAMap.setOnMapLoadedListener(new MyOnMapLoadedListener());

        mGeocodeSearch = new GeocodeSearch(getApplicationContext());
        mGeocodeSearch.setOnGeocodeSearchListener(new MyOnGeocodeSearchListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (android.R.id.home == itemId) {
            // 退出
            onBackPressed();
            return true;
        } else if (R.id.menu_ok == itemId) {
            // 确定
            return true;
        } else if (R.id.menu_search == itemId) {
            // 搜索
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if(null != mAMapLocationClient){
            mAMapLocationClient.onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position != mSearchResultAdapter.getSelectedPosition()) {

            PoiItem poiItem = mSearchResultAdapter.getItem(position);
            LatLng latLng = MapUtil.newLatLng(poiItem.getLatLonPoint());

            isItemClickAction = true;

            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

            mSearchResultAdapter.setSelectedPosition(position);
            mSearchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

        mOnLocationChangedListener = onLocationChangedListener;

        if (mAMapLocationClient == null) {

            mAMapLocationClient = new AMapLocationClient(getApplicationContext());
            mAMapLocationClientOption = new AMapLocationClientOption();

            // 设置定位参数,只定位一次
            mAMapLocationClientOption.setOnceLocation(true);
            mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mAMapLocationClient.setLocationListener(new MyAMapLocationListener());
            mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
            mAMapLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {

        mOnLocationChangedListener = null;

        if (mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
    }

    private void addMarkerInScreenCenter(LatLng locationLatLng) {

        LatLng latLng = mAMap.getCameraPosition().target;
        Point screenPosition = mAMap.getProjection().toScreenLocation(latLng);
        mLocationMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));

        //设置Marker在屏幕上,不跟随地图移动
        mLocationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        mLocationMarker.setZIndex(1);
    }

    private void geocodeAddress() {

        if (mSearchLatLonPoint == null) return;

        RegeocodeQuery query = new RegeocodeQuery(mSearchLatLonPoint, 2000, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * 移动中心位置的标记
     */
    private void moveLocationMarker() {

        if (mLocationMarker == null) return;

        // 根据屏幕距离计算需要移动的目标点
        LatLng latLng = mAMap.getCameraPosition().target;
        Point screenPosition =  mAMap.getProjection().toScreenLocation(latLng);

        mLocationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }

    /**
     * 搜索Poi信息
     * @param cityCode
     */
    private void doSearchQuery(String cityCode) {

        if (mSearchLatLonPoint == null) return;

        mQuery = new PoiSearch.Query("", "", cityCode);
        mQuery.setCityLimit(true);
        mQuery.setPageSize(20);
        mQuery.setPageNum(0);

        mPoiSearch = new PoiSearch(this, mQuery);
        mPoiSearch.setOnPoiSearchListener(new MyOnPoiSearchListener());
        mPoiSearch.setBound(new PoiSearch.SearchBound(mSearchLatLonPoint, 1000, true));
        mPoiSearch.searchPOIAsyn();
    }

    /**
     * 定位回调的监听
     */
    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (mOnLocationChangedListener == null || mAMapLocationClient == null
                    || aMapLocation == null || aMapLocation.getErrorCode() != 0) {
                // 定位失败了
                ToastUtil.show("定位失败！");
                return;
            }

            // 当前位置
            LatLng curLatLng = MapUtil.newLatLng(aMapLocation);

            // 通知位置信息
            mOnLocationChangedListener.onLocationChanged(aMapLocation);

            if (mCurMarker == null) {
                // 设置当前位置
                mCurMarker = mAMap.addMarker(new MarkerOptions()
                        .anchor(0.5f,0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));
            }

            // 移动到指定位置
            mCurMarker.setPosition(curLatLng);

            // 移动到当前位置
            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 16f));
        }
    }

    private class MyOnCameraChangeListener implements AMap.OnCameraChangeListener {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {

        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {

            mSearchLatLonPoint = MapUtil.newLocation(cameraPosition.target);

            if (!isItemClickAction) {
                moveLocationMarker();
                geocodeAddress();
            }
            isItemClickAction = false;
        }
    }

    /**
     * 地图加载监听
     */
    private class MyOnMapLoadedListener implements AMap.OnMapLoadedListener {

        @Override
        public void onMapLoaded() {
            addMarkerInScreenCenter(null);
        }
    }

    private class MyOnGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {

        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int resultCode) {

            if (resultCode != AMapException.CODE_AMAP_SUCCESS) {
                Alog.e("搜索出错了");
                return;
            }

            if (result == null
                    || result.getRegeocodeAddress() == null
                    || result.getRegeocodeAddress().getPois() == null) {
                Alog.e("没有搜索结果");
                return;
            }

            RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();

            // 当前位置
            PoiItem curPoiItem = new PoiItem(
                    "regeo", mSearchLatLonPoint, "标记的位置", regeocodeAddress.getFormatAddress());


            List<PoiItem> tmpList = new ArrayList<>();
            tmpList.add(curPoiItem);
            tmpList.addAll(regeocodeAddress.getPois());

            mSearchResultAdapter.setBeginAddress(regeocodeAddress.getProvince() + regeocodeAddress.getCity() + regeocodeAddress.getDistrict());
            mSearchResultAdapter.setSelectedPosition(0);
            mSearchResultAdapter.setItems(tmpList);
            mSearchResultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {

        }
    }

    /**
     * Poi搜索监听
     */
    private class MyOnPoiSearchListener implements PoiSearch.OnPoiSearchListener {

        @Override
        public void onPoiSearched(PoiResult poiResult, int resultCode) {

            if (resultCode != AMapException.CODE_AMAP_SUCCESS) {
                Alog.e("搜索出错了");
                return;
            }

            if (poiResult == null || poiResult.getQuery() == null) {
                Alog.e("没有搜索到结果");
                return;
            }

            List<PoiItem> tmpList = new ArrayList<>();
//            tmpList.add(mFirstItem);

            if (!CollectionUtil.isEmpty(poiResult.getPois())) {
                // 添加全部的信息
                tmpList.addAll(poiResult.getPois());
            }

            mSearchResultAdapter.setSelectedPosition(0);
            mSearchResultAdapter.setItems(tmpList);
            mSearchResultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {
        }
    }
}
