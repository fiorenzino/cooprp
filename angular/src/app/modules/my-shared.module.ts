import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {ConfirmDialogModule} from "primeng/primeng";
import {ButtonModule} from "primeng/button";

@NgModule({
	imports: [
		CommonModule,
		RouterModule,
		FormsModule,
		ReactiveFormsModule,
		HttpClientModule,
		ConfirmDialogModule,
		ButtonModule
	],
	declarations: [],
	exports: [
		CommonModule,
		RouterModule,
		FormsModule,
		ReactiveFormsModule,
		HttpClientModule,
		ConfirmDialogModule,
		ButtonModule
	],
	entryComponents: []
})
export class MySharedModule {

}
