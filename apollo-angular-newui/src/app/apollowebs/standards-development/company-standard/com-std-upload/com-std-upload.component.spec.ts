import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdUploadComponent } from './com-std-upload.component';

describe('ComStdUploadComponent', () => {
  let component: ComStdUploadComponent;
  let fixture: ComponentFixture<ComStdUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdUploadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
