import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateLocalCorComponent } from './generate-local-cor.component';

describe('GenerateLocalCorComponent', () => {
  let component: GenerateLocalCorComponent;
  let fixture: ComponentFixture<GenerateLocalCorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateLocalCorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateLocalCorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
