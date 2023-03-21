import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsLevySitesComponent } from './standards-levy-sites.component';

describe('StandardsLevySitesComponent', () => {
  let component: StandardsLevySitesComponent;
  let fixture: ComponentFixture<StandardsLevySitesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsLevySitesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsLevySitesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
