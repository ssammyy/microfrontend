import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewCorComponent} from './view-cor.component';

describe('ViewCorComponent', () => {
  let component: ViewCorComponent;
  let fixture: ComponentFixture<ViewCorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewCorComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewCorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
