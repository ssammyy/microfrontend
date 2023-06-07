import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddUpdateLimitComponent} from './add-update-limit.component';

describe('AddUpdateLimitComponent', () => {
  let component: AddUpdateLimitComponent;
  let fixture: ComponentFixture<AddUpdateLimitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddUpdateLimitComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUpdateLimitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
