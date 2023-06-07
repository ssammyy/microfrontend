import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NepSuccessComponent } from './nep-success.component';

describe('NepSuccessComponent', () => {
  let component: NepSuccessComponent;
  let fixture: ComponentFixture<NepSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NepSuccessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NepSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
