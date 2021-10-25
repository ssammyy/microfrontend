import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdGazzetteComponent } from './int-std-gazzette.component';

describe('IntStdGazzetteComponent', () => {
  let component: IntStdGazzetteComponent;
  let fixture: ComponentFixture<IntStdGazzetteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdGazzetteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdGazzetteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
