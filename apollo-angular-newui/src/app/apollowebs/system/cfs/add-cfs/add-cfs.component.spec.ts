import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddCfsComponent} from './add-cfs.component';

describe('AddCfsComponent', () => {
  let component: AddCfsComponent;
  let fixture: ComponentFixture<AddCfsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddCfsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCfsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
