import {Component, OnInit} from '@angular/core';
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	public username: string;
	public password: string;
	public mainForm: FormGroup;

	constructor(private loginService: LoginService,
				private fb: FormBuilder,
				private router: Router) {
	}

	ngOnInit() {
		this.buildForm();
		//
		// console.log(this.router.config);
		// this.router.config.forEach(r =>  {
		// 	if(r.path == 'app') {
		// 		r.children.push({path: 'home', component: HomeComponent});
		// 	}
		// });
		// console.log(this.router.config);
	}

	private buildForm() {
		this.mainForm = this.fb.group({
			username: [this.username, Validators.required],
			password: [this.password, Validators.required],
		});
	}

	public login() {
		// this.loginService.login()
		this.router.navigate(['/app']);
	}
}
