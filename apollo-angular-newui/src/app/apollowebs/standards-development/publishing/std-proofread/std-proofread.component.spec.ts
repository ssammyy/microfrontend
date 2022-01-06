import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdProofreadComponent } from './std-proofread.component';

describe('StdProofreadComponent', () => {
  let component: StdProofreadComponent;
  let fixture: ComponentFixture<StdProofreadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdProofreadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdProofreadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
