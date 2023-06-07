import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadTcMemberComponentComponent } from './upload-tc-member-component.component';

describe('UploadTcMemberComponentComponent', () => {
  let component: UploadTcMemberComponentComponent;
  let fixture: ComponentFixture<UploadTcMemberComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadTcMemberComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadTcMemberComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
