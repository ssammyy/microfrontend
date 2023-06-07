import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WaiverProductComponent} from './waiver-product.component';

describe('WaiverProductComponent', () => {
  let component: WaiverProductComponent;
  let fixture: ComponentFixture<WaiverProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WaiverProductComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WaiverProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
