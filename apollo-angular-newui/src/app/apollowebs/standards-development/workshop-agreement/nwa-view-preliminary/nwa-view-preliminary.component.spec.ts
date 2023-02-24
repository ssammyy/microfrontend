import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaViewPreliminaryComponent } from './nwa-view-preliminary.component';

describe('NwaViewPreliminaryComponent', () => {
  let component: NwaViewPreliminaryComponent;
  let fixture: ComponentFixture<NwaViewPreliminaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaViewPreliminaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaViewPreliminaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
