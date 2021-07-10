import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPermitComponent } from './new-permit.component';

describe('NewPermitComponent', () => {
  let component: NewPermitComponent;
  let fixture: ComponentFixture<NewPermitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewPermitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewPermitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
