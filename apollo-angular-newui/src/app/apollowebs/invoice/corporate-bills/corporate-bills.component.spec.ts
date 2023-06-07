import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CorporateBillsComponent} from './corporate-bills.component';

describe('CorporateBillsComponent', () => {
  let component: CorporateBillsComponent;
  let fixture: ComponentFixture<CorporateBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CorporateBillsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CorporateBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
