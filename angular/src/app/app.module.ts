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
import {CalendarModule} from "primeng/primeng";
import {MainAppComponent} from "./components/main-app/main-app.component";
import {InlineProfileComponent} from "./app.profile.component";
import {AppTopBar} from "./app.topbar.component";
import {AppFooter} from "./app.footer.component";
import {AppMenuComponent, AppSubMenu} from "./app.menu.component";

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

	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		MySharedModule,
		AppRoutingModule,
		CoreModule.forRoot(),
		TableModule,
		CalendarModule
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: BasicHttpInterceptor,
			multi: true
		}
	],
	entryComponents: [
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
