import {createAction, props} from '@ngrx/store';

export const loadRouters = createAction(
  '[Router] Load Routers'
);

export const Go = createAction(
  '[Router] Go',
  props<{ payload: any, link: string, redirectUrl: string }>()
);

export const Back = createAction(
  '[Router] Back'
);

export const Forward = createAction(
  '[Router] Forward'
);




