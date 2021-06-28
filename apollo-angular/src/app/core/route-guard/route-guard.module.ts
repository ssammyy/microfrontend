import {NgModule} from '@angular/core';
import {RouteGuard} from "./route.guard";


const PROVIDERS = [
  RouteGuard
];

@NgModule({
  providers: PROVIDERS
})
export class RouteGuardModule {
}
