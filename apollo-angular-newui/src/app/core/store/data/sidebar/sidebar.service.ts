import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from '@ngrx/data';
import {SideBarMainMenus} from './sidebar.model';

@Injectable({
    providedIn: 'root'
})
export class SidebarService extends EntityCollectionServiceBase<SideBarMainMenus> {
    constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
        super('SideBarMainMenus', serviceElementsFactory);
    }
}

