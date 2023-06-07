import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CorFragmentComponent} from './cor-fragment.component';

describe('CorFragmentComponent', () => {
  let component: CorFragmentComponent;
  let fixture: ComponentFixture<CorFragmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CorFragmentComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CorFragmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
