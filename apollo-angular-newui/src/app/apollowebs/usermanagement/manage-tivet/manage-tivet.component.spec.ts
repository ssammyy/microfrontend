import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTivetComponent } from './manage-tivet.component';

describe('ManageTivetComponent', () => {
  let component: ManageTivetComponent;
  let fixture: ComponentFixture<ManageTivetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageTivetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageTivetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
