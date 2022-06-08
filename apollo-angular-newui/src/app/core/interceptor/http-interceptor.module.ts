import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AuthInterceptor} from './auth.interceptor';


const PROVIDERS = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  },
];

@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: PROVIDERS,
})
export class HttpInterceptorModule { }
