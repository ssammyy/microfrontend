import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwiListComponent } from './nwi-list.component';

describe('NwiListComponent', () => {
  let component: NwiListComponent;
  let fixture: ComponentFixture<NwiListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwiListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwiListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
