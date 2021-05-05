import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PdListComponent } from './pd-list.component';

describe('PdListComponent', () => {
  let component: PdListComponent;
  let fixture: ComponentFixture<PdListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PdListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PdListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
