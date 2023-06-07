import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdGazetteComponent } from './int-std-gazette.component';

describe('IntStdGazetteComponent', () => {
  let component: IntStdGazetteComponent;
  let fixture: ComponentFixture<IntStdGazetteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdGazetteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdGazetteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
