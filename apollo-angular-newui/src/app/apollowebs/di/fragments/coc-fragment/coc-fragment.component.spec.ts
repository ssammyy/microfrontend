import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CocFragmentComponent} from './coc-fragment.component';

describe('CocFragmentComponent', () => {
  let component: CocFragmentComponent;
  let fixture: ComponentFixture<CocFragmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CocFragmentComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CocFragmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
