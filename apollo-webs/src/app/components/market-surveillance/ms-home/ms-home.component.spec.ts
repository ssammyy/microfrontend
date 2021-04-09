import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MsHomeComponent} from './ms-home.component';

describe('MsHomeComponent', () => {
  let component: MsHomeComponent;
  let fixture: ComponentFixture<MsHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MsHomeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MsHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
