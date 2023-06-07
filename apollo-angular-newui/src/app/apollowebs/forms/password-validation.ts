import {AbstractControl} from '@angular/forms';

export class PasswordValidation {

    static MatchPassword(AC: AbstractControl) {
        const password = AC.get('password').value; // to get value in input tag
        const confirmPassword = AC.get('confirmPassword').value; // to get value in input tag
        if (password !== confirmPassword) {
            AC.get('confirmPassword').setErrors({MatchPassword: true});
        } else {
            return null;
        }
    }
    static MatchCredentials(AC: AbstractControl) {
        const password = AC.get('credentials').value; // to get value in input tag
        const confirmPassword = AC.get('confirmCredentials').value; // to get value in input tag
        if (password !== confirmPassword) {
            AC.get('confirmCredentials').setErrors({MatchCredentials: true});
        } else {
            return null;
        }
    }
}
