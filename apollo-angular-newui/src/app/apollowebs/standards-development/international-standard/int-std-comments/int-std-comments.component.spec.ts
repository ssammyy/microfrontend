import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdCommentsComponent } from './int-std-comments.component';

describe('IntStdCommentsComponent', () => {
  let component: IntStdCommentsComponent;
  let fixture: ComponentFixture<IntStdCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
