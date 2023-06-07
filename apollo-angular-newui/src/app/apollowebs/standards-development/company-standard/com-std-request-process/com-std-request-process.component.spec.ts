import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdRequestProcessComponent } from './com-std-request-process.component';

describe('ComStdRequestProcessComponent', () => {
  let component: ComStdRequestProcessComponent;
  let fixture: ComponentFixture<ComStdRequestProcessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdRequestProcessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdRequestProcessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
