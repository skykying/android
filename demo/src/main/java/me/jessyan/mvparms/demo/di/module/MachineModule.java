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
package me.jessyan.mvparms.demo.di.module;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MachineContract;
import me.jessyan.mvparms.demo.mvp.contract.UserContract;
import me.jessyan.mvparms.demo.mvp.model.MachineModel;
import me.jessyan.mvparms.demo.mvp.model.UserModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Machine;
import me.jessyan.mvparms.demo.mvp.model.entity.User;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MachineAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserAdapter;


@Module
public abstract class MachineModule {

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(MachineContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(MachineContract.View view) {
        return new GridLayoutManager(view.getActivity(), 2);
    }

    @ActivityScope
    @Provides
    static List<Machine> provideMachineList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static DefaultAdapter provideMachineAdapter(List<Machine> list) {
        return new MachineAdapter(list);
    }

    @Binds
    abstract MachineContract.Model bindMachineModel(MachineModel model);
}
