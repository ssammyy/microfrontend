import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SmarkAllAwardedApplicationsComponent} from './smark-all-awarded-applications.component';

describe('SmarkAllAwardedApplicationsComponent', () => {
    let component: SmarkAllAwardedApplicationsComponent;
    let fixture: ComponentFixture<SmarkAllAwardedApplicationsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SmarkAllAwardedApplicationsComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(SmarkAllAwardedApplicationsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
