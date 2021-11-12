import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateproductSubCategoryComponent } from './createproduct-sub-category.component';

describe('CreateproductSubCategoryComponent', () => {
  let component: CreateproductSubCategoryComponent;
  let fixture: ComponentFixture<CreateproductSubCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateproductSubCategoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateproductSubCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
