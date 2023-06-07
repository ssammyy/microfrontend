import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DmarkAllAwardedApplicationsComponent} from './dmark-all-awarded-applications.component';

describe('DmarkAllAwardedApplicationsComponent', () => {
    let component: DmarkAllAwardedApplicationsComponent;
    let fixture: ComponentFixture<DmarkAllAwardedApplicationsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DmarkAllAwardedApplicationsComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(DmarkAllAwardedApplicationsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
