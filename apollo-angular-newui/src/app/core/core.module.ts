import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoresModule} from "./store/stores.module";
import {ServiceModule} from "./services/service.module";
import {HttpInterceptorModule} from "./interceptor/http-interceptor.module";
import {HttpClientModule} from "@angular/common/http";
import {RouteGuardModule} from "./route-guard/route-guard.module";
import {SharedModule} from "./shared/shared.module";

const MODULES = [
  HttpClientModule,
  CommonModule,
  StoresModule,
  ServiceModule,
    SharedModule,
  HttpInterceptorModule,
  RouteGuardModule
]
let PROVIDERS: any[];
PROVIDERS = [];


@NgModule({
  declarations: [
  ],
  imports: MODULES,
  exports: MODULES,
  providers: PROVIDERS
})
export class CoreModule { }
