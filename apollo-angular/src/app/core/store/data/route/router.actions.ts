import {createAction, props} from '@ngrx/store';

export const loadRouters = createAction(
  '[Router] Load Routers'
);

export const Go = createAction(
  '[Router] Go',
  props<{ payload: any, link: string }>()
);




