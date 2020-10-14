
package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;
import me.jessyan.mvparms.demo.di.module.UserModule;
import me.jessyan.mvparms.demo.mvp.contract.UserContract;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity;


@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        UserComponent.Builder view(UserContract.View view);

        UserComponent.Builder appComponent(AppComponent appComponent);

        UserComponent build();
    }
}
