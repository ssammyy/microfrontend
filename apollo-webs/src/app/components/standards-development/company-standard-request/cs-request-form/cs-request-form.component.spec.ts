import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CsRequestFormComponent } from './cs-request-form.component';

describe('CsRequestFormComponent', () => {
  let component: CsRequestFormComponent;
  let fixture: ComponentFixture<CsRequestFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CsRequestFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CsRequestFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
