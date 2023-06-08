import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatetechnicalcommitteeComponent } from './createtechnicalcommittee.component';

describe('CreatetechnicalcommitteeComponent', () => {
  let component: CreatetechnicalcommitteeComponent;
  let fixture: ComponentFixture<CreatetechnicalcommitteeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreatetechnicalcommitteeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatetechnicalcommitteeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
