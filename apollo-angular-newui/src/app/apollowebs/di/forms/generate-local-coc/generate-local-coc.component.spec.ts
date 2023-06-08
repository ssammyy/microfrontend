import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateLocalCocComponent } from './generate-local-coc.component';

describe('GenerateLocalCocComponent', () => {
  let component: GenerateLocalCocComponent;
  let fixture: ComponentFixture<GenerateLocalCocComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateLocalCocComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateLocalCocComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
