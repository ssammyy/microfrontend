import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendCoiComponent } from './send-coi.component';

describe('SendCoiComponent', () => {
  let component: SendCoiComponent;
  let fixture: ComponentFixture<SendCoiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SendCoiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SendCoiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
