import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from "@ngrx/store";
import {appEffects, appReducer, metaReducers, UsersCustomService} from "./index";
import {StoreRouterConnectingModule} from '@ngrx/router-store';
import {environment} from "../../../environments/environment";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {defaultDataServiceConfig, entityConfig} from './entity-store.module';
import {DefaultDataServiceConfig, EntityDataModule, EntityDataService} from "@ngrx/data";
import {EffectsModule} from "@ngrx/effects";
import {BranchesCustomService} from "./data";
import {TownCustomService} from "./data/town/town-custom.service";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    StoreModule.forRoot(appReducer, {metaReducers}),
    EffectsModule.forRoot(appEffects),
    EntityDataModule.forRoot(entityConfig),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    /**
     * @ngrx/router-store keeps router state up-to-date in the store.
     */
    StoreRouterConnectingModule.forRoot({
      // They stateKey defines the name of the state used by the router-store reducer.
      // This matches the key defined in the map of reducers
      stateKey: 'router'
    }),
    /**
     * Store devtools instrument the store retaining past versions of state
     * and recalculating new states. This enables powerful time-travel
     * debugging.
     *
     * To use the debugger, install the Redux Devtools extension for either
     * Chrome or Firefox
     *
     * See: https://github.com/zalmoxisus/redux-devtools-extension
     */
    StoreDevtoolsModule.instrument({
      name: 'KEBS KIMS Service Tool',
      logOnly: environment.production
    }),

  ],
  providers: [
    {provide: DefaultDataServiceConfig, useValue: defaultDataServiceConfig}
  ],
})
export class StoresModule {
  constructor(
    entityDataService: EntityDataService,
    customBranchesService: BranchesCustomService,
    customUserService: UsersCustomService,
    customTownService: TownCustomService,
  ) {
    // Register custom EntityDataServices
    entityDataService.registerService('Branches', customBranchesService);
    entityDataService.registerService('User', customUserService);
    entityDataService.registerService('Town', customTownService);
  }
}
