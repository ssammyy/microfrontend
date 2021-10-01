import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdConfirmComponent } from './com-std-confirm.component';

describe('ComStdConfirmComponent', () => {
  let component: ComStdConfirmComponent;
  let fixture: ComponentFixture<ComStdConfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdConfirmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
