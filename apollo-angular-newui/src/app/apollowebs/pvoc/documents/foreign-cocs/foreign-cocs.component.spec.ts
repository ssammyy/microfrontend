import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ForeignCocsComponent} from './foreign-cocs.component';

describe('ForeignCocsComponent', () => {
  let component: ForeignCocsComponent;
  let fixture: ComponentFixture<ForeignCocsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForeignCocsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ForeignCocsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
