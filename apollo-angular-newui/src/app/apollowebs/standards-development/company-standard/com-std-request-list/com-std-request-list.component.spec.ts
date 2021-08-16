import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdRequestListComponent } from './com-std-request-list.component';

describe('ComStdRequestListComponent', () => {
  let component: ComStdRequestListComponent;
  let fixture: ComponentFixture<ComStdRequestListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdRequestListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdRequestListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
