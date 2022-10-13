import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationsReceivedComponent } from './applications-received.component';

describe('ApplicationsReceivedComponent', () => {
  let component: ApplicationsReceivedComponent;
  let fixture: ComponentFixture<ApplicationsReceivedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApplicationsReceivedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationsReceivedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
