import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DefaultDataServiceConfig, EntityDataModuleConfig, EntityMetadataMap, PropsFilterFnFactory} from "@ngrx/data";
import {ApiEndpointService} from "../services/endpoints/api-endpoint.service";


export const entityMetadata: EntityMetadataMap = {
  RegistrationPayload: {
    filterFn: activeFilter,
  },
  BrsLookUpRequest: {
    filterFn: activeFilter,
  },
  SendTokenToPhone: {
    filterFn: activeFilter,
  },
  ValidateTokenAndPhone: {
    filterFn: activeFilter,
  },
  BusinessNatures: {
    filterFn: activeFilter,
  },
  BusinessLines: {
    filterFn: activeFilter,
  },
  Region: {
    filterFn: activeFilter,
  },
  County: {
    filterFn: activeFilter,
  },
  Town: {
    filterFn: activeFilter,
  },
};

export const entityConfig: EntityDataModuleConfig = {
  entityMetadata
};

export const defaultDataServiceConfig: DefaultDataServiceConfig = {
  root: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.HOME_PAGE),
  timeout: 15000, // request timeout
  entityHttpResourceUrls: {
    RegistrationPayload: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REGISTER_COMPANY),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REGISTER_COMPANY)
    },
    BrsLookUpRequest: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_BRS),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_BRS)
    },
    SendTokenToPhone: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SEND_TOKEN),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SEND_TOKEN)
    },
    ValidateTokenAndPhone: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_TOKEN),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_TOKEN)
    },
    BusinessNatures: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.BUSINESS_NATURES_LIST),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.BUSINESS_NATURES_LIST)
    },
    BusinessLines: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.BUSINESS_LINES_LIST),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.BUSINESS_LINES_LIST)
    },
    Region: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REGIONS_LIST),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REGIONS_LIST)
    },
    County: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.COUNTY_LIST),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.COUNTY_LIST)
    },
    Town: {
      entityResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.TOWN_LIST),
      collectionResourceUrl: ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.TOWN_LIST)
    },
  }
}


/**
 * Filter for entities whose name matches the case-insensitive pattern
 * The filter works on ALL properties
 * @return any[]
 * @param entities list of entities
 * @param pattern pattern to filter against
 */
export function textFilter<T extends { title: string; description: string }>(
  entities: T[],
  pattern: string
) {
  return PropsFilterFnFactory(['title', 'description'])(entities, pattern);
}

/**
 * Compare and sort
 * @returns number
 * @param a first title
 * @param b second title
 */
export function sortByTitle(
  a: { title: string },
  b: { title: string }
): number {
  return a.title.localeCompare(b.title);
}

/**
 * Compare and sort
 * @returns number
 * @param a first title
 * @param b second title
 */
export function sortById(
  a: { id: number },
  b: { id: number }
): number {
  /**
   * TODO; Fix to sort as a number
   */
  return a.id.toString().localeCompare(b.id.toString());
}

export function activeFilter(entities: { active: boolean }[], search: boolean) {
  return entities.filter(e => e.active === search);
}


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [{provide: DefaultDataServiceConfig, useValue: defaultDataServiceConfig}]
})
export class EntityStoreModule {

}
