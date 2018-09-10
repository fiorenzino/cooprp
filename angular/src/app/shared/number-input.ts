import {Component, forwardRef, Input, OnInit} from "@angular/core";
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";
import * as numeral from "numeral";

@Component({
	selector: 'number-input',
	template: `<input maxlength="{{_maxlength}}" (blur)="onBlur()" pInputText pattern="[0-9.,]*"
                      class="{{styleClass}}" placeholder="{{placeholder}}" [(ngModel)]="_numberout">`,
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => NumberInput),
			multi: true
		}
	]
})
export class NumberInput implements OnInit, ControlValueAccessor {

	@Input() maxlength: number;
	@Input() styleClass: string;
	@Input() placeholder: string;

	public _numberout: string;
	private _numberin: number;
	private _onTouchedCallback: (_: any) => {};
	private _onChangeCallback: (_: any) => {};

	constructor() {
	}

	ngOnInit() {
	}

	private translateLocalMessages() {
		numeral.locale('it');
		if (this._numberin != null) {
			this._numberout = numeral(this._numberin).format('0.00');
		}
	}

	public onBlur() {
		if (!this._numberout) {
			this._numberin = null;
			this._onTouchedCallback(this._numberin);
			this._onChangeCallback(this._numberin);
		} else {
			numeral.locale('it');
			let num = numeral(this._numberout);
			if (num) {
				let nnum = num.value();
				this._numberin = Math.round(nnum * 100) / 100;
				this._numberout = numeral(this._numberin).format('0.00');
				this._onTouchedCallback(this._numberin);
				this._onChangeCallback(this._numberin);
			} else {
				this._numberin = null;
				this._numberout = null;
				this._onTouchedCallback(this._numberin);
				this._onChangeCallback(this._numberin);
			}
		}
	}

	writeValue(value: any): void {
		this._numberin = null;
		this._numberout = null;
		if (value != null && value != undefined) {
			this._numberin = value;
			numeral.locale('en');
			const num = numeral(this._numberin);
			numeral.locale('it');
			this._numberout = num.format('0.00');
		}
	}

	registerOnChange(fn: any) {
		this._onChangeCallback = fn;
	}

	registerOnTouched(fn: any) {
		this._onTouchedCallback = fn;
	}

	public get _maxlength(): number {
		return (this.maxlength && this.maxlength > 0 ? this.maxlength : 10);
	}
}