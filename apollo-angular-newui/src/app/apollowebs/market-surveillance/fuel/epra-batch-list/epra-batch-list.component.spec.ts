import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpraBatchListComponent } from './epra-batch-list.component';

describe('EpraBatchListComponent', () => {
  let component: EpraBatchListComponent;
  let fixture: ComponentFixture<EpraBatchListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EpraBatchListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EpraBatchListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
