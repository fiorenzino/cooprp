import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AppRoutingModule} from "./app.routes.module";
import {MySharedModule} from "./modules/my-shared.module";
import {CoreModule} from "./modules/core.module";
import {BasicHttpInterceptor} from "./services/http-interceptors/basic-http-interceptor";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HomeComponent} from "./components/home/home.component";
import {TableModule} from "primeng/table";
import {
    ButtonModule,
    CalendarModule, CheckboxModule,
    ConfirmDialogModule, DataScrollerModule,
    DialogModule,
    DropdownModule,
    GrowlModule,
    InputSwitchModule,
    InputTextareaModule,
    InputTextModule,
    MessagesModule, OverlayPanelModule,
    PanelModule, ProgressBarModule,
    ProgressSpinnerModule, SelectButtonModule, SharedModule,
    SpinnerModule,
    TabViewModule, ToggleButtonModule
} from "primeng/primeng";
import {MainAppComponent} from "./components/main-app/main-app.component";
import {InlineProfileComponent} from "./app.profile.component";
import {AppTopBar} from "./app.topbar.component";
import {AppFooter} from "./app.footer.component";
import {AppMenuComponent, AppSubMenu} from "./app.menu.component";
import {LanguagesListComponent} from "./components/languages/languages-list.component";
import {LanguagesService} from "./services/languages.service";
import {LanguagesComponent} from "./components/languages/languages.component";
import {WorkshiftsService} from "./services/workshifts.service";
import {OperationsService} from "./services/operations.service";
import {NotificationsService} from "./services/notifications.service";
import {CompanyConfigurationsService} from "./services/companyConfigurations.service";
import {LocationsService} from "./services/locations.service";
import {CompanyConfigurationsListComponent} from "./components/companyconfigurations/companyConfigurations-list.component";
import {CompanyConfigurationsComponent} from "./components/companyconfigurations/companyConfigurations.component";
import {LocationsListComponent} from "./components/locations/locations-list.component";
import {LocationsComponent} from "./components/locations/locations.component";
import {OperationsListComponent} from "./components/operations/operations-list.component";
import {WorkshiftsListComponent} from "./components/workshifts/workshifts-list.component";
import {WorkshiftsComponent} from "./components/workshifts/workshifts.component";
import {OperationsComponent} from "./components/operations/operations.component";
import {NotificationsListComponent} from "./components/notifications/notifications-list.component";
import {NotificationsComponent} from "./components/notifications/notifications.component";

@NgModule({
	declarations: [
		AppComponent,
		MainAppComponent,
		HomeComponent,
        AppTopBar,
        AppFooter,
		InlineProfileComponent,
        AppMenuComponent,
        AppSubMenu,


        CompanyConfigurationsListComponent,
        CompanyConfigurationsComponent,

        LanguagesListComponent,
        LanguagesComponent,

        LocationsListComponent,
        LocationsComponent,

        OperationsListComponent,
        OperationsComponent,

        NotificationsListComponent,
        NotificationsComponent,

        WorkshiftsListComponent,
        WorkshiftsComponent,
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		MySharedModule,
		AppRoutingModule,
		CoreModule.forRoot(),
		TableModule,
		CalendarModule,

        SpinnerModule,
        ProgressSpinnerModule,
        MessagesModule,
        CalendarModule,
        ConfirmDialogModule,
        DropdownModule,
        InputTextModule,
        InputTextareaModule,
        SharedModule,
        TableModule,
        DialogModule,
        GrowlModule,
        PanelModule,
        TabViewModule,
        InputSwitchModule,
        ButtonModule,
        ProgressBarModule,
        DataScrollerModule,
        CheckboxModule,
        OverlayPanelModule,
        SelectButtonModule,
        ToggleButtonModule
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: BasicHttpInterceptor,
			multi: true
		},
        CompanyConfigurationsService,
        LanguagesService,
        LocationsService,
        NotificationsService,
        OperationsService,
        WorkshiftsService
	],
	entryComponents: [
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
