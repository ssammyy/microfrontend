import {Component} from '@angular/core';

import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {PasswordValidation} from "./password-validation";

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'app-forms',
    templateUrl: './forms.component.html',
    styleUrls: ['./forms.component.css']
})
export class FormsComponent {
    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    validEmailRegister = false;
    validConfirmPasswordRegister = false;
    validPasswordRegister = false;

    validEmailLogin = false;
    validPasswordLogin = false;

    validTextType = false;
    validEmailType = false;
    validNumberType = false;
    validUrlType = false;
    pattern = 'https?://.+';
    validSourceType = false;
    validDestinationType = false;

    matcher = new MyErrorStateMatcher();
    register: FormGroup;
    login: FormGroup;
    type: FormGroup;

    constructor(private formBuilder: FormBuilder) {
    }

    isFieldValid(form: FormGroup, field: string) {
        return !form.get(field).valid && form.get(field).touched;
    }

    displayFieldCss(form: FormGroup, field: string) {
        return {
            'has-error': this.isFieldValid(form, field),
            'has-feedback': this.isFieldValid(form, field)
        };
    }

    onRegister() {
        if (this.register.valid) {
        } else {
            this.validateAllFormFields(this.register);
        }
    }

    onLogin() {
        if (this.login.valid) {
        } else {
            this.validateAllFormFields(this.login);
        }
    }

    onType() {
        if (this.type.valid) {
        } else {
            this.validateAllFormFields(this.type);
        }
    }

    validateAllFormFields(formGroup: FormGroup) {
        Object.keys(formGroup.controls).forEach(field => {
            const control = formGroup.get(field);
            if (control instanceof FormControl) {
                control.markAsTouched({onlySelf: true});
            } else if (control instanceof FormGroup) {
                this.validateAllFormFields(control);
            }
        });
    }

// tslint:disable-next-line:use-life-cycle-interface
    ngOnInit() {
        this.register = this.formBuilder.group({
            // tslint:disable-next-line:max-line-length
            // To add a validator, we must first convert the string value into an array. The first item in the array is the default value if any, then the next item in the array is the validator. Here we are adding a required validator meaning that the firstName attribute must have a value in it.
            email: [null, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$')]],
            // tslint:disable-next-line:max-line-length
            // We can use more than one validator per field. If we want to use more than one validator we have to wrap our array of validators with a Validators.compose function. Here we are using a required, minimum length and maximum length validator.
            optionsCheckboxes: ['', Validators.required],
            password: ['', Validators.compose([Validators.required, Validators.minLength(6)])],
            confirmPassword: ['', Validators.required],
        }, {
            validator: PasswordValidation.MatchPassword // your validation method
        });
        this.login = this.formBuilder.group({
            // tslint:disable-next-line:max-line-length
            // To add a validator, we must first convert the string value into an array. The first item in the array is the default value if any, then the next item in the array is the validator. Here we are adding a required validator meaning that the firstName attribute must have a value in it.
            email: [null, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$')]],
            // tslint:disable-next-line:max-line-length
            // We can use more than one validator per field. If we want to use more than one validator we have to wrap our array of validators with a Validators.compose function. Here we are using a required, minimum length and maximum length validator.
            password: ['', Validators.required]
        });
        this.type = this.formBuilder.group({
            // tslint:disable-next-line:max-line-length
            // To add a validator, we must first convert the string value into an array. The first item in the array is the default value if any, then the next item in the array is the validator. Here we are adding a required validator meaning that the firstName attribute must have a value in it.
            text: [null, Validators.required],
            email: [null, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$')]],
            number: [null, Validators.required],
            url: [null, Validators.required],
            // tslint:disable-next-line:max-line-length
            // We can use more than one validator per field. If we want to use more than one validator we have to wrap our array of validators with a Validators.compose function. Here we are using a required, minimum length and maximum length validator.
            password: ['', Validators.required],
            confirmPassword: ['', Validators.required],
        }, {
            validator: PasswordValidation.MatchPassword // your validation method
        });
    }

    emailValidationRegister(e) {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (re.test(String(e).toLowerCase())) {
            this.validEmailRegister = true;
        } else {
            this.validEmailRegister = false;
        }
    }

    passwordValidationRegister(e) {
        if (e.length > 5) {
            this.validPasswordRegister = true;
        } else {
            this.validPasswordRegister = false;
        }
    }

    confirmPasswordValidationRegister(e) {
        if (this.register.controls['password'].value === e) {
            this.validConfirmPasswordRegister = true;
        } else {
            this.validConfirmPasswordRegister = false;
        }
    }

    emailValidationLogin(e) {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        this.validEmailLogin = re.test(String(e).toLowerCase());
    }

    passwordValidationLogin(e) {
        this.validPasswordLogin = e.length > 5;
    }


    textValidationType(e) {
        this.validTextType = !!e;
    }

    emailValidationType(e) {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        this.validEmailType = re.test(String(e).toLowerCase());
    }

    numberValidationType(e) {
        this.validNumberType = !!e;
    }

    urlValidationType(e) {
        try {
            // tslint:disable-next-line:no-unused-expression
            new URL(e);
            this.validUrlType = true;
        } catch (_) {
            this.validUrlType = false;
        }
    }

    sourceValidationType(e) {
        if (e) {
            this.validSourceType = true;
        } else {
            this.validSourceType = false;
        }
    }

    confirmDestinationValidationType(e) {
        if (this.type.controls['password'].value === e) {
            this.validDestinationType = true;
        } else {
            this.validDestinationType = false;
        }
    }

}
