import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDmarkPermitComponent } from './new-dmark-permit.component';

describe('NewDmarkPermitComponent', () => {
  let component: NewDmarkPermitComponent;
  let fixture: ComponentFixture<NewDmarkPermitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewDmarkPermitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewDmarkPermitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
