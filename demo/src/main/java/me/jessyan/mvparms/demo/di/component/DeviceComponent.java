
package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DeviceModule;
import me.jessyan.mvparms.demo.di.module.UserModule;
import me.jessyan.mvparms.demo.mvp.contract.DeviceContract;
import me.jessyan.mvparms.demo.mvp.contract.UserContract;
import me.jessyan.mvparms.demo.mvp.ui.activity.DeviceActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity;


@ActivityScope
@Component(modules = DeviceModule.class, dependencies = AppComponent.class)
public interface DeviceComponent {
    void inject(DeviceActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceComponent.Builder view(DeviceContract.View view);

        DeviceComponent.Builder appComponent(AppComponent appComponent);

        DeviceComponent build();
    }
}
