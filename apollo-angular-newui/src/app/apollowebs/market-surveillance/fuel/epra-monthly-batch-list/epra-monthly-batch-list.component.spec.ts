import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpraMonthlyBatchListComponent } from './epra-monthly-batch-list.component';

describe('EpraMonthlyBatchListComponent', () => {
  let component: EpraMonthlyBatchListComponent;
  let fixture: ComponentFixture<EpraMonthlyBatchListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EpraMonthlyBatchListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EpraMonthlyBatchListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
