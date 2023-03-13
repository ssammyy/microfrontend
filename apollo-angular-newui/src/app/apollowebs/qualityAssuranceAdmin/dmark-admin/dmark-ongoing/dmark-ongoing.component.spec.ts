import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkOngoingComponent } from './dmark-ongoing.component';

describe('DmarkOngoingComponent', () => {
  let component: DmarkOngoingComponent;
  let fixture: ComponentFixture<DmarkOngoingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkOngoingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkOngoingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
