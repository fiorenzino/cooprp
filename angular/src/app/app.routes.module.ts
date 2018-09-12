import {PreloadAllModules, RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {UserResolveGuard} from "./router-guards/user-resolve-guard";
import {HomeComponent} from "./components/home/home.component";
import {MainAppComponent} from "./components/main-app/main-app.component";
import {LanguagesComponent} from "./components/languages/languages.component";
import {LanguagesListComponent} from "./components/languages/languages-list.component";
import {LocationsComponent} from "./components/locations/locations.component";
import {LocationsListComponent} from "./components/locations/locations-list.component";
import {NotificationsComponent} from "./components/notifications/notifications.component";
import {NotificationsListComponent} from "./components/notifications/notifications-list.component";
import {OperationsComponent} from "./components/operations/operations.component";
import {OperationsListComponent} from "./components/operations/operations-list.component";
import {CompanyConfigurationsComponent} from "./components/companyconfigurations/companyConfigurations.component";
import {CompanyConfigurationsListComponent} from "./components/companyconfigurations/companyConfigurations-list.component";
import {WorkshiftsListComponent} from "./components/workshifts/workshifts-list.component";
import {WorkshiftsComponent} from "./components/workshifts/workshifts.component";

export const routing: Routes = [
	{path: '', redirectTo: 'home', pathMatch: 'full'},
	{
		path: '', component: MainAppComponent, resolve: {utente: UserResolveGuard},
		children: [
			{path: '', redirectTo: 'home', pathMatch: 'full'},
			{path: 'home', component: HomeComponent},

			// {path: 'free-page', loadChildren: './modules/free-page.module#FreePageModule'}
            {
                path: 'languages',
                component: LanguagesComponent,
                resolve: { user: UserResolveGuard },
                children: [
                    { path: '', redirectTo: '/languages/list', pathMatch: 'full' },
                    { path: 'list', component: LanguagesListComponent },
                ]
            },

            {
                path: 'locations',
                component: LocationsComponent,
                resolve: { user: UserResolveGuard },
                children: [
                    { path: '', redirectTo: '/locations/list', pathMatch: 'full' },
                    { path: 'list', component: LocationsListComponent },
                ]
            },
            {
                path: 'notifications',
                component: NotificationsComponent,
                resolve: { user: UserResolveGuard },
                children: [
                    { path: '', redirectTo: '/notifications/list', pathMatch: 'full' },
                    { path: 'list', component: NotificationsListComponent },
                ]
            },

            {
                path: 'operations',
                component: OperationsComponent,
                resolve: { user: UserResolveGuard },
                children: [
                    { path: '', redirectTo: '/operations/list', pathMatch: 'full' },
                    { path: 'list', component: OperationsListComponent },
                ]
            },
            {
                path: 'workshifts',
                component: WorkshiftsComponent,
                resolve: { user: UserResolveGuard },
                children: [
                    { path: '', redirectTo: '/workshifts/list', pathMatch: 'full' },
                    { path: 'list', component: WorkshiftsListComponent },
                ]
            },
            {
                path: 'companyconfigurations',
                component: CompanyConfigurationsComponent,
                resolve: { user: UserResolveGuard },
                children: [
                    { path: '', redirectTo: '/companyconfigurations/list', pathMatch: 'full' },
                    { path: 'list', component: CompanyConfigurationsListComponent },
                ]
            },
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
