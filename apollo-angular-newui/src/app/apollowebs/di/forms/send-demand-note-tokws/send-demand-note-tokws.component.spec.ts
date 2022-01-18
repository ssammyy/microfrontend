import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendDemandNoteTokwsComponent } from './send-demand-note-tokws.component';

describe('SendDemandNoteTokwsComponent', () => {
  let component: SendDemandNoteTokwsComponent;
  let fixture: ComponentFixture<SendDemandNoteTokwsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SendDemandNoteTokwsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SendDemandNoteTokwsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
