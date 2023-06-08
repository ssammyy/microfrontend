import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpraListComponent } from './epra-list.component';

describe('EpraListComponent', () => {
  let component: EpraListComponent;
  let fixture: ComponentFixture<EpraListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EpraListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EpraListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
