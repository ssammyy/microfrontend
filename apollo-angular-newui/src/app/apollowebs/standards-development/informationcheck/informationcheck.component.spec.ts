import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InformationcheckComponent } from './informationcheck.component';

describe('InformationcheckComponent', () => {
  let component: InformationcheckComponent;
  let fixture: ComponentFixture<InformationcheckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InformationcheckComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InformationcheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
