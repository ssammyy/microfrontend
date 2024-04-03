import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplyComponentComponent } from './supply-component.component';

describe('SupplyComponentComponent', () => {
  let component: SupplyComponentComponent;
  let fixture: ComponentFixture<SupplyComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SupplyComponentComponent]
    });
    fixture = TestBed.createComponent(SupplyComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
