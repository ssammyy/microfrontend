import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisseminateStandardComponent } from './disseminate-standard.component';

describe('DisseminateStandardComponent', () => {
  let component: DisseminateStandardComponent;
  let fixture: ComponentFixture<DisseminateStandardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DisseminateStandardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DisseminateStandardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
