import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadSacSummaryComponent } from './upload-sac-summary.component';

describe('UploadSacSummaryComponent', () => {
  let component: UploadSacSummaryComponent;
  let fixture: ComponentFixture<UploadSacSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadSacSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadSacSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
