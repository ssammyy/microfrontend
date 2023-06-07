import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplaintNewComponent } from './complaint-new.component';

describe('ComplaintNewComponent', () => {
  let component: ComplaintNewComponent;
  let fixture: ComponentFixture<ComplaintNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
