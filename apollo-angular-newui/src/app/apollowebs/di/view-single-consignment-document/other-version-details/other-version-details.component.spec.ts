import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherVersionDetailsComponent } from './other-version-details.component';

describe('OtherVersionDetailsComponent', () => {
  let component: OtherVersionDetailsComponent;
  let fixture: ComponentFixture<OtherVersionDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OtherVersionDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherVersionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
