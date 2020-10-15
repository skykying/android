
package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MachineModule;
import me.jessyan.mvparms.demo.mvp.contract.MachineContract;
import me.jessyan.mvparms.demo.mvp.ui.activity.MachineActivity;


@ActivityScope
@Component(modules = MachineModule.class, dependencies = AppComponent.class)
public interface MachineComponent {
    void inject(MachineActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MachineComponent.Builder view(MachineContract.View view);

        MachineComponent.Builder appComponent(AppComponent appComponent);

        MachineComponent build();
    }
}
