import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SourceForStandardComponent } from './source-for-standard.component';

describe('SourceForStandardComponent', () => {
  let component: SourceForStandardComponent;
  let fixture: ComponentFixture<SourceForStandardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SourceForStandardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SourceForStandardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
