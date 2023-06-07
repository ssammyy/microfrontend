import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PvocNewComplaintComponent} from './pvoc-new-complaint.component';

describe('PvocNewComplaintComponent', () => {
  let component: PvocNewComplaintComponent;
  let fixture: ComponentFixture<PvocNewComplaintComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PvocNewComplaintComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PvocNewComplaintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
