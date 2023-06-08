import {Injectable} from '@angular/core';
import {AbstractControl, FormGroup, ValidationErrors} from '@angular/forms';

export interface AllValidationErrors {
    control_name: string;
    error_name: string;
    error_txt: string;
    error_value: any;
}

export interface FormGroupControls {
    [key: string]: AbstractControl;
}

@Injectable({
    providedIn: 'root',
})
export class ValidationService {

    constructor() {
    }

    getFormValidationErrors(controls: FormGroupControls, controlNames?: any): AllValidationErrors[] {
        let errors: AllValidationErrors[] = [];
        Object.keys(controls).forEach(key => {
            const control = controls[key];
            if (control instanceof FormGroup) {
                errors = errors.concat(this.getFormValidationErrors(control.controls, controlNames));
            }
            const controlErrors: ValidationErrors = controls[key].errors;
            if (controlErrors !== null) {
                Object.keys(controlErrors).forEach(keyError => {
                    let controlName = key;
                    if (controlNames && controlNames[key]) {
                        controlName = controlNames[key];
                    }
                    errors.push({
                        control_name: key,
                        error_txt: this.translateErrorMessage(controlName, keyError, controlErrors[keyError]),
                        error_name: keyError,
                        error_value: controlErrors[keyError],
                    })
                    ;
                });
            }
        });
        return errors;
    }

    translateErrorMessage(control_name: string, error_name: string, error_value: any): string {
        let text;
        switch (error_name) {
            case 'required':
                text = `${control_name} is required!`;
                break;
            case 'pattern':
                text = `${control_name} has wrong pattern!`;
                break;
            case 'email':
                text = `${control_name} has wrong email format!`;
                break;
            case 'minlength':
                text = `${control_name} has wrong length! Required length: ${error_value.requiredLength}`;
                break;
            case 'areEqual':
                text = `${control_name} must be equal!`;
                break;
            default:
                text = `${control_name}: ${error_name}: ${error_value}`;
        }
        return text;
    }

    validateForm(form: FormGroup, controlNames?: any): any {
        const errorList = {};
        if (form.invalid) {
            this.getFormValidationErrors(form.controls, controlNames).forEach(error => {
                errorList[error.control_name] = error.error_txt;
            });
        }
        return errorList;
    }
}
