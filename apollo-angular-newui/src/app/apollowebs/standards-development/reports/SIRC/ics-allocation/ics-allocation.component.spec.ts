import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IcsAllocationComponent } from './ics-allocation.component';

describe('IcsAllocationComponent', () => {
  let component: IcsAllocationComponent;
  let fixture: ComponentFixture<IcsAllocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IcsAllocationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IcsAllocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
