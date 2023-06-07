import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RfcFragmentComponent} from './rfc-fragment.component';

describe('RfcFragmentComponent', () => {
  let component: RfcFragmentComponent;
  let fixture: ComponentFixture<RfcFragmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RfcFragmentComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RfcFragmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
