import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplyComponent } from './supply.component';

describe('SupplyComponent', () => {
  let component: SupplyComponent;
  let fixture: ComponentFixture<SupplyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SupplyComponent]
    });
    fixture = TestBed.createComponent(SupplyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
