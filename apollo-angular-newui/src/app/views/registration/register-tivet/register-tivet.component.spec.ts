import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterTivetComponent } from './register-tivet.component';

describe('RegisterTivetComponent', () => {
  let component: RegisterTivetComponent;
  let fixture: ComponentFixture<RegisterTivetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterTivetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterTivetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
