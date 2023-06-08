import {ComponentFixture, TestBed} from '@angular/core/testing';

import {QaTaskDetailsComponent} from './qa-task-details.component';

describe('QaTaskDetailsComponent', () => {
    let component: QaTaskDetailsComponent;
    let fixture: ComponentFixture<QaTaskDetailsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [QaTaskDetailsComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(QaTaskDetailsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
