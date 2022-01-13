import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCorporateComponent } from './view-corporate.component';

describe('ViewCorporateComponent', () => {
  let component: ViewCorporateComponent;
  let fixture: ComponentFixture<ViewCorporateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewCorporateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewCorporateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
