import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsProofreadComponent } from './standards-proofread.component';

describe('StandardsProofreadComponent', () => {
  let component: StandardsProofreadComponent;
  let fixture: ComponentFixture<StandardsProofreadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsProofreadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsProofreadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
