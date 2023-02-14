import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KenyaStandardRequsitionFormComponent } from './kenya-standard-requsition-form.component';

describe('KenyaStandardRequsitionFormComponent', () => {
  let component: KenyaStandardRequsitionFormComponent;
  let fixture: ComponentFixture<KenyaStandardRequsitionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KenyaStandardRequsitionFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KenyaStandardRequsitionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
