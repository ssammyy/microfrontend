import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FmarkAllAwardedApplicationsComponent} from './fmark-all-awarded-applications.component';

describe('FmarkAllAwardedApplicationsComponent', () => {
    let component: FmarkAllAwardedApplicationsComponent;
    let fixture: ComponentFixture<FmarkAllAwardedApplicationsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [FmarkAllAwardedApplicationsComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(FmarkAllAwardedApplicationsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
