import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManageCorporateCustomerComponent} from './manage-corporate-customer.component';

describe('ManageCorporateCustomerComponent', () => {
    let component: ManageCorporateCustomerComponent;
    let fixture: ComponentFixture<ManageCorporateCustomerComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ManageCorporateCustomerComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(ManageCorporateCustomerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
