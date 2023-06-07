import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CdGeneralChecklistComponent} from './cd-general-checklist.component';

describe('CdGeneralChecklistComponent', () => {
  let component: CdGeneralChecklistComponent;
  let fixture: ComponentFixture<CdGeneralChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CdGeneralChecklistComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CdGeneralChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
