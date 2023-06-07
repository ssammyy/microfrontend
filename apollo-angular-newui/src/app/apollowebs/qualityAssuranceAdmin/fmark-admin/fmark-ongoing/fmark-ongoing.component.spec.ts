import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkOngoingComponent } from './fmark-ongoing.component';

describe('FmarkOngoingComponent', () => {
  let component: FmarkOngoingComponent;
  let fixture: ComponentFixture<FmarkOngoingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkOngoingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkOngoingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
