import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadForeignFormComponent } from './upload-foreign-form.component';

describe('UploadForeignFormComponent', () => {
  let component: UploadForeignFormComponent;
  let fixture: ComponentFixture<UploadForeignFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadForeignFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadForeignFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
