package com.quezx.analytics.dagger.component;

import com.quezx.analytics.dagger.module.InternetModule;
import com.quezx.analytics.dagger.module.StorageModule;
import com.quezx.analytics.dagger.scope.ApplicationScope;

import com.quezx.analytics.fragment.DashBoardFragment;
import com.quezx.analytics.fragment.ReportsFragment;
import com.quezx.analytics.ui.activity.AddCategory;
import com.quezx.analytics.ui.activity.AddReports;
import com.quezx.analytics.ui.activity.ChangeDashboards;
import com.quezx.analytics.ui.activity.DetailReportPage;
import com.quezx.analytics.ui.activity.ReportTypeActivity;
import com.quezx.analytics.ui.user.LoginActivity;

import dagger.Component;

@ApplicationScope
@Component(
        modules = {
                InternetModule.class, StorageModule.class,
        }, dependencies = { AppComponent.class }
)

public interface InternetComponent {

    void inject(LoginActivity loginActivity);
    void inject(DashBoardFragment dashBoardFragment);
    void inject(ReportsFragment reportsFragment);
    void inject(ChangeDashboards changeDashboards);
    void inject(ReportTypeActivity reportTypeActivity);
    void inject(DetailReportPage detailReportPage);
    void inject(AddReports addReports);
    void inject(AddCategory addCategory);

}
