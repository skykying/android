/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.coboltforge.dontmind.xbox.ui.VMMainActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.MachineContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Machine;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;


@ActivityScope
public class MachinePresenter extends BasePresenter<MachineContract.Model, MachineContract.View> implements DefaultAdapter.OnRecyclerViewItemClickListener {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    List<Machine> mMachines;
    @Inject
    DefaultAdapter mAdapter;
    private int lastUserId = 1;
    private boolean isFirst = true;
    private int preEndIndex;


    @Inject
    public MachinePresenter(MachineContract.Model model, MachineContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link ComponentActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        requestMachines(true);//打开 App 时自动加载列表
        mAdapter.setOnItemClickListener(this);
    }

    public void requestMachines(final boolean pullToRefresh) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                requestFromModel(pullToRefresh);
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    private void requestFromModel(boolean pullToRefresh) {
        if (pullToRefresh) {
            lastUserId = 1;//下拉刷新默认只请求第一页
        }

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        boolean isEvictCache = pullToRefresh;//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }

        mModel.getMachines(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Machine>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Machine> machines) {
                        lastUserId = machines.get(machines.size() - 1).getId();//记录最后一个id,用于下一次请求
                        if (pullToRefresh) {
                            mMachines.clear();//如果是下拉刷新则清空列表
                        }
                        preEndIndex = mMachines.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mMachines.addAll(machines);
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(preEndIndex, machines.size());
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mMachines = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    @Override
    public void onItemClick(@NonNull View view, int viewType, @NonNull Object data, int position) {
        Timber.tag(TAG).w("999999999999999999999999");
        ArmsUtils.startActivity(VMMainActivity.class);
    }
}
