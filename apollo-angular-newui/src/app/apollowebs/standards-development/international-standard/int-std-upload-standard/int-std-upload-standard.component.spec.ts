import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdUploadStandardComponent } from './int-std-upload-standard.component';

describe('IntStdUploadStandardComponent', () => {
  let component: IntStdUploadStandardComponent;
  let fixture: ComponentFixture<IntStdUploadStandardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdUploadStandardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdUploadStandardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
