import {ModuleWithProviders, NgModule, Optional, SkipSelf} from "@angular/core";
import {UserResolveGuard} from "../router-guards/user-resolve-guard";
import {AuthenticationService} from "../services/authentication.service";
import {TopMenuService} from "../services/top-menu.service";
import {ConfirmationService} from "primeng/api";
import {LoginService} from "../services/login.service";

@NgModule({
	imports: []
})

export class CoreModule {

	constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
		if (parentModule) {
			throw new Error(
				'CoreModule is already loaded. Import it in the AppModule only');
		}
	}

	static forRoot(): ModuleWithProviders {
		return {
			ngModule: CoreModule,
			providers: [
				ConfirmationService,
				AuthenticationService,
				LoginService,

				UserResolveGuard,
				TopMenuService
			]
		};
	}
}

