import {ComponentFixture, TestBed} from '@angular/core/testing';

import {QrCodeDetailsComponent} from './qr-code-details.component';

describe('QrCodeDetailsComponent', () => {
    let component: QrCodeDetailsComponent;
    let fixture: ComponentFixture<QrCodeDetailsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [QrCodeDetailsComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(QrCodeDetailsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
