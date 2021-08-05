import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicAnalyseCommentsComponent } from './systemic-analyse-comments.component';

describe('SystemicAnalyseCommentsComponent', () => {
  let component: SystemicAnalyseCommentsComponent;
  let fixture: ComponentFixture<SystemicAnalyseCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicAnalyseCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicAnalyseCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
