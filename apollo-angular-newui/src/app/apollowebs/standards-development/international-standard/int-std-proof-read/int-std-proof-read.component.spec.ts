import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdProofReadComponent } from './int-std-proof-read.component';

describe('IntStdProofReadComponent', () => {
  let component: IntStdProofReadComponent;
  let fixture: ComponentFixture<IntStdProofReadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdProofReadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdProofReadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
