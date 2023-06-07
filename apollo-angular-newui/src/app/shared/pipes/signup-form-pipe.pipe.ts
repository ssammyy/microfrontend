import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'signupFormPipe'
})
export class SignupFormPipePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}
