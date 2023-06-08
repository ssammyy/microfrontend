import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkOngoingComponent } from './smark-ongoing.component';

describe('SmarkOngoingComponent', () => {
  let component: SmarkOngoingComponent;
  let fixture: ComponentFixture<SmarkOngoingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkOngoingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkOngoingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
