import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsReceivedComponent } from './standards-received.component';

describe('StandardsReceivedComponent', () => {
  let component: StandardsReceivedComponent;
  let fixture: ComponentFixture<StandardsReceivedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsReceivedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsReceivedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
