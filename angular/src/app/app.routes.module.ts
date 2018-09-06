import {PreloadAllModules, RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {UserResolveGuard} from "./router-guards/user-resolve-guard";
import {HomeComponent} from "./components/home/home.component";
import {MainAppComponent} from "./components/main-app/main-app.component";

export const routing: Routes = [
	{path: '', redirectTo: 'home', pathMatch: 'full'},
	{
		path: '', component: MainAppComponent, resolve: {utente: UserResolveGuard},
		children: [
			{path: '', redirectTo: 'home', pathMatch: 'full'},
			{path: 'home', component: HomeComponent},

			// {path: 'free-page', loadChildren: './modules/free-page.module#FreePageModule'}
		]
	},
];

@NgModule({
	imports: [
		RouterModule.forRoot(routing, {useHash: true, preloadingStrategy: PreloadAllModules}),
	],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
