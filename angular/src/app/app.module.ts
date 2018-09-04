import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AppRoutingModule} from "./app.routes.module";
import {MySharedModule} from "./modules/my-shared.module";
import {CoreModule} from "./modules/core.module";
import {BasicHttpInterceptor} from "./services/http-interceptors/basic-http-interceptor";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MainHeaderComponent} from "./shared/main-header/main-header.component";
import {MainFooterComponent} from "./shared/main-footer/main-footer.component";
import {HomeComponent} from "./components/home/home.component";
import {TableModule} from "primeng/table";
import {CalendarModule} from "primeng/primeng";
import {MainAppComponent} from "./components/main-app/main-app.component";

@NgModule({
	declarations: [
		AppComponent,
		MainAppComponent,
		MainHeaderComponent,
		MainFooterComponent,
		HomeComponent,
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
